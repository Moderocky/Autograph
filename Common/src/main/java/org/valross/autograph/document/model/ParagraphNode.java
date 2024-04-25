package org.valross.autograph.document.model;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.*;
import org.valross.constantine.RecordConstant;

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

    @Override
    public HTMElement compile() {
        if (this.isSingleLine()) return StandardElements.P.child(nodes);
        return StandardElements.P.block().child(nodes);
    }

    @Override
    public String toString() {
        return "Paragraph[" + String.join(", ", nodes) + ']';
    }

}
