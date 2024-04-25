package org.valross.autograph.command;

import mx.kenzie.hypertext.element.MultiElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class CodeBlockCommand extends BalancedBracketParser {

    public CodeBlockCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final StringBuilder builder = new StringBuilder();
        for (int c : this) builder.append((char) c);
        final String string = builder.toString();
        final String adjusted = string.substring(this.firstLine(string)).stripTrailing().stripIndent();
        return new HTMNode(new MultiElement(StandardElements.PRE, StandardElements.CODE).finalise(),
                           new TextNode(adjusted));
    }

    private int firstLine(String value) {
        int found = -1;
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c == '\n') found = i;
            else if (!Character.isWhitespace(c)) break;
        }
        return found + 1;
    }

}
