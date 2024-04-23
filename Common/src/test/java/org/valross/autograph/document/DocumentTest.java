package org.valross.autograph.document;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.Writable;
import org.junit.Test;
import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.parser.AutographParser;
import org.valross.autograph.parser.Parser;

import java.io.IOException;

public class DocumentTest {

    @Test
    public void simple() throws IOException {
        final String content = "hello there", expected = "<body><p>hello there</p></body>";
        final Document document = this.parse(new AutographParser(content));
        this.test(expected, document);
    }

    @Test
    public void twoTextElements() throws IOException {
        final String content = "hello there\n\ngeneral kenobi", expected = "<body><p>hello there</p><p>general " +
            "kenobi</p></body>";
        final Document document = this.parse(new AutographParser(content));
        this.test(expected, document);
    }

    @Test
    public void commandOnly() throws IOException {
        final CommandDefinition hello = CommandDefinition.of("hello", () -> new TextNode("hello"));
        final String content = "&hello()", expected = "<body><p>hello</p></body>";
        final Document document = this.parse(new AutographParser(content, hello));
        this.test(expected, document);
    }

    @Test
    public void commandOnlyHead() throws IOException {
        final CommandDefinition hello = CommandDefinition.of("hello", (text) -> new TextNode("hello " + text));
        final String content = "&hello(there)", expected = "<body><p>hello there</p></body>";
        final Document document = this.parse(new AutographParser(content, hello));
        this.test(expected, document);
    }

    private void test(String expected, Writable actual) {
        final StringBuilder builder = new StringBuilder();
        try (final PageWriter writer = new PageWriter(builder)) {
            writer.write(actual);
        }
        assert expected.contentEquals(builder) : "expected vs got:\n" + expected + System.lineSeparator() + builder;
    }

    private <Result extends Node> Result parse(Parser<Result> parser)
        throws IOException {
        try (parser) {
            return parser.parse();
        }
    }

}