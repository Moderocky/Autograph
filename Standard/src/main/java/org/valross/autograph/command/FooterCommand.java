package org.valross.autograph.command;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;

public class FooterCommand extends HTMCommandParser {

    protected final ArticleCommand article;

    public FooterCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
        this.article = this.findOuter(ArticleCommand.class);
    }

    @Override
    public HTMNode parse() throws IOException {
        this.consumeWhitespace();
        do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
            this.addNode(parser.parse());
        } while (this.hasNext());
        if (article != null) {
            for (HTMNode node : this.article.footnotes) this.addNode(node);
            this.article.footnotes.clear();
        }
        return new HTMNode(StandardElements.FOOTER.classes("ag-footer"), this.nodes());
    }

}
