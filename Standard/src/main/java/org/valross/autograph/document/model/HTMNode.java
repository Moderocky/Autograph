package org.valross.autograph.document.model;

import mx.kenzie.hypertext.element.HTMElement;
import org.valross.autograph.document.ModelNode;
import org.valross.autograph.document.Node;

import java.lang.constant.Constable;

public record HTMNode(HTMElement element, Node... contents) implements ModelNode {

    @Override
    public HTMElement compile() {
        return element.child(contents);
    }

    @Override
    public Constable[] serial() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?>[] canonicalParameters() {
        throw new UnsupportedOperationException();
    }

}
