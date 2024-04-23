package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

public record CommandNode(String command, Node node) implements Node, RecordConstant {

    @Override
    public String toString() {
        return '&' + command + '(' + node.toString() + ')';
    }

}