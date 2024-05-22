package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.ContentParser;
import org.valross.autograph.parser.command.MultiCommandParser;

import java.io.IOException;

public class CiteCommand extends MultiCommandParser<Node> {

    public CiteCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public Node parse() throws IOException {
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
        return this.selectQuoteMode(citation);
    }

    private Node selectQuoteMode(Body citation) {
        final boolean block = (this.getNodes().size() > 1 || !this.getNodes().getFirst().isSingleLine());
        final boolean isFootnoteOnly = this.areAllFootnotes(citation.nodes());
        final HTMElement quote;
        if (block) quote = StandardElements.BLOCKQUOTE.classes("ag-citation");
        else quote = StandardElements.Q.classes("ag-citation");
        if (citation.isText()) quote.set("cite", citation.asText().value());

        final Node source;
        if (isFootnoteOnly) source = citation.stripParagraphs();
        else source = new HTMNode(StandardElements.A.classes("ag-source"), citation.stripParagraphs().nodes());
        if (block) this.addNode(source);
        else return new Body(new HTMNode(quote, this.stripNodes()), source);
        return new HTMNode(quote, this.nodes());
    }

    private Node[] stripNodes() {
        return new Body(false, this.nodes()).stripParagraphs().nodes();
    }

    private boolean areAllFootnotes(Node... nodes) {
        for (Node node : nodes) {
            if (node instanceof CommandNode command && command.command().equals("footnote")) continue;
            return false;
        }
        return true;
    }

}
