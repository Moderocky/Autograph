package org.valross.autograph.parser.command;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.Source;

public abstract class ArgumentParser<Result extends Node> extends CommandParser<Result> {

    public ArgumentParser(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    protected boolean isTerminatingChar(int c) {
        return switch (c) {
            case -1, ')', ',' -> true;
            default -> super.isTerminatingChar(c);
        };
    }

}
