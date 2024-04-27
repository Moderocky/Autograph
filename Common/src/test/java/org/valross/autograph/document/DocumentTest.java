package org.valross.autograph.document;

import org.junit.Test;
import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class DocumentTest extends DOMTest {

    @Test
    public void simple() throws IOException {
        final String content = "hello there", expected = "<body><p>hello there</p></body>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of()));
        this.test(expected, document);
    }

    @Test
    public void twoTextElements() throws IOException {
        final String content = "hello there\n\ngeneral kenobi", expected = "<body><p>hello there</p><p>general " +
            "kenobi</p></body>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of()));
        this.test(expected, document);
    }

    @Test
    public void commandOnly() throws IOException {
        final CommandDefinition hello = CommandDefinition.of("hello", () -> new TextNode("hello"));
        final String content = "&hello()", expected = "<body>hello</body>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of(hello)));
        this.test(expected, document);
    }

    @Test
    public void commandOnlyHead() throws IOException {
        final CommandDefinition hello = CommandDefinition.of("hello", (text) -> new TextNode("hello " + text));
        final String content = "&hello(there)", expected = "<body>hello there</body>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of(hello)));
        this.test(expected, document);
    }

    @Test
    public void commandSimpleArguments() throws IOException {
        final CommandDefinition hello = CommandDefinition.ofArguments("hello", strings -> new TextNode(String.join(
            "+", strings)));
        final String content = "&hello(there, world, foo)", expected = "<body>there+world+foo</body>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of(hello)));
        this.test(expected, document);
    }

}