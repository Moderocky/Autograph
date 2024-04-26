package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.ArgumentParser;

import java.io.IOException;

public class CiteCommand extends HTMCommandParser {

    public CiteCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final Body citation;
        try (ReferenceParser delegate = this.delegate(ReferenceParser::new)) {
            citation = delegate.parse();
        }
        if (citation.isBlank()) throw new CommandException("Citation was blank", this);
        if (this.next() != ',') throw new CommandException("Citation requires second (quote) argument", this);
        this.consumeWhitespace();
        do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
            this.addNode(parser.parse());
        } while (this.hasNext());
        final HTMElement quote;
        if (this.getNodes().size() > 1 || !this.getNodes().getFirst().isSingleLine())
            quote = StandardElements.BLOCKQUOTE.classes("ag-citation");
        else {
            quote = StandardElements.Q.classes("ag-citation");
            if (this.getNodes().getFirst() instanceof ParagraphNode node)
                this.getNodes().set(0, node.unwrap());
        }
        if (citation.isText()) quote.set("cite", citation.asText().value());
        else this.addNode(citation);
        final Node[] nodes = this.nodes();
        return new HTMNode(quote, nodes);
    }

    static class ReferenceParser extends ArgumentParser<Node> {

        public ReferenceParser(Source source, CommandDefinition... commands) {
            super(source, commands);
        }

        @Override
        public Body parse() throws IOException {
            this.consumeWhitespace();
            do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
                this.addNode(parser.parse());
            } while (this.hasNext());
            return new Body(this.nodes());
        }

    }

}
