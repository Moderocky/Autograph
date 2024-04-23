package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.error.CommandException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CommandHeaderParser extends ElementParser<CommandNode> {

    private final Map<String, CommandDefinition.ParserSupplier> parsers;

    protected CommandHeaderParser(Source source) {
        super(source);
        this.parsers = new LinkedHashMap<>();
        for (CommandDefinition command : this.commands()) {
            this.parsers.put(command.command(), command.parser());
        }
    }

    @Override
    public CommandNode parse() throws IOException {
        final StringBuilder builder = new StringBuilder();
        read:
        for (int c : this) {
            if (super.length >= 64)
                throw new CommandException("Command name exceeded the maximum length: &" + builder);
            switch (c) {
                case -1:
                    throw new CommandException("Command definition ran out of characters: &" + builder);
                case '(':
                    if (!this.isEscaped()) {
                        break read;
                    }
                default:
                    builder.append((char) c);
            }
        }
        final String text = builder.toString().trim().intern();
        final CommandDefinition.ParserSupplier supplier = parsers.get(text);
        if (supplier == null) throw new CommandException("Unknown command: &" + text);
        try (ElementParser<? extends Node> parser = this.delegate(supplier)) {
            final Node parsed = parser.parse();
            if (this.next() != ')')
                throw new CommandException("Invalid command closer");
            return new CommandNode(text, parsed);
        }
    }

    public abstract CommandDefinition[] commands();

}