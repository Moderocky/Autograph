package org.valross.autograph.command.article;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.command.Commands;
import org.valross.autograph.command.HTMCommandParser;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class FigureCommand extends HTMCommandParser {

    protected final ArticleCommand article;
    protected FigureStub stub;

    public FigureCommand(Source source, CommandSet commands) {
        super(source, commands);
        this.article = this.findOuter(ArticleCommand.class);
    }

    @Override
    public HTMNode parse() throws IOException {
        this.consumeWhitespace();
        final String name = this.nextArgument().trim();
        if (name.isEmpty()) throw new CommandException("Figure ID must not be blank", this);
        if (this.next() != ',') throw new CommandException("Figure has no content", this);
        this.stub = compileStub(article, name);
        final Node[] nodes = this.consume();
        return new HTMNode(StandardElements.FIGURE.id(stub.id()).classes("ag-figure"), nodes);
    }

    @Override
    protected Node[] consume() throws IOException {
        final CommandSet commands = this.commands().with(Commands.FIGURE_CAPTION);
        this.consumeWhitespace();
        do try (final InnerTextParser text = this.delegate(commands, InnerTextParser::new)) {
            this.addNode(text.parse());
        } while (this.hasNext());
        return this.nodes();
    }

    protected static FigureCommand.FigureStub compileStub(ArticleCommand article, String name) {
        int index = 0;
        for (FigureCommand.FigureStub figure : article.figures) {
            ++index;
            if (!figure.name.equals(name)) continue;
            return figure;
        }
        final FigureCommand.FigureStub stub = new FigureCommand.FigureStub(name, ++index);
        stub.id = "figure-" + Integer.toHexString(index ^ name.hashCode());
        article.figures.add(stub);
        return stub;
    }

    protected static class FigureStub {

        protected final int index;
        protected final String name;
        protected String id;

        FigureStub(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String name() {
            return name;
        }

        public String id() {
            return id;
        }

        public int index() {
            return index;
        }

    }

}
