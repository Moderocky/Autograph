package org.valross.autograph;

import org.valross.autograph.command.CommandDefinition;
import org.valross.autograph.document.Document;
import org.valross.autograph.error.AutographException;
import org.valross.autograph.parser.AutographParser;

import java.io.*;

public class Autograph {

    public static void main(String... args) {

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
