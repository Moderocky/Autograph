package org.valross.autograph.document;

import mx.kenzie.hypertext.internal.FormattedOutputStream;
import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public record TextNode(String value) implements Node, RecordConstant {

    private static String sanitise(String value) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            result.append(switch (c) {
                case '<' -> "&lt;";
                case '>' -> "&gt;";
                case '&' -> "&amp;";
                case '"' -> "&quot;";
                case '\'' -> "&apos;";
                default -> Character.toString(c);
            });
        }
        return result.toString();
    }

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        if (stream instanceof FormattedOutputStream format && !this.isSingleLine()) {
            final String[] lines = value.split("\n");
            format.write(lines[0].getBytes(charset));
            for (int i = 1; i < lines.length; i++) {
                format.writeLine();
                format.write(sanitise(lines[i]).getBytes(charset));
            }
        } else stream.write(sanitise(value).getBytes(charset));
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
    public boolean isSingleLine() {
        return value.indexOf('\n') == -1;
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

}
