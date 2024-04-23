package org.valross.autograph.document;

import mx.kenzie.hypertext.Writable;
import org.valross.constantine.Constant;

public interface Node extends Constant, CharSequence, Writable {

    @Override
    default int length() {
        return this.toString().length();
    }

    @Override
    default char charAt(int index) {
        return this.toString().charAt(index);
    }

    @Override
    default CharSequence subSequence(int start, int end) {
        return this.toString().subSequence(start, end);
    }

}
