package org.valross.autograph.command;

import org.valross.autograph.document.Node;
import org.valross.autograph.parser.ElementParser;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.SimpleCommandParser;

import java.util.function.BiFunction;

public record CommandDefinition(String command, ParserSupplier parser) {

    public static CommandDefinition ofArguments(String command, SimpleCommandParser.ArgumentCommand function) {
        return new CommandDefinition(command, (source, _) -> new SimpleCommandParser(function, source));
    }

    public static CommandDefinition of(String command, SimpleCommandParser.SimpleCommand function) {
        return new CommandDefinition(command, (source, _) -> new SimpleCommandParser(function, source));
    }

    public static CommandDefinition of(String command, SimpleCommandParser.HeadlessCommand function) {
        return of(command, (SimpleCommandParser.SimpleCommand) function);
    }

    @FunctionalInterface
    public interface ParserSupplier extends BiFunction<Source, CommandDefinition[], ElementParser<? extends Node>> {
    }

    @Override
    public String toString() {
        return "&" + command;
    }

}

