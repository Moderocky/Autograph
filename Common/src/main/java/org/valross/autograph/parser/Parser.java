package org.valross.autograph.parser;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.function.BiFunction;

public abstract sealed class Parser<Result extends Node> extends Source
    implements Closeable, Iterable<Integer>, Iterator<Integer>
    permits AutographParser, ElementParser {

    public static final int ESCAPE_CHAR = '\\';
    private static final int EMPTY = -2;

    private final Source source;
    protected boolean closed;
    int length;
    private boolean escaped;
    private int current = EMPTY;

    public Parser(InputStream stream) {
        this(new InputStreamReader(stream));
    }

    public Parser(Reader reader) {
        if (reader instanceof BufferedReader buffered) this.source = new ReaderSource(buffered);
        else this.source = new ReaderSource(new BufferedReader(reader));
    }

    public Parser(Source source) {
        this.source = source;
    }

    protected static InputStream input(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    public abstract Result parse() throws IOException;

    public abstract CommandDefinition[] commands();

    private void readChar() throws IOException {
        //<editor-fold desc="Store the next (unescaped) character" defaultstate="collapsed">
        if (closed) {
            this.current = -1;
            return;
        }
        this.source.mark(8);
        do {
            if (length >= this.maxLength()) {
                this.closed = true;
                this.source.reset();
                this.current = -1;
                return;
            }
            this.current = source.read();
            switch (current) {
                case -1:
                    this.closed = true;
                    return;
                case ESCAPE_CHAR:
                    this.escaped ^= true;
                    if (escaped) continue;
                default:
                    if (!escaped && this.isTerminatingChar(current)) {
                        this.source.reset();
                        this.current = -1;
                        this.closed = true;
                    } else ++length;
                    return;
            }
        } while (true);
        //</editor-fold>
    }

    protected int maxLength() {
        return Short.MAX_VALUE;
    }

    protected boolean isTerminatingChar(int c) {
        return c == -1;
    }

    protected boolean isEscaped() {
        return escaped;
    }

    protected int current() {
        return current;
    }

    public <Type extends ElementParser<? extends Node>>
    Type delegate(BiFunction<Source, CommandDefinition[], Type> supplier) {
        return supplier.apply(this, this.commands());
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        //<editor-fold desc="Check if upcoming character > -1" defaultstate="collapsed">
        if (length >= this.maxLength()) return false;
        try {
            this.source.mark(4);
            try {
                return !this.isTerminatingChar(this.source.read());
            } finally {
                this.source.reset();
            }
        } catch (IOException ex) {
            return false;
        }
        //</editor-fold>
    }

    @Override
    public Integer next() {
        //<editor-fold desc="Shift next & return current" defaultstate="collapsed">
        try {
            this.readChar();
            return current;
        } catch (IOException ex) {
            return current = -1;
        }
        //</editor-fold>
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        this.source.close();
    }

    /**
     * Returns the currently-operating character into the buffer if possible.
     * If the buffer has been reset since, this will have no effect.
     */
    protected void stowChar() throws IOException {
        this.source.reset();
    }

    @Override
    public int read() throws IOException {
        if (closed) return -1;
        if (length >= this.maxLength()) {
            this.closed = true;
            return -1;
        }
        return source.read();
    }

    @Override
    public int read(char[] chars, int offset, int length) throws IOException {
        return source.read(chars, offset, length);
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        this.source.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        this.source.reset();
    }

    @Override
    public int caret() {
        return source.caret();
    }

    public Source source() {
        return source;
    }

    /**
     * Wraps this source in a checking reader that pulls characters from its
     * iterator rather than its read method: i.e. this parser keeps control over
     * its terminator rather than deferring it to whatever is using it.
     */
    public Source checkedSource() {
        return new CheckedSource(this);
    }

}
