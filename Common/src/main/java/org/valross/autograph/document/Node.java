package org.valross.autograph.document;

import mx.kenzie.hypertext.Writable;
import org.valross.constantine.Constant;

public interface Node extends Constant, Writable {

    default boolean isSingleLine() {
        return true;
    }

}
