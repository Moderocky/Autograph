package org.valross.autograph.parser;

import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.document.MultiNode;
import org.valross.autograph.document.TextNode;

public class AutographParserTest extends ParserTest {

    @Test
    public void parse() throws Throwable {
        final Document document = this.makeParser("hello").parse();
        assert document != null;
        assert document.nodes().length == 1;
        assert document.nodes()[0] instanceof MultiNode multi
            && multi.nodes().length == 1
            && multi.nodes()[0] instanceof TextNode node
            && node.value().equals("hello") : document;
    }

    @Override
    protected AutographParser makeParser(String text) {
        return new AutographParser(Parser.input(text));
    }

}