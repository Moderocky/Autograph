package org.valross.autograph.command;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.Writable;
import org.junit.BeforeClass;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.Parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class DOMTest {

    static File folder = new File("target/generated-pages/");

    private static String from() {
        final Exception exception = new Exception();
        exception.fillInStackTrace();
        final StackTraceElement[] trace = exception.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            final StackTraceElement element = trace[i];
            if (element.getClassName().endsWith(".DOMTest")) continue;
            return element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1)
                + "/" + element.getMethodName();
        }
        return "Unknown/" + System.currentTimeMillis();
    }

    protected void test(String expected, Writable actual) {
        this.test(expected, actual, false);
    }

    protected void test(String expected, Writable actual, boolean pretty) {
        final String source = from();
        final StringBuilder builder = new StringBuilder();
        try (final PageWriter writer = new PageWriter(builder)) {
            if (pretty) writer.format("\t");
            writer.write(actual);
        }
        final File file = new File(folder, source + ".html");
        file.getParentFile().mkdirs();
        try (final OutputStream stream = new FileOutputStream(file);
             final PageWriter writer = new PageWriter(stream).format("\t")) {
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
