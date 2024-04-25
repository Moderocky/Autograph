package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.util.Arrays;

public final class Commands {

    public static final CommandDefinition ARTICLE = blockCommand(StandardElements.ARTICLE);
    public static final CommandDefinition ASIDE = blockCommand(StandardElements.ASIDE);
    public static final CommandDefinition BOLD = inlineCommand(StandardElements.B),
        ITALIC = inlineCommand(StandardElements.I),
        UNDERLINE = inlineCommand(StandardElements.U),
        STRIKETHROUGH = inlineCommand(StandardElements.S);
    public static final CommandDefinition TABLE = blockCommand(StandardElements.TABLE),
        ROW = inlineCommand("row", StandardElements.TR),
        CELL = inlineCommand("cell", StandardElements.TD);
    public static final CommandDefinition LINK = new CommandDefinition("link", LinkCommand::new);
    public static final CommandDefinition HTML = new CommandDefinition("html", HTMLCommand::new);
    public static final CommandDefinition SOFT_TABLE = new CommandDefinition("softTable", SoftTableCommand::new);
    public static final CommandDefinition CODE = new CommandDefinition("code", CodeCommand::new);
    public static final CommandDefinition CODE_BLOCK = new CommandDefinition("codeBlock", CodeBlockCommand::new);

    private static final CommandDefinition[] commands = new CommandDefinition[] {
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH,
        ARTICLE,
        ASIDE,
        LINK,
        HTML,
        TABLE,
        ROW,
        CELL,
        SOFT_TABLE,
        CODE,
        CODE_BLOCK
    };

    public static CommandDefinition[] standard() {
        return Arrays.copyOf(commands, commands.length);
    }

    public static CommandDefinition[] formatting() {
        return new CommandDefinition[] {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH};
    }

    public static CommandDefinition[] tables() {
        return new CommandDefinition[] {SOFT_TABLE, TABLE, ROW, CELL};
    }

    private static CommandDefinition inlineCommand(HTMElement tag) {
        return inlineCommand(tag.getTag(), tag);
    }

    private static CommandDefinition inlineCommand(String name, HTMElement tag) {
        //<editor-fold desc="Fake command parser using the tag" defaultstate="collapsed">
        class StaticCommandParser extends HTMCommandParser {

            public StaticCommandParser(Source source, CommandDefinition... commands) {
                super(source, commands);
            }

            @Override
            public HTMNode parse() throws IOException {
                final Node[] nodes = this.consume(), raw = new Node[nodes.length];
                for (int i = 0; i < nodes.length; i++) {
                    if (nodes[i] instanceof ParagraphNode node) raw[i] = node.overSimplify();
                    else raw[i] = nodes[i];
                }
                return new HTMNode(tag, raw);
            }

        }
        //</editor-fold>
        return new CommandDefinition(name, StaticCommandParser::new);
    }

    private static CommandDefinition blockCommand(HTMElement tag) {
        //<editor-fold desc="Fake command parser using the tag" defaultstate="collapsed">
        class StaticCommandParser extends HTMCommandParser {

            public StaticCommandParser(Source source, CommandDefinition... commands) {
                super(source, commands);
            }

            @Override
            public HTMNode parse() throws IOException {
                return new HTMNode(tag, this.consume());
            }

        }
        //</editor-fold>
        return new CommandDefinition(tag.getTag(), StaticCommandParser::new);
    }

}
