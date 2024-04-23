package org.valross.autograph.command;

import org.valross.autograph.document.Node;
import org.valross.autograph.parser.ElementParser;
import org.valross.autograph.parser.Source;

import java.io.Reader;
import java.util.function.Function;

public record CommandDefinition(String command, ParserSupplier parser) {

    @FunctionalInterface
    public interface ParserSupplier extends Function<Source, ElementParser<? extends Node>> {
    }
}
