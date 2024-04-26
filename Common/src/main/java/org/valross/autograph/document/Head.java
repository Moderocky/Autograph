package org.valross.autograph.document;

import org.valross.constantine.RecordConstant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public record Head(Node... nodes) implements RecordConstant, MultiNode {

    @Override
    public String toString() {
        return "Head[" + String.join(", ", nodes) + ']';
    }

    @Override
    public void write(OutputStream stream, Charset charset) throws IOException {
        for (Node node : nodes) node.write(stream, charset);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(nodes);
    }

}
