package org.valross.autograph.command.article;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.command.HTMCommandParser;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class CaptionCommand extends HTMCommandParser {

    protected final FigureCommand figure;

    public CaptionCommand(Source source, CommandSet commands) {
        super(source, commands);
        this.figure = this.findOuter(FigureCommand.class);
    }

    @Override
    public HTMNode parse() throws IOException {
        final HTMElement index = StandardElements.SPAN.write("Figure " + figure.stub.index());
        this.consumeWhitespace();
        final Body body = new Body(this.consume());
        return new HTMNode(StandardElements.FIGCAPTION.classes("ag-caption"), index, body.isText() ? body.asText() :
            body);
    }

}
