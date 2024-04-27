package org.valross.autograph.command;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.LinkedList;

public class ArticleCommand extends HTMCommandParser {

    final LinkedList<HTMNode> footnotes = new LinkedList<>();

    public ArticleCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        return new HTMNode(StandardElements.ARTICLE.classes("ag-article"), this.consume());
    }

    @Override
    protected Node[] consume() throws IOException {
        final CommandSet commands = this.commands().with(Commands.FOOTNOTE);
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
