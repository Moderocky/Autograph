package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.TextNode;

public class TestCommandParser extends CommandParser<Node> {

    static final CommandDefinition TEST = new CommandDefinition("test", TestCommandParser::new);

    public TestCommandParser(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public Node parse() {
        final StringBuilder builder = new StringBuilder("hello ");
        for (int c : this) {
            builder.append((char) c);
        }
        return new TextNode(builder.toString());
    }

}
