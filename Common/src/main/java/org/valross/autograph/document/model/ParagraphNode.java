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
        for (Node node : nodes) if (!(node instanceof TextNode)) return this;
        return new Body(nodes);
    }

    @Override
    public HTMElement compile() {
        if (this.isSingleLine()) return StandardElements.P.child(nodes);
        return StandardElements.P.block().child(nodes);
    }

}
