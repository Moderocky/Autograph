package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

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

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        stream.write(value.getBytes(charset));
    }

}
