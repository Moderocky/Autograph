package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class SoftTableCommandTest extends DOMTest {

    @Test
    public void twoColumns() throws IOException {
        final String content = "&softTable(2 by 1, hello, there)", expected = "<main class=\"autograph\"><table><tr" +
            "><td><p>hello</p></td><td><p>there</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void twoRows() throws IOException {
        final String content = "&softTable(1 by 2, hello, there)", expected = "<main class=\"autograph\"><table><tr" +
            "><td><p>hello</p></td></tr><tr><td><p>there</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void twoByTwo() throws IOException {
        final String content = "&softTable(2 by 2, hello, there, general, kenobi)", expected = "<main " +
            "class=\"autograph\"><table><tr><td><p>hello</p></td><td><p>there</p></td></tr><tr><td><p>general</p></td" +
            "><td><p>kenobi</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void notEnoughData() throws IOException {
        final String content = "&softTable(2 by 2, hello)", expected = "<main class=\"autograph\"><table><tr><td><p" +
            ">hello</p></td><td></td></tr><tr><td></td><td></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void tooMuchData() throws IOException {
        final String content = "&softTable(1 by 1, hello, there)", expected = "<main class=\"autograph\"><table><tr" +
            "><td><p>hello</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void stuffAroundTable() throws IOException {
        final String content = "hello &softTable(2 by 1, hello, there) there", expected = "<main class=\"autograph" +
            "\"><p>hello <table><tr><td><p>hello</p></td><td><p>there</p></td></tr></table> there</p></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void stuffInTable() throws IOException {
        final String content = "&softTable(2 by 1, &b(hello), there)", expected = "<main class=\"autograph\"><table" +
            "><tr><td><b>hello</b></td><td><p>there</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void moreStuffInTable() throws IOException {
        final String content = "&softTable(2 by 1, &b(hello) &i(there), general kenobi)", expected = "<main " +
            "class=\"autograph\"><table><tr><td><p><b>hello</b> <i>there</i></p></td><td><p>general " +
            "kenobi</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void bigTable() throws IOException {
        final String content = """
            &softTable(2 by 3,
              cell 1, cell 2,
              cell 3, cell 4,
              cell 5, cell 6
            )""", expected = "<main class=\"autograph\"><table><tr><td><p>cell 1</p></td><td><p>cell " +
            "2</p></td></tr><tr><td><p>cell 3</p></td><td><p>cell 4</p></td></tr><tr><td><p>cell " +
            "5</p></td><td><p>cell 6</p></td></tr></table></main>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

}