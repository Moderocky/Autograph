package org.valross.autograph.parser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public abstract sealed class Source extends Reader implements Closeable permits CheckedSource, Parser, ReaderSource {

    public abstract int read() throws IOException;

    public abstract void reset() throws IOException;

    public abstract void mark(int readAheadLimit) throws IOException;

    public abstract int caret();

    public abstract int read(char[] chars, int offset, int length) throws IOException;

}

final class CheckedSource extends Source {

    private final Parser<?> parser;

    CheckedSource(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public int read() {
        return parser.next();
    }

    @Override
    public void reset() throws IOException {
        this.parser.reset();
    }

    @Override
    public void close() throws IOException {
        this.parser.close();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        this.parser.mark(readAheadLimit);
    }

    @Override
    public int caret() {
        return parser.caret();
    }

    @Override
    public int read(char[] chars, int offset, int length) throws IOException {
        return parser.read(chars, offset, length);
    }

}

final class ReaderSource extends Source {

    private final BufferedReader reader;
    private int caret, mark, limit;

    ReaderSource(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public int read() throws IOException {
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
    public int read(char[] chars, int offset, int length) throws IOException {
        try {
            return reader.read(chars, offset, length);
        } finally {
            this.caret += length;
            if (caret > (mark + limit)) {
                this.mark = limit = 0;
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

}