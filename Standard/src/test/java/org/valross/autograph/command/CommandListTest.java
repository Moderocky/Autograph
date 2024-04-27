package org.valross.autograph.command;

import org.junit.BeforeClass;
import org.junit.Test;
import org.valross.autograph.document.Document;
import org.valross.autograph.parser.AutographParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CommandListTest extends DOMTest {

    private static final Set<CommandDefinition> commands = new HashSet<>();

    @BeforeClass
    public static void setUpClass() throws Exception {
        for (Field field : Commands.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!Modifier.isPublic(field.getModifiers())) continue;
            if (field.getType() != CommandDefinition.class) continue;
            field.setAccessible(true);
            final boolean added = commands.add((CommandDefinition) field.get(null));
            assert added : "Duplicate definition of " + field.getName();
        }
    }

    @Test
    public void checkStandardSet() {
        final Set<CommandDefinition> commands = new HashSet<>(CommandListTest.commands);
        for (CommandDefinition value : Commands.standard().commands()) {
            assert commands.contains(value) : "Unknown " + value;
            final boolean removed = commands.remove(value);
            assert removed;
        }
        commands.removeIf(command -> command.parent() != null);
        assert commands.isEmpty() : "Missing " + commands;
    }

    @Test
    public void checkTestsPresent() {
        final Set<CommandDefinition> commands = new HashSet<>(CommandListTest.commands);
        final List<String> names = new LinkedList<>();
        for (CommandDefinition command : commands) {
            try {
                CommandsTest.class.getDeclaredMethod(command.command());
            } catch (NoSuchMethodException e) {
                names.add(command.command());
            }
        }
        if (!names.isEmpty()) {
            for (String name : names) {
                System.err.println("@Test\npublic void " + name + "() throws IOException {\n\n}");
            }
            assert false : "Missing tests.";
        }
    }

    @Test
    public void document() throws IOException {
        final String content = """
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
            	<article class="ag-article">
            		<p>this command is on its own, so it gets its own box!</p>
            		<p>and it&apos;s in like a lil space idk</p>
            	</article>
            </body>""";
        final Document document = this.parse(new AutographParser(content, Commands.standard()));
        this.test(expected, document, true);
    }

}