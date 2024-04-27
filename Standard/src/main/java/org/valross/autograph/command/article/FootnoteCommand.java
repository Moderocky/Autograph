package org.valross.autograph.command.article;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.command.HTMCommandParser;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class FootnoteCommand extends HTMCommandParser {

    protected final ArticleCommand article;

    public FootnoteCommand(Source source, CommandSet commands) {
        super(source, commands);
        this.article = this.findOuter(ArticleCommand.class);
    }

    @Override
    public HTMNode parse() throws IOException {
        this.consumeWhitespace();
        do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
            this.addNode(parser.parse());
        } while (this.hasNext());
        final int index = article.footnotes.size() + 1;
        final Node[] contents = this.nodes();
        final int hash = Arrays.hashCode(contents);
        final String id = "footnote-" + Integer.toHexString(index ^ hash);
        final HTMElement list = StandardElements.DL.classes("ag-footnote").id(id);
        final HTMElement title = StandardElements.DT.write(String.valueOf(index));
        final HTMNode footnote = new HTMNode(StandardElements.DD, contents);
        this.article.footnotes.add(new HTMNode(list, title, footnote));
        final HTMElement link = StandardElements.A.href("#" + id).write(String.valueOf(index));
        return new HTMNode(StandardElements.SUP.classes("ag-reference"), link);
    }

    public static String dummyAnchor(String content) {
        int result = 31 + (31 + Objects.hashCode(content));
        return "footnote-" + Integer.toHexString(1 ^ result);
    }

}
