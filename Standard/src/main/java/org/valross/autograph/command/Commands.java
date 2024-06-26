package org.valross.autograph.command;

import mx.kenzie.hypertext.element.HTMElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.command.article.*;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.document.model.ParagraphNode;
import org.valross.autograph.parser.Source;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Commands {

    public static final CommandDefinition COMMENT = CommandDefinition.of("comment", CommentCommand::new);
    public static final CommandDefinition ASIDE = blockCommand(StandardElements.ASIDE);
    public static final CommandDefinition BOLD = inlineCommand(StandardElements.B),
        ITALIC = inlineCommand(StandardElements.I),
        UNDERLINE = inlineCommand(StandardElements.U),
        STRIKETHROUGH = inlineCommand(StandardElements.S),
        QUOTE = inlineCommand(StandardElements.Q),
        EMPHASIS = inlineCommand(StandardElements.EM),
        STRONG = inlineCommand(StandardElements.STRONG),
        MARK = inlineCommand(StandardElements.MARK),
        COLOR = CommandDefinition.of("color", ColorCommand::new),
        LINE_BREAK = inlineCommand("break", StandardElements.BR),
        HORIZONTAL_RULE = inlineCommand("ruler", StandardElements.HR),
        PARAGRAPH = inlineCommand(StandardElements.P);
    public static final CommandDefinition TABLE = blockCommand(StandardElements.TABLE),
        ROW = inlineCommand("row", StandardElements.TR),
        CELL = inlineCommand("cell", StandardElements.TD);
    public static final CommandDefinition DETAILS = blockCommand(StandardElements.DETAILS),
        SUMMARY = blockCommand(StandardElements.SUMMARY);
    public static final CommandDefinition BLOCK_QUOTE = blockCommand("quote", StandardElements.BLOCKQUOTE);
    public static final CommandDefinition LINK = CommandDefinition.of("link", LinkCommand::new);
    public static final CommandDefinition EMBED = CommandDefinition.of("embed", EmbedCommand::new);
    public static final CommandDefinition HTML = CommandDefinition.of("html", HTMLCommand::new);
    public static final CommandDefinition SOFT_TABLE = CommandDefinition.of("softTable", SoftTableCommand::new);
    public static final CommandDefinition CODE = CommandDefinition.of("code", CodeCommand::new);
    public static final CommandDefinition CODE_BLOCK = CommandDefinition.of("codeBlock", CodeBlockCommand::new);
    public static final CommandDefinition RUBY = CommandDefinition.of("ruby", RubyCommand::new);
    public static final CommandDefinition ARTICLE = CommandDefinition.of("article", ArticleCommand::new),
        HEADER = blockCommand(StandardElements.HEADER),
        FOOTER = CommandDefinition.of("footer", FooterCommand::new),
        CITE = CommandDefinition.of("cite", CiteCommand::new),
        FOOTNOTE = ARTICLE.subcommand("footnote", FootnoteCommand::new),
        FIGURE = ARTICLE.subcommand("figure", FigureCommand::new),
        FIGURE_CAPTION = FIGURE.subcommand("caption", CaptionCommand::new),
        FIGURE_REFERENCE = ARTICLE.subcommand("fig", FigureReferenceCommand::new);

    private static final CommandDefinition[] commands = new CommandDefinition[] {
        COMMENT,
        PARAGRAPH,
        STRONG,
        LINE_BREAK,
        HORIZONTAL_RULE,
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH,
        RUBY,
        QUOTE,
        EMPHASIS,
        MARK,
        COLOR,
        DETAILS,
        SUMMARY,
        ASIDE,
        BLOCK_QUOTE,
        LINK,
        EMBED,
        HTML,
        TABLE,
        ROW,
        CELL,
        SOFT_TABLE,
        CODE,
        CODE_BLOCK,
        ARTICLE,
        HEADER,
        FOOTER,
        CITE
    };

    public static CommandSet standard() {
        return CommandSet.of(commands);
    }

    /**
     * The standard commands set, with additional wrapping commands added for every HTML element.
     * Generated commands will not override standard ones.
     *
     * @return A new array of generated commands
     */
    public static CommandSet standardHTML() {
        final Set<CommandDefinition> commands = new LinkedHashSet<>(List.of(Commands.commands));
        final List<HTMElement> list = new ArrayList<>();
        for (final Field field : StandardElements.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!HTMElement.class.isAssignableFrom(field.getType())) continue;
            try {
                list.add(((HTMElement) field.get(null)));
            } catch (IllegalAccessException _) {
            }
        }
        for (HTMElement element : list) {
            if (element.isSingle()) continue;
            final CommandDefinition definition;
            if (element.isSingle()) definition = singleCommand(element.getTag(), element);
            else if (element.isInline()) definition = inlineCommand(element);
            else definition = blockCommand(element);
            commands.add(definition);
        }
        return CommandSet.of(commands.toArray(new CommandDefinition[0]));
    }

    public static CommandSet formatting() {
        return CommandSet.of(BOLD, ITALIC, UNDERLINE, STRIKETHROUGH, QUOTE, EMPHASIS, MARK, COLOR);
    }

    public static CommandSet tables() {
        return CommandSet.of(SOFT_TABLE, TABLE, ROW, CELL);
    }

    private static CommandDefinition inlineCommand(HTMElement tag) {
        return inlineCommand(tag.getTag(), tag);
    }

    private static CommandDefinition inlineCommand(String name, HTMElement tag) {
        //<editor-fold desc="Fake command parser using the tag" defaultstate="collapsed">
        class StaticCommandParser extends HTMCommandParser {

            public StaticCommandParser(Source source, CommandSet commands) {
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
        return CommandDefinition.of(name, StaticCommandParser::new);
    }

    private static CommandDefinition blockCommand(HTMElement tag) {
        return blockCommand(tag.getTag(), tag);
    }

    private static CommandDefinition blockCommand(String name, HTMElement tag) {
        //<editor-fold desc="Fake command parser using the tag" defaultstate="collapsed">
        class StaticCommandParser extends HTMCommandParser {

            public StaticCommandParser(Source source, CommandSet commands) {
                super(source, commands);
            }

            @Override
            public HTMNode parse() throws IOException {
                return new HTMNode(tag, this.consume());
            }

        }
        //</editor-fold>
        return CommandDefinition.of(name, StaticCommandParser::new);
    }

    private static CommandDefinition singleCommand(String name, HTMElement tag) {
        //<editor-fold desc="Fake command parser using the tag" defaultstate="collapsed">
        class StaticCommandParser extends HTMCommandParser {

            public StaticCommandParser(Source source, CommandSet commands) {
                super(source, commands);
            }

            @Override
            public HTMNode parse() {
                //noinspection StatementWithEmptyBody
                while (this.next() != -1) ;
                return new HTMNode(tag);
            }

        }
        //</editor-fold>
        return CommandDefinition.of(name, StaticCommandParser::new);
    }

    private Commands() {
        throw new IllegalStateException("Utility class");
    }

}
