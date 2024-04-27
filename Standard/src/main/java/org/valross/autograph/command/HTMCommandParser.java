package org.valross.autograph.command;

import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.MultiCommandParser;

public abstract class HTMCommandParser extends MultiCommandParser<HTMNode> {

    public HTMCommandParser(Source source, CommandSet commands) {
        super(source, commands);
    }

}
