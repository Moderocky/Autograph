package org.valross.autograph.command;

import mx.kenzie.hypertext.PageWriter;
import mx.kenzie.hypertext.internal.StringBuilderOutputStream;
import org.junit.Test;
import org.valross.autograph.Autograph;
import org.valross.autograph.document.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class CommandsTest {

    @Test
    public void standard() throws Exception {
        final Set<CommandDefinition> commands = new HashSet<>();
        for (Field field : Commands.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType() != CommandDefinition.class) continue;
            field.setAccessible(true);
            final boolean added = commands.add((CommandDefinition) field.get(null));
            assert added : "Duplicate definition of " + field.getName();
        }
        for (CommandDefinition value : Commands.standard()) {
            assert commands.contains(value) : "Unknown " + value;
            final boolean removed = commands.remove(value);
            assert removed;
        }
        assert commands.isEmpty() : "Missing " + commands;
    }

    @Test
    public void document() {
        final String source = """
            hello there! this is a &b(cool new document).

            this is a new paragraph!
            yay!

            and &i(this) is in italics!

            &article(
            this command is on its own, so it gets its own box!

            and it's in like a lil space idk
            )
            """;
        final String expected = """
            <body>
            	<p>hello there! this is a <b>cool new document</b>.</p>
            	<p>
            		this is a new paragraph!
            		yay!
            	</p>
            	<p>and <i>this</i> is in italics!</p>
            	<article>
            		<p>this command is on its own, so it gets its own box!</p>
            		<p>and it&apos;s in like a lil space idk</p>
            	</article>
            </body>""";
        final Document document = Autograph.parse(source, Commands.standard());
        final StringBuilderOutputStream stream = new StringBuilderOutputStream();
        new PageWriter(stream).format("\t").write(document);
        assert stream.toString().equals(expected) : stream;
    }

}