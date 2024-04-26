package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.TextNode;
import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.error.AutographException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextAreaParser extends ElementParser<Node> implements MultiNodeParser {

    private final CommandDefinition[] commands;
    private final List<Node> nodes;

    public TextAreaParser(Source source, CommandDefinition... commands) {
        super(source);
        this.commands = commands;
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
        if (text.isEmpty()) return;
        this.addNode(new TextNode(text));
    }

    @Override
    public Node parse() {
        boolean lineBreak = false;
        StringBuilder text = new StringBuilder();
        read:
        for (final int c : this) {
            if (this.isTerminatingChar(c)) break;
            switch (c) {
                case '&':
                    final String string = text.toString();
                    if (!nodes.isEmpty() || !string.isBlank()) this.addTextNode(string);
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
                    if (lineBreak) break read;
                    else lineBreak = true;
                default:
                    text.append((char) c);
                    if (!Character.isWhitespace(c)) lineBreak = false;
            }
        }
        this.addTextNode(text.toString().stripTrailing());
        final Node[] nodes = this.nodes();
        if (nodes.length == 1 && nodes[0] instanceof CommandNode node) return node;
        return new ParagraphNode(nodes).simplify();
    }

    public CommandDefinition[] commands() {
        return commands;
    }

    public static class LocalCommandParser extends CommandHeaderParser {

        public LocalCommandParser(Source source, CommandDefinition... commands) {
            super(source, commands);
        }

    }

}
