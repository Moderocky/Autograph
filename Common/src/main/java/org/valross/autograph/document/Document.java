package org.valross.autograph.document;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.constantine.RecordConstant;

public record Document(Node... nodes) implements MultiNode, Node, ModelNode, RecordConstant {

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), nodes);
    }

    @Override
    public HTMElement compile() {
        return StandardElements.BODY.child(nodes);
    }

}
