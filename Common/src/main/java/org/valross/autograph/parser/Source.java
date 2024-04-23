package org.valross.autograph.parser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public abstract sealed class Source implements Closeable permits Parser, ReaderSource {

    abstract int readChar() throws IOException;

    abstract void reset() throws IOException;

    abstract void mark(int readAheadLimit) throws IOException;

}

final class ReaderSource extends Source {

    private final BufferedReader reader;

    ReaderSource(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public int readChar() throws IOException {
        return reader.read();
    }

    @Override
    public void reset() throws IOException {
        this.reader.reset();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        this.reader.mark(readAheadLimit);
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

}