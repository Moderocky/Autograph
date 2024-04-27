package org.valross.autograph.command;

import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public abstract class BalancedBracketParser extends HTMCommandParser {

    private int bracketDepth = 0;

    public BalancedBracketParser(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        return null;
    }

    @Override
    public Integer next() {
        final int c = super.next();
        if (this.isEscaped()) return c;
        switch (c) {
            case '(' -> ++this.bracketDepth;
            case ')' -> --this.bracketDepth;
        }
        return c;
    }

    @Override
    protected boolean isTerminatingChar(int c) {
        if (bracketDepth == 0) return super.isTerminatingChar(c);
        else return c == -1;
    }

}
