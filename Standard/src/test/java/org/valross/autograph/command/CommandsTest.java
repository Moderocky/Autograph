package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class CommandsTest extends DOMTest {

    @Test
    public void comment() throws IOException {
        this.test("&comment(foo bar something something)", "<body></body>");
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
        this.test("&table(&row(&cell(hello) &cell(there)))", "<body><table><tr><td>hello</td> " +
            "<td>there</td></tr></table></body>");
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
        final String content = "&article(foo)", expected = "<body><article class=\"ag-article\"><p>foo</p></article" +
            "></body>";
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

    @Test
    public void codeBlock() throws IOException {
        this.test("&codeBlock(foo)", "<body><pre><code>foo</code></pre></body>");
        this.test("&codeBlock(function())", "<body><pre><code>function()</code></pre></body>");
        this.test("&codeBlock(public void test(String string))", "<body><pre><code>public void test(String string)" +
            "</code></pre></body>");
        this.test("""
                      My Code:
                                            
                      &codeBlock(
                      something:
                        something else: foo()
                      )""", """
                      <body><p>My Code:</p><pre><code>something:
                        something else: foo()</code></pre></body>""");
        this.test("""
                      My Code:
                                            
                      &codeBlock(
                      something:
                        something else: foo()
                      )""", """
                      <body>
                      \t<p>My Code:</p>
                      \t<pre><code>something:
                        something else: foo()</code></pre>
                      </body>""", true);
    }

    @Test
    public void code() throws IOException {
        this.test("&code(foo)", "<body><code>foo</code></body>");
        this.test("&code(function())", "<body><code>function()</code></body>");
    }

    @Test
    public void quote() throws IOException {
        this.test("&quote(foo)", "<body><blockquote><p>foo</p></blockquote></body>");
    }

    @Test
    public void q() throws IOException {
        this.test("&q(foo)", "<body><q>foo</q></body>");
    }

    @Test
    public void cite() throws IOException {
        this.test("&cite(https://foo, my thing)", "<body><q cite=\"https://foo\" class=\"ag-citation\">my " +
            "thing<span class=\"ag-source\"><p>https://foo</p></span></q></body>");
        this.test("&cite(https://foo, my &b(cool) thing)", "<body><q cite=\"https://foo\" class=\"ag-citation\">my" +
            " <b>cool</b> thing<span class=\"ag-source\"><p>https://foo</p></span></q></body>");
        this.test("&cite(https://foo, my\n\ncool\n\nthing)", "<body><blockquote cite=\"https://foo\" " +
            "class=\"ag-citation\"><p>my</p><p>cool</p><p>thing</p><span " +
            "class=\"ag-source\"><p>https://foo</p></span></blockquote></body>");
    }

    @Test
    public void footnote() throws IOException {
        this.test("&article(A bold claim!&footnote(ok maybe not)\n\n&footer(references:))", "<body><article " +
            "class=\"ag-article\"><p>A bold claim!<sup class=\"ag-reference\"><a " +
            "href=\"#footnote-45ed3e14\">1</a></sup></p><footer class=\"ag-footer\"><p>references:</p><dl " +
            "class=\"ag-footnote\" id=\"footnote-45ed3e14\"><dt>1</dt><dd><p>ok maybe " +
            "not</p></dd></dl></footer></article></body>");
        this.test("&article(&cite(&footnote(ok maybe not), A bold claim!)\n\n&footer(references:))", "<body><article " +
            "class=\"ag-article\"><q class=\"ag-citation\">A bold claim!<sup class=\"ag-reference\"><a " +
            "href=\"#footnote-45ed3e14\">1</a></sup></q><footer class=\"ag-footer\"><p>references:</p><dl " +
            "class=\"ag-footnote\" id=\"footnote-45ed3e14\"><dt>1</dt><dd><p>ok maybe " +
            "not</p></dd></dl></footer></article></body>");
        this.test("&article(&cite(&footnote(foo)&footnote(bar), text)\n\n&footer())", "<body><article " +
            "class=\"ag-article\"><q class=\"ag-citation\">text<span class=\"ag-source\"><sup " +
            "class=\"ag-reference\"><a href=\"#footnote-18d05\">1</a></sup><sup class=\"ag-reference\"><a " +
            "href=\"#footnote-17c53\">2</a></sup></span></q><footer class=\"ag-footer\"><dl class=\"ag-footnote\" " +
            "id=\"footnote-18d05\"><dt>1</dt><dd><p>foo</p></dd></dl><dl class=\"ag-footnote\" " +
            "id=\"footnote-17c53\"><dt>2</dt><dd><p>bar</p></dd></dl></footer></article></body>");
    }

    @Test
    public void footer() throws IOException {
        this.test("&footer(foo)", "<body><footer class=\"ag-footer\"><p>foo</p></footer></body>");
        this.test("&article(&footer(foo))", "<body><article class=\"ag-article\"><footer " +
            "class=\"ag-footer\"><p>foo</p></footer></article></body>");
        this.test("""
                      &article(
                          ...as stated in my previous publication.&footnote(&i(The Odyssey), Homer, 800BC)
                         \s
                          &footer(References:)
                      )""", "<body><article class=\"ag-article\"><p>...as stated in my previous publication.<sup " +
                      "class=\"ag-reference\"><a href=\"#footnote-ebb08fe6\">1</a></sup></p><footer " +
                      "class=\"ag-footer\"><p>References:</p><dl class=\"ag-footnote\" " +
                      "id=\"footnote-ebb08fe6\"><dt>1</dt><dd><p><i>The Odyssey</i>, Homer, " +
                      "800BC</p></dd></dl></footer></article></body>");
    }

    @Test
    public void header() throws IOException {
        this.test("&header(foo)", "<body><header><p>foo</p></header></body>");
        this.test("&article(&header(foo))", "<body><article class=\"ag-article\"><header><p>foo</p></header></article" +
            "></body>");
    }

    @Test
    public void ruby() throws IOException {
        this.test("&ruby(hello, there)", "<body><ruby><p>there</p><rt>hello</rt></ruby></body>");
    }

    @Test
    public void details() throws IOException {
        this.test("&details(test)", "<body><details><p>test</p></details></body>");
        this.test("&details(&summary(foo)\n\nsomething else)", "<body><details><summary><p>foo</p></summary><p>something else</p></details></body>");
    }

    @Test
    public void summary() throws IOException {
        this.test("&details(&summary(foo)\n\nsomething else)", "<body><details><summary><p>foo</p></summary><p>something else</p></details></body>");
    }

    @Test
    public void em() throws IOException {
        this.test("&em(testing thing)", "<body><em>testing thing</em></body>");
        this.test("this is &em(very) good", "<body><p>this is <em>very</em> good</p></body>");
        this.test("&em(&i(testing) &b(thing))", "<body><em><i>testing</i> <b>thing</b></em></body>");
    }

}