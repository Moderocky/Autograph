package org.valross.autograph.parser.command;

import org.valross.autograph.document.TextNode;
import org.valross.autograph.parser.Source;

public class SimpleArgumentParser extends ArgumentParser<TextNode> {

    public SimpleArgumentParser(Source reader) {
        super(reader);
    }

    @Override
    public TextNode parse() {
        final StringBuilder builder = new StringBuilder();
        for (int c : this) {
            builder.append((char) c);
        }
        return new TextNode(builder.toString().strip());
    }

}
