package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class CommandsTest extends DOMTest {

    @Test
    public void comment() throws IOException {
        this.test("&comment(foo bar something something)", "<main class=\"autograph\"></main>");
    }

    @Test
    public void i() throws IOException {
        final String content = "&i(foo)", expected = "<main class=\"autograph\"><i>foo</i></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void link() throws IOException {
        final String content = "&link(https://test)", expected = "<main class=\"autograph\"><a " + "href=\"https" +
            "://test\">https://test</a" + "></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void softTable() throws IOException {
        final String content = "&softTable(1 by 1, hello)", expected = "<main class=\"autograph\"><table><tr><td><p" +
            ">hello</p></td></tr" + "></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void cell() throws IOException {
        final String content = "&cell(foo)", expected = "<main class=\"autograph\"><td>foo</td></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void aside() throws IOException {
        final String content = "&aside(foo)", expected = "<main class=\"autograph\"><aside><p>foo</p></aside></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void row() throws IOException {
        final String content = "&row(foo)", expected = "<main class=\"autograph\"><tr>foo</tr></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void b() throws IOException {
        final String content = "&b(foo)", expected = "<main class=\"autograph\"><b>foo</b></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void table() throws IOException {
        this.test("&table(foo)", "<main class=\"autograph\"><table><p>foo</p></table></main>");
        this.test("&table(&row(&cell(hello) &cell(there)))",
                  "<main class=\"autograph\"><table><tr><td>hello</td> " + "<td>there</td></tr></table></main>");
    }

    @Test
    public void s() throws IOException {
        final String content = "&s(foo)", expected = "<main class=\"autograph\"><s>foo</s></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void u() throws IOException {
        final String content = "&u(foo)", expected = "<main class=\"autograph\"><u>foo</u></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void article() throws IOException {
        final String content = "&article(foo)", expected = "<main class=\"autograph\"><article " + "class=\"ag" +
            "-article\"><p>foo</p></article" + "></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void html() throws IOException {
        final String content = "&html(<p>foo</p>)<p></p>", expected =
            "<main class=\"autograph\"><p><p>foo</p>&lt;" + "p&gt;&lt;/p&gt;" + "</p" + "></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void codeBlock() throws IOException {
        this.test("&codeBlock(foo)", "<main class=\"autograph\"><pre><code>foo</code></pre></main>");
        this.test("&codeBlock(function())", "<main class=\"autograph\"><pre><code>function()</code></pre></main>");
        this.test("&codeBlock(public void test(String string))",
                  "<main class=\"autograph\"><pre><code>public void " + "test(String string)" + "</code></pre></main>");
        this.test("""
                      My Code:
                                            
                      &codeBlock(
                      something:
                        something else: foo()
                      )""", """
                      <main class="autograph"><p>My Code:</p><pre><code>something:
                        something else: foo()</code></pre></main>""");
        this.test("""
                      My Code:
                                            
                      &codeBlock(
                      something:
                        something else: foo()
                      )""", """
                      <main class="autograph">
                      \t<p>My Code:</p>
                      \t<pre><code>something:
                        something else: foo()</code></pre>
                      </main>""", true);
    }

    @Test
    public void code() throws IOException {
        this.test("&code(foo)", "<main class=\"autograph\"><code>foo</code></main>");
        this.test("&code(function())", "<main class=\"autograph\"><code>function()</code></main>");
    }

    @Test
    public void quote() throws IOException {
        this.test("&quote(foo)", "<main class=\"autograph\"><blockquote><p>foo</p></blockquote></main>");
    }

    @Test
    public void q() throws IOException {
        this.test("&q(foo)", "<main class=\"autograph\"><q>foo</q></main>");
    }

    @Test
    public void cite() throws IOException {
        this.test("&cite(https://foo, my thing)", "<main class=\"autograph\"><q cite=\"https://foo\" " +
            "class=\"ag-citation\">my thing</q><a class=\"ag-source\">https://foo</a></main>");
        this.test("&cite(https://foo, my &b(cool) thing)", "<main class=\"autograph\"><q cite=\"https://foo\" " +
            "class=\"ag-citation\">my <b>cool</b> thing</q><a class=\"ag-source\">https://foo</a></main>");
        this.test("&cite(https://foo, my\n\ncool\n\nthing)", "<main class=\"autograph\"><blockquote " +
            "cite=\"https://foo\" class=\"ag-citation\"><p>my</p><p>cool</p><p>thing</p><a " +
            "class=\"ag-source\">https://foo</a></blockquote></main>");
    }

    @Test
    public void footnote() throws IOException {
        this.test("&article(A bold claim!&footnote(ok maybe not)\n\n&footer(references:))",
                  "<main class=\"autograph" + "\"><article " + "class=\"ag-article\"><p>A bold claim!<sup " +
                      "class=\"ag-reference\"><a " + "href=\"#footnote-45ed3e14\">1</a></sup></p><footer " +
                      "class=\"ag-footer\"><p>references:</p><dl " + "class=\"ag-footnote\" " +
                      "id=\"footnote-45ed3e14\"><dt>1</dt><dd><p>ok maybe " + "not</p></dd></dl></footer></article" +
                      "></main>");
        this.test("&article(&cite(&footnote(ok maybe not), A bold claim!)\n\n&footer(references:))", "<main class=\"autograph\"><article class=\"ag-article\"><q class=\"ag-citation\">A bold claim!</q><sup class=\"ag-reference\"><a href=\"#footnote-45ed3e14\">1</a></sup><footer class=\"ag-footer\"><p>references:</p><dl class=\"ag-footnote\" id=\"footnote-45ed3e14\"><dt>1</dt><dd><p>ok maybe not</p></dd></dl></footer></article></main>");
        this.test("&article(&cite(&footnote(foo)&footnote(bar), text)\n\n&footer())", "<main class=\"autograph\"><article class=\"ag-article\"><q class=\"ag-citation\">text</q><sup class=\"ag-reference\"><a href=\"#footnote-18d05\">1</a></sup><sup class=\"ag-reference\"><a href=\"#footnote-17c53\">2</a></sup><footer class=\"ag-footer\"><dl class=\"ag-footnote\" id=\"footnote-18d05\"><dt>1</dt><dd><p>foo</p></dd></dl><dl class=\"ag-footnote\" id=\"footnote-17c53\"><dt>2</dt><dd><p>bar</p></dd></dl></footer></article></main>");
    }

    @Test
    public void footer() throws IOException {
        this.test("&footer(foo)", "<main class=\"autograph\"><footer class=\"ag-footer\"><p>foo</p></footer></main>");
        this.test("&article(&footer(foo))", "<main class=\"autograph\"><article class=\"ag-article\"><footer " +
            "class=\"ag-footer\"><p>foo</p></footer></article></main>");
        this.test("""
                      &article(
                          ...as stated in my previous publication.&footnote(&i(The Odyssey), Homer, 800BC)
                         \s
                          &footer(References:)
                      )""",
                  "<main class=\"autograph\"><article class=\"ag-article\"><p>...as stated in my previous " +
                      "publication.<sup " + "class=\"ag-reference\"><a href=\"#footnote-ebb08fe6\">1</a></sup></p" +
                      "><footer " + "class=\"ag-footer\"><p>References:</p><dl class=\"ag-footnote\" " + "id" +
                      "=\"footnote-ebb08fe6\"><dt>1</dt><dd><p><i>The Odyssey</i>, Homer, " + "800BC</p></dd></dl" +
                      "></footer></article></main>");
    }

    @Test
    public void header() throws IOException {
        this.test("&header(foo)", "<main class=\"autograph\"><header><p>foo</p></header></main>");
        this.test("&article(&header(foo))", "<main class=\"autograph\"><article " + "class=\"ag-article\"><header><p" +
            ">foo</p></header></article" + "></main>");
    }

    @Test
    public void ruby() throws IOException {
        this.test("&ruby(hello, there)", "<main class=\"autograph\"><ruby><p>there</p><rt>hello</rt></ruby></main>");
    }

    @Test
    public void details() throws IOException {
        this.test("&details(test)", "<main class=\"autograph\"><details><p>test</p></details></main>");
        this.test("&details(&summary(foo)\n\nsomething else)", "<main class=\"autograph\"><details><summary><p>foo</p"
            + "></summary><p" + ">something else</p></details></main>");
    }

    @Test
    public void summary() throws IOException {
        this.test("&details(&summary(foo)\n\nsomething else)", "<main class=\"autograph\"><details><summary><p>foo</p"
            + "></summary><p" + ">something else</p></details></main>");
    }

    @Test
    public void em() throws IOException {
        this.test("&em(testing thing)", "<main class=\"autograph\"><em>testing thing</em></main>");
        this.test("this is &em(very) good", "<main class=\"autograph\"><p>this is <em>very</em> good</p></main>");
        this.test("&em(&i(testing) &b(thing))", "<main class=\"autograph\"><em><i>testing</i> " + "<b>thing</b></em" +
            "></main>");
    }

    @Test
    public void fig() throws IOException {
        this.test("&article(See &fig(Foo)\n\n&figure(Foo, hello))", "<main class=\"autograph\"><article " + "class" +
            "=\"ag-article\"><p>See <a " + "alt=\"Figure 1\" class=\"ag-figure-reference\" href=\"#figure-114a7\">Fig" +
            ". 1</a></p><figure " + "class=\"ag-figure\" " + "id=\"figure-114a7\"><p>hello</p></figure></article" +
            "></main>");
    }

    @Test(expected = CommandException.class)
    public void figNoName() throws IOException {
        this.test("&article(See &fig()\n\n&figure(Foo, hello))", "");
    }

    @Test
    public void caption() throws IOException {
        this.test("&article(See &fig(Foo)\n\n&figure(Foo, hello &caption(test)))", "<main class=\"autograph" +
            "\"><article class=\"ag-article\"><p>See <a alt=\"Figure 1\" class=\"ag-figure-reference\" " + "href" +
            "=\"#figure-114a7\">Fig. 1</a></p><figure class=\"ag-figure\" id=\"figure-114a7\"><p>hello " +
            "<figcaption class=\"ag-caption\"><span>Figure 1</span>test</figcaption></p></figure></article></main>");
    }

    @Test
    public void figure() throws IOException {
        this.test("&article(See &fig(Foo)\n\n&figure(Foo, hello &caption(test)))", "<main class=\"autograph" +
            "\"><article class=\"ag-article\"><p>See <a alt=\"Figure 1\" class=\"ag-figure-reference\" " + "href" +
            "=\"#figure-114a7\">Fig. 1</a></p><figure class=\"ag-figure\" id=\"figure-114a7\"><p>hello " +
            "<figcaption class=\"ag-caption\"><span>Figure 1</span>test</figcaption></p></figure></article></main>");
    }

    @Test(expected = CommandException.class)
    public void figureNoName() throws IOException {
        this.test("&article(See &fig(Foo)\n\n&figure(, hello &caption(test)))", "");
    }

    @Test(expected = CommandException.class)
    public void figureNoContent() throws IOException {
        this.test("&article(See &fig(Foo)\n\n&figure(Foo))", "<main class=\"autograph\"><article " + "class=\"ag" +
            "-article\"><p>See <a " + "alt=\"Figure 1\" class=\"Method...\" href=\"#figure-114a7\">Fig. " +
            "1</a></p><figure class=\"ag-figure\" " + "id=\"figure-114a7\"><p>hello <figcaption " +
            "class=\"ag-caption\"><a>Figure " + "1</a>test</figcaption></p></figure></article></main>");
    }

    @Test
    public void color() throws IOException {
        this.test("&color(#ff0000, Hello there!)", "<main class=\"autograph\"><span style=\"color: #ff0000;\"><p>Hello there!</p></span></main>");
        this.test("&color(#ff0000, &b(bold text))", "<main class=\"autograph\"><b style=\"color: #ff0000;\">bold text</b></main>");
    }

    @Test(expected = CommandException.class)
    public void colorNoContent() throws IOException {
        this.test("&color(#ff0000)", "");
    }

    @Test
    public void mark() throws IOException {
        this.test("&mark(text)", "<main class=\"autograph\"><mark>text</mark></main>");
        this.test("foo &mark(text) bar", "<main class=\"autograph\"><p>foo <mark>text</mark> bar</p></main>");
    }

    @Test
    public void embed() throws IOException {
        this.test("&embed(600x340, foo.png)", "<main class=\"autograph\"><embed width=\"600\" type=\"image/png\" src=\"foo.png\" height=\"340\" /></main>");
        this.test("&embed(800x700, myFile.txt)", "<main class=\"autograph\"><embed width=\"800\" type=\"text/plain\" src=\"myFile.txt\" height=\"700\" /></main>");
    }

}