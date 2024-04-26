package org.valross.autograph.document.model;

import mx.kenzie.hypertext.Writable;
import mx.kenzie.hypertext.element.HTMElement;
import org.valross.autograph.document.ModelNode;

import java.lang.constant.Constable;
import java.util.Arrays;
import java.util.Objects;

public record HTMNode(HTMElement element, Writable... contents) implements ModelNode {

    public HTMNode(HTMElement element, Writable... contents) {
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

    @Override
    public String toString() {
        return "<" + element.getTag() + ">[" + String.join(", ", contents) + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(element.getTag(), Arrays.hashCode(contents));
    }

}
