package org.valross.autograph.parser.command;

import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.parser.Source;

public class SimpleArgumentParser extends ArgumentParser<TextNode> {

    public SimpleArgumentParser(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public TextNode parse() {
        return new TextNode(this.readAll());
    }

    public String readAll() {
        final StringBuilder builder = new StringBuilder();
        for (int c : this) {
            builder.append((char) c);
        }
        return builder.toString().strip();
    }

}
