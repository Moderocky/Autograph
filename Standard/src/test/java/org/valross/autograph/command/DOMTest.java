package org.valross.autograph.command;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.Writable;
import org.junit.BeforeClass;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.Parser;

import java.io.*;

public abstract class DOMTest {

    static File folder = new File("target/generated-pages/");
    @BeforeClass
    public static void setUpClass() throws IOException {
        folder.mkdirs();
    }

    private static String from() {
        final Exception exception = new Exception();
        exception.fillInStackTrace();
        final StackTraceElement element = exception.getStackTrace()[2];
        return element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1)
            + "/" + element.getMethodName();
    }

    protected void test(String expected, Writable actual) {
        final String source = from();
        final StringBuilder builder = new StringBuilder();
        try (final PageWriter writer = new PageWriter(builder)) {
            writer.write(actual);
        }
        final File file = new File(folder, source + ".html");
        file.getParentFile().mkdirs();
        try (final OutputStream stream = new FileOutputStream(file);
             final PageWriter writer = new PageWriter(stream)) {
            writer.write(actual);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assert expected.contentEquals(builder) : "expected vs got:\n" + expected + System.lineSeparator() + builder;
    }

    protected <Result extends Node> Result parse(Parser<Result> parser) throws IOException {
        try (parser) {
            return parser.parse();
        }
    }

}
