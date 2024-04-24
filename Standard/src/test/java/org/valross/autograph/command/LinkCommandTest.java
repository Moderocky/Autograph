package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class LinkCommandTest extends DOMTest {

    @Test
    public void simple() throws IOException {
        final String content = "&link(https://foo)", expected = "<body><a href=\"https://foo\">https://foo</a></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void inText() throws IOException {
        final String content = "hello &link(https://foo) there", expected = "<body><p>hello <a " +
            "href=\"https://foo\">https://foo</a> there</p></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void betweenText() throws IOException {
        final String content = "hello\n\n&link(https://foo)\n\nthere", expected = "<body><p>hello</p><a " +
            "href=\"https://foo\">https://foo</a><p>there</p></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void withContents() throws IOException {
        final String content = "&link(https://foo, hello there)", expected = "<body><a href=\"https://foo\"><p>hello there</p></a></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

}