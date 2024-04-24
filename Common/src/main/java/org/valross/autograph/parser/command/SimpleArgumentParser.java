package org.valross.autograph.parser.command;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.parser.Source;

public class SimpleArgumentParser extends ArgumentParser<TextNode> {

    public SimpleArgumentParser(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public TextNode parse() {
        return new TextNode(this.read());
    }

    public String read() {
        final StringBuilder builder = new StringBuilder();
        for (int c : this) {
            builder.append((char) c);
        }
        return builder.toString().strip();
    }

}
