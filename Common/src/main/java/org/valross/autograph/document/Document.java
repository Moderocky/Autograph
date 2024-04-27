package org.valross.autograph.document;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.constantine.RecordConstant;

import java.util.Arrays;

public record Document(Node... nodes) implements MultiNode, Node, ModelNode, RecordConstant {

    @Override
    public HTMElement compile() {
        return StandardElements.BODY.child(nodes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(nodes);
    }

    @Override
    public String toString() {
        return "Document[" + String.join(", ", nodes) + ']';
    }

}
