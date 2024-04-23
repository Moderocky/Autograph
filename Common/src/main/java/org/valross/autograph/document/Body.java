package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public record Body(Node... nodes) implements RecordConstant, MultiNode {

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), nodes);
    }

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        for (Node node : nodes) node.write(stream, charset);
    }

}
