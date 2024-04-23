package org.valross.autograph.parser.command;

import org.valross.autograph.document.Node;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class SimpleCommandParser extends CommandParser<Node> {

    private final SimpleCommand function;

    public SimpleCommandParser(SimpleCommand function, Source reader) {
        super(reader);
        this.function = function;
    }

    @Override
    public Node parse() throws IOException {
        return function.parse(this.readLine());
    }

    @FunctionalInterface
    public interface SimpleCommand {

        Node parse(String head) throws IOException;

    }

    @FunctionalInterface
    public interface HeadlessCommand extends SimpleCommandParser.SimpleCommand {

        Node parse() throws IOException;

        @Override
        default Node parse(String head) throws IOException {
            return parse();
        }

    }

}
