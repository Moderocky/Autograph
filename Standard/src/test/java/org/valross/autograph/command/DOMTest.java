package org.valross.autograph.command;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.Writable;
import org.valross.autograph.document.Document;
import org.valross.autograph.document.Node;
import org.valross.autograph.parser.AutographParser;
import org.valross.autograph.parser.Parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DOMTest {

    static File folder = new File("target/generated-pages/");
    static Map<String, AtomicInteger> testCounts = new HashMap<>();

    private static String from() {
        final Exception exception = new Exception();
        exception.fillInStackTrace();
        final StackTraceElement[] trace = exception.getStackTrace();
        String name = "Unknown/" + System.currentTimeMillis();
        for (final StackTraceElement element : trace) {
            if (element.getClassName().endsWith(".DOMTest")) continue;
            name = element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1)
                + "/" + element.getMethodName();
            break;
        }
        final AtomicInteger count = testCounts.computeIfAbsent(name, _ -> new AtomicInteger());
        final int number = count.getAndIncrement();
        if (number > 0) name = name + number;
        return name;
    }

    protected void test(String content, String expected) throws IOException {
        this.test(content, expected, false);
    }

    protected void test(String content, String expected, boolean pretty) throws IOException {
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document, pretty);
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
        assert expected.contentEquals(builder) : "\nFailure for: " + actual + "\nExpected:\n`" + expected + "`\n" +
            "\nFound:\n`" + builder + "`";
    }

    protected <Result extends Node> Result parse(Parser<Result> parser) throws IOException {
        try (parser) {
            return parser.parse();
        }
    }

}
