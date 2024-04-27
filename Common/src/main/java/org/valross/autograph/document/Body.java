package org.valross.autograph.document;

import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.error.AutographException;
import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Body(boolean safe, Node... nodes) implements RecordConstant, MultiNode {

    public Body(Node... nodes) {
        this(true, checkNodes(nodes));
    }

    public Body(boolean safe, Node... nodes) {
        this.safe = safe;
        this.nodes = nodes;
    }

    private static Node[] checkNodes(Node... nodes) {
        final List<Node> list = new ArrayList<>(nodes.length * 2);
        checkNodes(list, nodes);
        return list.toArray(new Node[0]);
    }

    private static void checkNodes(List<Node> list, Node... nodes) {
        for (Node node : nodes) checkNode(list, node);
    }

    private static void checkNode(List<Node> list, Node node) {
        if (node instanceof Body body) {
            for (Node inner : body.nodes) checkNode(list, inner);
        } else list.add(node);
    }

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        for (Node node : nodes) node.write(stream, charset);
    }

    public Body stripParagraphs() {
        final List<Node> list = new ArrayList<>(nodes.length * 2);
        for (Node node : nodes) {
            if (node instanceof ParagraphNode paragraph)
                checkNodes(list, paragraph.nodes());
            else checkNode(list, node);
        }
        return new Body(safe, list.toArray(new Node[0]));
    }

    public boolean isText() {
        if (nodes.length != 1) return false;
        if (nodes[0] instanceof TextNode) return true;
        return nodes[0] instanceof ParagraphNode node
            && node.nodes().length == 1
            && node.nodes()[0] instanceof TextNode;
    }

    public TextNode asText() {
        if (nodes[0] instanceof TextNode node) return node;
        else if (nodes[0] instanceof ParagraphNode node)
            return (TextNode) node.nodes()[0];
        throw new AutographException("Not a text");
    }

    @Override
    public boolean isEmpty() {
        return nodes.length == 0 || (nodes.length == 1 && nodes[0].isEmpty());
    }

    public boolean isBlank() {
        return nodes.length == 0
            || (nodes.length == 1 && nodes[0] instanceof TextNode node && node.value().isBlank());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(nodes);
    }

    @Override
    public String toString() {
        return "Body[" + String.join(", ", nodes) + ']';
    }

}
