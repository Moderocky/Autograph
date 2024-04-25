package org.valross.autograph.document.model;

import mx.kenzie.hypertext.element.HTMElement;
import org.valross.autograph.document.ModelNode;
import org.valross.autograph.document.Node;

import java.lang.constant.Constable;

public record HTMNode(HTMElement element, Node... contents) implements ModelNode {

    public HTMNode(HTMElement element, Node... contents) {
        this.element = element.working();
        this.contents = contents;
    }

    @Override
    public HTMElement compile() {
        if (element.isFrozen()) return element;
        return element.child(contents).finalise();
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
