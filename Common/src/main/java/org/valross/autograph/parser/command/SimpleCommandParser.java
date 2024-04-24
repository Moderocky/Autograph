package org.valross.autograph.parser.command;

import org.valross.autograph.document.Node;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.ElementParser;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCommandParser extends CommandParser<Node> {

    private final Command function;

    public SimpleCommandParser(Command function, Source reader) {
        super(reader);
        this.function = function;
    }

    @Override
    public Node parse() throws IOException {
        return function.accept(this);
    }

    public sealed interface Command {

        Node accept(ElementParser<?> parser) throws IOException;

    }

    @FunctionalInterface
    public non-sealed interface ArgumentCommand extends Command {

        @Override
        default Node accept(ElementParser<?> parser) throws IOException {
            final List<TextNode> arguments = new ArrayList<>();
            do {
                try (SimpleArgumentParser delegate = parser.delegate(SimpleArgumentParser::new)) {
                    arguments.add(delegate.parse());
                }
                parser.next();
            } while (parser.hasNext());
            return this.parse(arguments.toArray(new TextNode[0]));
        }

        Node parse(CharSequence... arguments) throws IOException;

    }

    @FunctionalInterface
    public non-sealed interface SimpleCommand extends Command {

        @Override
        default Node accept(ElementParser<?> parser) throws IOException {
            return this.parse(parser.readLine());
        }

        Node parse(String head) throws IOException;

    }

    @FunctionalInterface
    public interface HeadlessCommand extends SimpleCommand {

        Node parse() throws IOException;

        @Override
        default Node parse(String head) throws IOException {
            return parse();
        }

    }

}
