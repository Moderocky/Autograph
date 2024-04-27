package org.valross.autograph.parser.command;

import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.MultiNodeParser;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MultiCommandParser<Result extends Node> extends CommandParser<Result> implements MultiNodeParser {

    private final List<Node> nodes;

    public MultiCommandParser(Source source, CommandSet commands) {
        super(source, commands);
        this.nodes = new ArrayList<>();
    }

    protected Node[] consume() throws IOException {
        this.consumeWhitespace();
        do try (final InnerTextParser text = this.delegate(InnerTextParser::new)) {
            this.addNode(text.parse());
        } while (this.hasNext());
        return this.nodes();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public Node[] nodes() {
        return nodes.toArray(new Node[0]);
    }

    protected List<Node> getNodes() {
        return nodes;
    }

}
