package org.valross.autograph.parser;

import org.valross.autograph.document.Node;

import java.io.Reader;

public abstract non-sealed class ElementParser<Result extends Node> extends Parser<Result> {

    public ElementParser(Source source) {
        super(source);
    }

    public ElementParser(Reader reader) {
        super(reader);
    }

    @Override
    public void close() {
        this.closed = true;
    }

}
