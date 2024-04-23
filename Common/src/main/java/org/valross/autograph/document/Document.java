package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

public record Document(Node... nodes) implements MultiNode, Node, RecordConstant {

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), nodes);
    }

}
