package org.valross.autograph.parser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public abstract sealed class Source implements Closeable permits Parser, ReaderSource {

    abstract int readChar() throws IOException;

    abstract void reset() throws IOException;

    abstract void mark(int readAheadLimit) throws IOException;

    public abstract int caret();

}

final class ReaderSource extends Source {

    private final BufferedReader reader;
    private int caret, mark, limit;

    ReaderSource(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public int readChar() throws IOException {
        try {
            return reader.read();
        } finally {
            ++this.caret;
            if (caret > (mark + limit)) {
                this.mark = limit = 0;
            }
        }
    }

    @Override
    public void reset() throws IOException {
        this.reader.reset();
        this.caret = mark;
        this.mark = limit = 0;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        this.limit = readAheadLimit;
        this.mark = caret;
        this.reader.mark(readAheadLimit);
    }

    @Override
    public int caret() {
        return caret;
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

}