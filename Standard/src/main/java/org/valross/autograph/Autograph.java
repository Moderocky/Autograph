package org.valross.autograph;

import mx.kenzie.hypertext.PageWriter;
import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.command.Commands;
import org.valross.autograph.document.Document;
import org.valross.autograph.error.AutographException;
import org.valross.autograph.parser.AutographParser;

import java.io.*;

public class Autograph {

    public static void main(String... args) throws IOException {
        final Document document;
        try (AutographParser parser = new AutographParser(System.in, Commands.standard())) {
            document = parser.parse();
        }
        try (PageWriter writer = new PageWriter(System.out)) {
            writer.format("\t").write(document);
        }
    }

    public static Document parse(String source, CommandDefinition... commands) {
        if (source == null) return new Document();
        try (AutographParser parser = new AutographParser(source, commands)) {
            return parser.parse();
        } catch (IOException ex) {
            throw new AutographException(ex);
        }
    }

    public static Document parse(InputStream source, CommandDefinition... commands) {
        if (source == null) return new Document();
        else return parse(new BufferedReader(new InputStreamReader(source)), commands);
    }

    public static Document parse(Reader source, CommandDefinition... commands) {
        if (source == null) return new Document();
        try (AutographParser parser = new AutographParser(source, commands)) {
            return parser.parse();
        } catch (IOException ex) {
            throw new AutographException(ex);
        }
    }

}
