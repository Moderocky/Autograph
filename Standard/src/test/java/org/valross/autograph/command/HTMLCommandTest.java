package org.valross.autograph.command;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class HTMLCommandTest extends DOMTest {

    @Test
    public void noCommand() throws IOException {
        final String content = "<b>hello there!</b>", expected = "<body><p>&lt;b&gt;hello there!&lt;/b&gt;</p></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

    @Test
    public void simple() throws IOException {
        final String content = "&html(<b>hello there!</b>)", expected = "<body><b>hello there!</b></body>";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document);
    }

}