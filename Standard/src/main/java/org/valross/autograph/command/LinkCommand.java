package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HREFElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinkCommand extends HTMCommandParser {

    public LinkCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final String href = this.nextArgument();
        if (href.isBlank()) return new HTMNode(StandardElements.A);
        final HREFElement link = StandardElements.A.href(href);
        switch ((int) this.next()) {
            case ',':
                this.consumeWhitespace();
                final List<Node> nodes = new ArrayList<>();
                do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
                    nodes.add(parser.parse());
                } while (this.hasNext());
                return new HTMNode(link, nodes.toArray(new Node[0]));
            default:
                return new HTMNode(link.child(new TextNode(href)));
        }
    }

}
