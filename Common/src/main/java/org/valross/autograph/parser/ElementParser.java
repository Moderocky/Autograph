package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.Node;

public abstract non-sealed class ElementParser<Result extends Node> extends Parser<Result> {

    protected final CommandDefinition[] commands;

    public ElementParser(Source source, CommandDefinition... commands) {
        super(source);
        this.commands = commands;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    public String readLine() {
        final StringBuilder builder = new StringBuilder();
        for (int c : this) builder.append((char) c);
        return builder.toString();
    }

    @Override
    public CommandDefinition[] commands() {
        return commands;
    }

}
