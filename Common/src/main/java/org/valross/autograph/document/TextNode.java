package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

public record TextNode(String value) implements Node, RecordConstant {

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

}
