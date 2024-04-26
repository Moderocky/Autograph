package org.valross.autograph.command;

import mx.kenzie.hypertext.PageWriter;
import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;
import java.util.UUID;

public class FootnoteCommandTest extends DOMTest {

    @Test
    public void staticLinks() throws IOException {
        for (int i = 0; i < 40; i++) {
            final String text = UUID.randomUUID().toString();
            final String expected = FootnoteCommand.dummyAnchor(text);
            final String source = "&article(foo&footnote(" + text + ")\n\n&footer())";
            final Document document = this.parse(new AutographParser(source, Commands.standard()));
            final StringBuilder builder = new StringBuilder();
            try (PageWriter writer = new PageWriter(builder)) {
                writer.write(document);
            }
            assert builder.toString().contains(expected) : builder.toString();
        }
    }

}