package org.valross.autograph.command.article;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.Source;
import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class FigureReferenceCommand extends CommandParser<Node> {

    protected final ArticleCommand article;

    public FigureReferenceCommand(Source source, CommandSet commands) {
        super(source, commands);
        this.article = this.findOuter(ArticleCommand.class);
    }

    @Override
    public Node parse() throws IOException {
        final String name = this.readLine().trim();
        if (name.isEmpty()) throw new CommandException("Figure ID must not be blank", this);
        final Supplier<HTMNode> supplier = () -> {
            final FigureCommand.FigureStub stub = FigureCommand.compileStub(article, name);
            final HTMElement link = StandardElements.A.href('#' + stub.id()).alt("Figure " + stub.index());
            return new HTMNode(link.classes("ag-fig"), new TextNode("Fig. " + stub.index()));
        };
        return new LazyNode(supplier, name.hashCode());
    }

    protected record LazyNode(Supplier<HTMNode> supplier, int hash) implements Node, RecordConstant {

        @Override
        public void write(OutputStream outputStream, Charset charset) throws IOException {
            this.supplier.get().write(outputStream, charset);
        }

        @Override
        public int hashCode() {
            return hash;
        }

    }

}
