package org.valross.autograph.command;

import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.parser.CommandParser;
import org.valross.autograph.parser.MultiNodeParser;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.TextAreaParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class HTMCommandParser extends CommandParser<HTMNode> implements MultiNodeParser {

    private final List<Node> nodes;

    public HTMCommandParser(Source source, CommandDefinition... commands) {
        super(source, commands);
        this.nodes = new ArrayList<>();
    }

    protected Node[] consume() throws IOException {
        while (Character.isWhitespace(this.next()));
        this.stowChar();
        do try (final InnerTextParser text = this.delegate(InnerTextParser::new)) {
            this.addNode(text.parse());
        } while (this.hasNext());
        return this.nodes();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public Node[] nodes() {
        return nodes.toArray(new Node[0]);
    }

}
