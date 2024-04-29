package org.valross.autograph.command;

import mx.kenzie.hypertext.css.Rule;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.MultiNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class ColorCommand extends HTMCommandParser {

    public ColorCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final String color = this.nextArgument();
        if (this.next() != ',') throw new CommandException("&color hex argument should be followed by content", this);
        final Node[] nodes = this.consume();
        if (this.setInnerNode(color, nodes) instanceof HTMNode node) return node;
        return new HTMNode(StandardElements.SPAN.style(Rule.style().rule("color", color)), nodes);
    }

    private Node setInnerNode(String color, Node... nodes) {
        if (nodes.length != 1) return null;
        final Node node = nodes[0];
        if (node instanceof HTMNode htm) {
            htm.element().style().rule("color", color);
            return htm;
        }
        if (node instanceof CommandNode command) return this.setInnerNode(color, command.node());
        if (node instanceof MultiNode multi) return this.setInnerNode(color, multi.nodes());
        return null;
    }

}
