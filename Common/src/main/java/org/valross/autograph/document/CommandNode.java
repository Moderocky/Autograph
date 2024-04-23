package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public record CommandNode(String command, Node node) implements Node, RecordConstant {

    @Override
    public String toString() {
        return '&' + command + '(' + node.toString() + ')';
    }

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        this.node.write(stream, charset);
    }

}