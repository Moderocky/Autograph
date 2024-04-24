package org.valross.autograph.document;

public interface MultiNode extends Node {

    Node[] nodes();

    @Override
    default boolean isSingleLine() {
        for (Node node : this.nodes()) if (!node.isSingleLine()) return false;
        return true;
    }

}
