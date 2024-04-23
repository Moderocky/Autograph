package org.valross.autograph.parser;

import org.valross.autograph.document.Node;

public abstract class CommandParser<Result extends Node> extends ElementParser<Result> {

    public CommandParser(Source reader) {
        super(reader);
    }

    @Override
    protected boolean isTerminatingChar(int c) {
        return c == ')' || c == -1;
    }

}
