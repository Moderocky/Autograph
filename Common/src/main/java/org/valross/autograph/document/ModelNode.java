package org.valross.autograph.document;

import mx.kenzie.hypertext.element.HTMElement;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * A model node represents a document element.
 */
public interface ModelNode extends Node {

    HTMElement compile();

    @Override
    default void write(OutputStream outputStream, Charset charset) throws IOException {
        this.compile().write(outputStream, charset);
    }

}
