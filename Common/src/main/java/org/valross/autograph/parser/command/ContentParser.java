package org.valross.autograph.parser.command;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class ContentParser extends ArgumentParser<Node> {

    public ContentParser(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public Body parse() throws IOException {
        this.consumeWhitespace();
        do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
            this.addNode(parser.parse());
        } while (this.hasNext());
        return new Body(this.nodes());
    }

}
