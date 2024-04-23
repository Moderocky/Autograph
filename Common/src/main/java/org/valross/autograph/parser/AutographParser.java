package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.Document;
import org.valross.autograph.document.MultiNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.error.AutographException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public non-sealed class AutographParser extends Parser<Document> implements MultiNodeParser {

    private final List<Node> nodes;
    private final CommandDefinition[] commands;

    public AutographParser(String source, CommandDefinition... commands) {
        this(new StringReader(source), commands);
    }

    public AutographParser(InputStream stream, CommandDefinition... commands) {
        this(new BufferedReader(new InputStreamReader(stream)), commands);
    }

    public AutographParser(Reader reader, CommandDefinition... commands) {
        super(reader);
        this.nodes = new ArrayList<>();
        this.commands = commands;
    }

    @Override
    public Document parse() throws IOException {
        while (this.hasNext()) {
            try (final TextAreaParser text = this.delegate(TextAreaParser::new)) {
                this.addNode(text.parse());
            }
        }
        return new Document(this.nodes());
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public Node[] nodes() {
        return nodes.toArray(new Node[0]);
    }

    public class LocalCommandParser extends CommandHeaderParser {

        public LocalCommandParser(Source source) {
            super(source);
        }

        @Override
        public CommandDefinition[] commands() {
            return commands;
        }

    }

    public class TextAreaParser extends ElementParser<MultiNode> implements MultiNodeParser {

        private final List<Node> nodes;

        public TextAreaParser(Source source) {
            super(source);
            this.nodes = new ArrayList<>();
        }

        @Override
        public void addNode(Node node) {
            this.nodes.add(node);
        }

        @Override
        public Node[] nodes() {
            return nodes.toArray(new Node[0]);
        }

        protected void addTextNode(String text) {
            if (text.isBlank()) return;
            this.addNode(new TextNode(text));
        }

        @Override
        public MultiNode parse() {
            boolean lineBreak = false;
            StringBuilder text = new StringBuilder();
            read:
            for (int c : this) {
                switch (c) {
                    case -1:
                        break read;
                    case '&':
                        this.addTextNode(text.toString());
                        try (LocalCommandParser parser = this.delegate(LocalCommandParser::new)) {
                            this.addNode(parser.parse());
                        } catch (IOException ex) {
                            throw new AutographException(ex);
                        }
                        text = new StringBuilder();
                        continue;
                    case '\r':
                        continue;
                    case '\n':
                        if (lineBreak) {
                            break read;
                        } else lineBreak = true;
                    default:
                        text.append((char) c);
                        if (!Character.isWhitespace(c)) lineBreak = false;
                }
            }
            this.addTextNode(text.toString().stripTrailing());
            return new ParagraphNode(this.nodes());
        }

    }

}
