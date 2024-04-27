package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class LinkCommandTest extends DOMTest {

    @Test
    public void simple() throws IOException {
        final String content = "&link(https://foo)", expected = "<main class=\"autograph\"><a " +
            "href=\"https://foo\">https://foo</a></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void inText() throws IOException {
        final String content = "hello &link(https://foo) there", expected = "<main class=\"autograph\"><p>hello <a " +
            "href=\"https://foo\">https://foo</a> there</p></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void betweenText() throws IOException {
        final String content = "hello\n\n&link(https://foo)\n\nthere", expected = "<main class=\"autograph\"><p>hello" +
            "</p><a " +
            "href=\"https://foo\">https://foo</a><p>there</p></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void withContents() throws IOException {
        final String content = "&link(https://foo, hello there)", expected = "<main class=\"autograph\"><a " +
            "href=\"https://foo\"><p>hello there</p></a></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void withContentsCommand() throws IOException {
        final String content = "&link(https://foo, hello &b(there))", expected = "<main class=\"autograph\"><a " +
            "href=\"https://foo\"><p>hello <b>there</b></p></a></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void withContents2Commands() throws IOException {
        final String content = "&link(https://foo, &i(hello) &b(there))", expected = "<main class=\"autograph\"><a " +
            "href=\"https://foo\"><p><i>hello</i> <b>there</b></p></a></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void nestedCommands() throws IOException {
        final String content = "&link(https://foo, &i(&b(hello)))", expected = "<main class=\"autograph\"><a " +
            "href=\"https://foo\"><i><b>hello</b></i></a></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

}