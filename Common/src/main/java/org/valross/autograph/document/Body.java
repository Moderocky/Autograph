package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

public record Body(Node... nodes) implements RecordConstant, MultiNode {

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), nodes);
    }

}
