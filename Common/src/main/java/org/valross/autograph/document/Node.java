package org.valross.autograph.document;

import org.valross.constantine.Constant;

public interface Node extends Constant, CharSequence {

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
