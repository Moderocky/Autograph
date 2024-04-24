package org.valross.autograph.document;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.Writable;
import org.valross.autograph.parser.Parser;

import java.io.IOException;

public abstract class DOMTest {

    protected void test(String expected, Writable actual) {
        final StringBuilder builder = new StringBuilder();
        try (final PageWriter writer = new PageWriter(builder)) {
            writer.write(actual);
        }
        assert expected.contentEquals(builder) : "expected vs got:\n" + expected + System.lineSeparator() + builder;
    }

    protected <Result extends Node> Result parse(Parser<Result> parser) throws IOException {
        try (parser) {
            return parser.parse();
        }
    }

}
