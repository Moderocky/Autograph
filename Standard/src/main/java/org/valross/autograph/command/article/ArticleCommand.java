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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArticleCommand extends HTMCommandParser {

    final List<HTMNode> footnotes;
    final List<FigureCommand.FigureStub> figures;

    public ArticleCommand(Source source, CommandSet commands) {
        super(source, commands);
        this.footnotes = new LinkedList<>();
        this.figures = new ArrayList<>();
    }

    @Override
    public HTMNode parse() throws IOException {
        return new HTMNode(StandardElements.ARTICLE.classes("ag-article"), this.consume());
    }

    @Override
    protected Node[] consume() throws IOException {
        final CommandSet commands = this.commands().with(Commands.FOOTNOTE, Commands.FIGURE, Commands.FIGURE_REFERENCE);
        this.consumeWhitespace();
        do try (final InnerTextParser text = this.delegate(commands, InnerTextParser::new)) {
            this.addNode(text.parse());
        } while (this.hasNext());
        return this.nodes();
    }

    @Override
    public void close() {
        super.close();
        if (!this.footnotes.isEmpty())
            throw new CommandException("Footnotes were never printed in a '&footer()' area.", this);
    }

}
