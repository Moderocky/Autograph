package org.valross.autograph.command;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.ContentParser;

import java.io.IOException;

public class RubyCommand extends HTMCommandParser {

    public RubyCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final Body superscript;
        try (ContentParser delegate = this.delegate(ContentParser::new)) {
            superscript = delegate.parse();
        }
        if (superscript.isBlank()) throw new CommandException("Ruby subscript was blank", this);
        if (this.next() != ',') throw new CommandException("Ruby requires second (content) argument", this);
        this.consumeWhitespace();
        do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
            this.addNode(parser.parse());
        } while (this.hasNext());
        final HTMNode top;
        if (superscript.isText()) top = new HTMNode(StandardElements.RT, superscript.asText());
        else top = new HTMNode(StandardElements.RT, superscript.nodes());
        this.addNode(top);
        return new HTMNode(StandardElements.RUBY, this.nodes());
    }

}
