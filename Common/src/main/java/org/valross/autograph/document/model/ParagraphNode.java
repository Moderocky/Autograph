package org.valross.autograph.document.model;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.*;
import org.valross.constantine.RecordConstant;

import java.util.Arrays;

public record ParagraphNode(Node... nodes) implements MultiNode, ModelNode, RecordConstant {

    public ParagraphNode(String text) {
        this(new TextNode(text));
    }

    public Node simplify() {
        boolean anyText = false, anyNonText = false;
        for (Node node : nodes) {
            if (node instanceof TextNode) anyText = true;
            else anyNonText = true;
        }
        if (anyText) return this;
        if (anyNonText) return new Body(nodes);
        else return new EmptyNode();
    }

    public Node overSimplify() {
        return new Body(nodes);
    }

    public Node unwrap() {
        if (nodes.length == 1 && nodes[0] instanceof TextNode node) return node;
        return this.overSimplify();
    }

    @Override
    public HTMElement compile() {
        return StandardElements.P.child(nodes);
    }

    @Override
    public boolean isSingleLine() {
        return MultiNode.super.isSingleLine();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.nodes);
    }

    @Override
    public String toString() {
        return "Paragraph[" + String.join(", ", nodes) + ']';
    }

}
