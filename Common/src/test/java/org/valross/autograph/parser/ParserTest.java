package org.valross.autograph.parser;

import org.junit.Test;

public abstract class ParserTest {

    protected abstract Parser<?> makeParser(String text);

    @Test
    public void isEscaped() {
        final Parser<?> parser = this.makeParser("\\c");
        assert !parser.isEscaped();
        assert parser.hasNext();
        assert parser.next() == 'c';
        assert parser.isEscaped();
    }

    @Test
    public void iterator() {
        final Parser<?> parser = this.makeParser("hello");
        assert parser.iterator() == parser;
        for (int i : parser) assert i > -1;
    }

    @Test
    public void hasNext() {
        final Parser<?> parser = this.makeParser("hello");
        assert parser.hasNext();
        assert parser.iterator().hasNext();
    }

    @Test
    public void next() {
        final Parser<?> parser = this.makeParser("hello");
        assert parser.hasNext();
        assert parser.next() == 'h';
    }

}