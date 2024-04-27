package org.valross.autograph;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.Writable;
import org.junit.BeforeClass;
import org.junit.Test;
import org.valross.autograph.command.Commands;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static mx.kenzie.hypertext.element.StandardElements.*;

public class TestPageGenerator {

    private static File source, target;

    @BeforeClass
    public static void setUp() throws Exception {
        source = new File("../Standard/target/generated-pages/");
        target = new File("target/generated-pages/");
        target.mkdirs();
    }

    @Test
    public void copyStylesheet() throws IOException {
        final File file = new File("src/main/css/standard.css");
        Files.copy(file.toPath(), new File(target, "stylesheet.css").toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void generate() {
        final File folder = new File("src/test/resources/");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.getName().endsWith(".autograph")) continue;
            this.makeFile(file, target);
        }
    }

    private void makeFile(File source, File folder) {
        final String name = source.getName();
        final File file = new File(folder, name.substring(0, name.lastIndexOf('.')) + ".html");
        try (final InputStream input = new FileInputStream(source);
             final OutputStream output = new FileOutputStream(file);
             final AutographParser parser = new AutographParser(input, Commands.standard());
             final PageWriter writer = new PageWriter(output).format("    ")) {
            final Document document = parser.parse();
            writer.write(DOCTYPE_HTML, HEAD.child(
                TITLE.write(name.substring(0, name.lastIndexOf('.'))),
                LINK.rel("stylesheet").href("./stylesheet.css")), BODY.child(document));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void copyTestPages() {
        if (!source.isDirectory()) return;
        for (File file : Objects.requireNonNull(source.listFiles())) {
            this.copyDirectory(file);
        }
    }

    private void copyDirectory(File source) {
        if (!source.isDirectory()) return;
        final File folder = new File(target, source.getName());
        folder.mkdirs();
        for (File file : Objects.requireNonNull(source.listFiles())) {
            if (file.isDirectory()) continue;
            if (!file.getName().endsWith(".html")) continue;
            this.makeTestFile(file, folder);
        }
    }

    private void makeTestFile(File source, File folder) {
        final String name = source.getName();
        final File file = new File(folder, name);
        try (final InputStream input = new FileInputStream(source);
             final OutputStream output = new FileOutputStream(file);
             final PageWriter writer = new PageWriter(output).format("    ")) {
            final Writable writable = (stream, _) -> input.transferTo(stream);
            writer.write(DOCTYPE_HTML, HEAD.child(
                TITLE.write(name.substring(0, name.lastIndexOf('.'))),
                LINK.rel("stylesheet").href("../stylesheet.css")), BODY.child(writable));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
