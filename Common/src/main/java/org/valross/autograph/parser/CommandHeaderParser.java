package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.error.CommandException;

import java.io.IOException;

public abstract class CommandHeaderParser extends ElementParser<CommandNode> {

    private final CommandSet parsers;

    protected CommandHeaderParser(Source source, CommandSet commands) {
        super(source, commands);
        this.parsers = commands;
    }

    @Override
    public CommandNode parse() throws IOException {
        final String text = this.reader();
        final CommandDefinition.ParserSupplier supplier = parsers.get(text);
        if (supplier == null) throw new CommandException("Unknown command: &" + text, this);
        try (ElementParser<? extends Node> parser = this.delegate(supplier)) {
            final Node parsed = parser.parse();
            final int next = this.next();
            if (next != ')')
                throw new CommandException("Invalid command closer: " + next, this);
            return new CommandNode(text, parsed);
        }
    }

    private String reader() {
        final StringBuilder builder = new StringBuilder();
        read:
        for (int c : this) {
            if (super.length >= 64)
                throw new CommandException("Command name exceeded the maximum length: &" + builder, this);
            switch (c) {
                case -1:
                    throw new CommandException("Command definition ran out of characters: &" + builder, this);
                case '(':
                    if (!this.isEscaped()) {
                        break read;
                    }
                default:
                    builder.append((char) c);
            }
        }
        final String text = builder.toString().trim().intern();
        return text;
    }

}
