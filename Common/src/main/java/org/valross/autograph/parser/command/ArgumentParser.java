package org.valross.autograph.parser.command;

import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.MultiNodeParser;
import org.valross.autograph.parser.Source;

import java.util.ArrayList;
import java.util.List;

public abstract class ArgumentParser<Result extends Node> extends CommandParser<Result> implements MultiNodeParser {

    private final List<Node> nodes;

    public ArgumentParser(Source source, CommandSet commands) {
        super(source, commands);
        this.nodes = new ArrayList<>();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public Node[] nodes() {
        return nodes.toArray(new Node[0]);
    }

    @Override
    protected boolean isTerminatingChar(int c) {
        return switch (c) {
            case -1, ')', ',' -> true;
            default -> super.isTerminatingChar(c);
        };
    }

}
