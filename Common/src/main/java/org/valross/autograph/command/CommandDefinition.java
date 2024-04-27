package org.valross.autograph.command;

import org.valross.autograph.document.Node;
import org.valross.autograph.parser.ElementParser;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.SimpleCommandParser;

import java.util.Objects;
import java.util.function.BiFunction;

public record CommandDefinition(CommandDefinition parent, String command, ParserSupplier parser) {

    public static CommandDefinition ofArguments(String command, SimpleCommandParser.ArgumentCommand function) {
        return of(command, (source, _) -> new SimpleCommandParser(function, source));
    }

    public static CommandDefinition of(String command, SimpleCommandParser.SimpleCommand function) {
        return of(command, (source, _) -> new SimpleCommandParser(function, source));
    }

    public static CommandDefinition of(String command, SimpleCommandParser.HeadlessCommand function) {
        return of(command, (SimpleCommandParser.SimpleCommand) function);
    }

    public static CommandDefinition of(String command, ParserSupplier parser) {
        return new CommandDefinition(null, command, parser);
    }

    public CommandDefinition subcommand(String command, ParserSupplier parser) {
        return new CommandDefinition(this, command, parser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandDefinition that)) return false;
        return Objects.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        return command.hashCode();
    }

    @Override
    public String toString() {
        return "&" + command;
    }

    @FunctionalInterface
    public interface ParserSupplier extends BiFunction<Source, CommandSet, ElementParser<? extends Node>> {
    }

}

