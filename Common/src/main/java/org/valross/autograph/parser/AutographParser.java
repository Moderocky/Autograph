package org.valross.autograph.parser;

import org.valross.autograph.command.CommandSet;
import org.valross.autograph.document.Document;
import org.valross.autograph.document.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public non-sealed class AutographParser extends Parser<Document> implements MultiNodeParser {

    private final List<Node> nodes;
    private final CommandSet commands;

    public AutographParser(String source, CommandSet commands) {
        this(new StringReader(source), commands);
    }

    public AutographParser(InputStream stream, CommandSet commands) {
        this(new BufferedReader(new InputStreamReader(stream)), commands);
    }

    public AutographParser(Reader reader, CommandSet commands) {
        super(reader);
        this.nodes = new ArrayList<>();
        this.commands = commands;
    }

    @Override
    public Document parse() throws IOException {
        do try (final TextAreaParser text = this.delegate(TextAreaParser::new)) {
            this.addNode(text.parse());
        } while (this.hasNext());
        return new Document(this.nodes());
    }

    @Override
    public CommandSet commands() {
        return commands;
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
