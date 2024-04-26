package org.valross.autograph.document;

import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.error.AutographException;
import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public record Body(Node... nodes) implements RecordConstant, MultiNode {

    @Override
    public String toString() {
        return "Body[" + String.join(", ", nodes) + ']';
    }

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        for (Node node : nodes) node.write(stream, charset);
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

}
