package org.valross.autograph.document;

import org.junit.Test;
import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.command.CommandSet;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;

public class DocumentTest extends DOMTest {

    @Test
    public void simple() throws IOException {
        final String content = "hello there", expected = "<main class=\"autograph\"><p>hello there</p></main>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of()));
        this.test(expected, document);
    }

    @Test
    public void twoTextElements() throws IOException {
        final String content = "hello there\n\ngeneral kenobi", expected = "<main class=\"autograph\"><p>hello there</p><p>general " +
            "kenobi</p></main>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of()));
        this.test(expected, document);
    }

    @Test
    public void commandOnly() throws IOException {
        final CommandDefinition hello = CommandDefinition.of("hello", () -> new TextNode("hello"));
        final String content = "&hello()", expected = "<main class=\"autograph\">hello</main>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of(hello)));
        this.test(expected, document);
    }

    @Test
    public void commandOnlyHead() throws IOException {
        final CommandDefinition hello = CommandDefinition.of("hello", (text) -> new TextNode("hello " + text));
        final String content = "&hello(there)", expected = "<main class=\"autograph\">hello there</main>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of(hello)));
        this.test(expected, document);
    }

    @Test
    public void commandSimpleArguments() throws IOException {
        final CommandDefinition hello = CommandDefinition.ofArguments("hello", strings -> new TextNode(String.join(
            "+", strings)));
        final String content = "&hello(there, world, foo)", expected = "<main class=\"autograph\">there+world+foo</main>";
        final Document document = this.parse(new AutographParser(content, CommandSet.of(hello)));
        this.test(expected, document);
    }

}