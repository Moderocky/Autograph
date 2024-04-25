package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CiteCommand extends HTMCommandParser {

    public CiteCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException { // todo maybe footnotes???
        final String href = this.nextArgument();
        if (href.isBlank()) throw new CommandException("Citation was blank", this);
        if (((int) this.next()) != ',') throw new CommandException("Citation requires second (quote) argument", this);
        this.consumeWhitespace();
        final List<Node> nodes = new ArrayList<>();
        do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
            nodes.add(parser.parse());
        } while (this.hasNext());
        final HTMElement quote;
        if (nodes.size() > 1) quote = StandardElements.BLOCKQUOTE.classes("ag-citation").set("cite", href);
        else quote = StandardElements.Q.classes("ag-citation").set("cite", href);
        return new HTMNode(quote, nodes.toArray(new Node[0]));
    }

}
