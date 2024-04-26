package org.valross.autograph.command;

import org.valross.autograph.document.EmptyNode;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class CommentCommand extends CommandParser<EmptyNode> {

    public CommentCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public EmptyNode parse() throws IOException {
        while (this.next() != -1) ;
        return new EmptyNode();
    }

}
