package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class CommandsTest extends DOMTest {

    private void test(String content, String expected) throws IOException {
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void i() throws IOException {
        final String content = "&i(foo)", expected = "<body><i>foo</i></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void link() throws IOException {
        final String content = "&link(https://test)", expected = "<body><a href=\"https://test\">https://test</a" +
            "></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void softTable() throws IOException {
        final String content = "&softTable(1 by 1, hello)", expected =
            "<body><table><tr><td><p>hello</p></td></tr" + "></table></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void cell() throws IOException {
        final String content = "&cell(foo)", expected = "<body><td>foo</td></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void aside() throws IOException {
        final String content = "&aside(foo)", expected = "<body><aside><p>foo</p></aside></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void row() throws IOException {
        final String content = "&row(foo)", expected = "<body><tr>foo</tr></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void b() throws IOException {
        final String content = "&b(foo)", expected = "<body><b>foo</b></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void table() throws IOException {
        this.test("&table(foo)", "<body><table><p>foo</p></table></body>");
        this.test("&table(&row(&cell(hello) &cell(there)))", "<body><table><tr><td>hello</td> <td>there</td></tr></table></body>");
    }

    @Test
    public void s() throws IOException {
        final String content = "&s(foo)", expected = "<body><s>foo</s></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void u() throws IOException {
        final String content = "&u(foo)", expected = "<body><u>foo</u></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void article() throws IOException {
        final String content = "&article(foo)", expected = "<body><article><p>foo</p></article></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void html() throws IOException {
        final String content = "&html(<p>foo</p>)<p></p>", expected = "<body><p><p>foo</p>&lt;p&gt;&lt;/p&gt;" + "</p" +
            "></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

}