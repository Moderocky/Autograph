package org.valross.autograph.parser;

import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.command.SimpleArgumentParser;

public abstract class CommandParser<Result extends Node> extends ElementParser<Result> {

    public CommandParser(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    protected boolean isTerminatingChar(int c) {
        return c == ')' || super.isTerminatingChar(c);
    }

    /**
     * Returns the (potentially empty) text before the next unescaped comma , or closer )
     */
    protected String nextArgument() {
        try (SimpleArgumentParser parser = this.delegate(SimpleArgumentParser::new)) {
            return parser.readAll();
        }
    }

}
