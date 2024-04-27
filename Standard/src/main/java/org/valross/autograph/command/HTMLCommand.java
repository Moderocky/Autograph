package org.valross.autograph.command;

import mx.kenzie.hypertext.element.Page;
import mx.kenzie.hypertext.internal.HTMElementUnwrapper;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class HTMLCommand extends HTMCommandParser {

    public HTMLCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final Page page;
        try (HTMElementUnwrapper reader = new HTMElementUnwrapper(this.checkedSource())) {
            page = reader.unwrap();
        }
        return new HTMNode(page);
    }

}
