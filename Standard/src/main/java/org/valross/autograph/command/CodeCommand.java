package org.valross.autograph.command;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class CodeCommand extends BalancedBracketParser {

    public CodeCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final StringBuilder builder = new StringBuilder();
        for (int c : this) builder.append((char) c);
        return new HTMNode(StandardElements.CODE, new TextNode(builder.toString()));
    }

}
