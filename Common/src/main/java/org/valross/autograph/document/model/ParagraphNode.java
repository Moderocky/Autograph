package org.valross.autograph.document.model;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.ModelNode;
import org.valross.autograph.document.MultiNode;
import org.valross.autograph.document.Node;
import org.valross.constantine.RecordConstant;

public record ParagraphNode(Node... nodes) implements MultiNode, ModelNode, RecordConstant {

    @Override
    public HTMElement compile() {
        return StandardElements.P.child(nodes);
    }

}
