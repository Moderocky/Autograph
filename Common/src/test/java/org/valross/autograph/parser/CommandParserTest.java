package org.valross.autograph.parser;

import org.junit.Test;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.CommandNode;
import org.valross.autograph.document.Document;
import org.valross.autograph.document.TextNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class CommandParserTest extends ParserTest {

    @Override
    protected Parser<?> makeParser(String text) {
        return new TestCommandParser(new ReaderSource(new BufferedReader(new StringReader(text))));
    }

    @Test
    public void simpleCommand() throws IOException {
        final Document document;
        try (AutographParser parser = new AutographParser(new StringReader("&test(foo)"), TestCommandParser.TEST)) {
            document = parser.parse();
        }
        assert document != null;
        assert document.nodes().length == 1;
        assert document.nodes()[0] instanceof Body body
            && body.nodes().length == 1
            && body.nodes()[0] instanceof CommandNode node
            && node.command().equals("test")
            && node.node() instanceof TextNode text
            && text.value().equals("hello foo");
    }

}