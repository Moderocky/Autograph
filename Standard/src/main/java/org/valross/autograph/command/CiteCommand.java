package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.ContentParser;

import java.io.IOException;
import java.util.List;

public class CiteCommand extends HTMCommandParser {

    public CiteCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final Body citation;
        try (ContentParser delegate = this.delegate(ContentParser::new)) {
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
        final Node[] nodes = this.decide(citation, quote);
        return new HTMNode(quote, nodes);
    }

    private Node[] decide(Body citation, HTMElement quote) {
        if (citation.isText()) quote.set("cite", citation.asText().value());
        if (this.areAllFootnotes(citation.nodes()))
            this.getNodes().addAll(List.of(citation.nodes()));
        else this.addNode(new HTMNode(StandardElements.SPAN.classes("ag-source"), citation.nodes()));
        return this.nodes();
    }

    private boolean areAllFootnotes(Node... nodes) {
        for (Node node : nodes) {
            if (node instanceof CommandNode command && command.command().equals("footnote")) continue;
            return false;
        }
        return true;
    }

}
