package org.valross.autograph.command;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.ContentParser;

import java.io.IOException;
import java.util.LinkedList;

public class SoftTableCommand extends HTMCommandParser {

    public SoftTableCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final String definition = this.nextArgument();
        if (definition.isBlank()) return new HTMNode(StandardElements.TABLE);
        final int columns, rows;
        try {
            final String[] split = definition.split("(x|by)");
            if (split.length != 2) throw new IllegalArgumentException();
            columns = Integer.parseInt(split[0].trim());
            rows = Integer.parseInt(split[1].trim());
        } catch (Exception ex) {
            throw new CommandException("Soft table must start with 'X by Y' size definition:", this);
        }
        final LinkedList<Body> entries = new LinkedList<>();
        do {
            switch ((int) this.next()) {
                case ')', ',':
                    break;
                default:
                    this.stowChar();
            }
            try (ContentParser parser = this.delegate(ContentParser::new)) {
                entries.addLast(parser.parse());
            }
        } while (this.hasNext());
        final HTMNode[] tableRows = new HTMNode[rows];
        for (int x = 0; x < rows; x++) {
            final HTMNode[] cells = new HTMNode[columns];
            for (int y = 0; y < columns; y++)
                cells[y] = new HTMNode(StandardElements.TD, this.next(entries));
            tableRows[x] = new HTMNode(StandardElements.TR, cells);
        }
        return new HTMNode(StandardElements.TABLE, tableRows);
    }

    private Node[] next(LinkedList<Body> entries) {
        if (entries.isEmpty()) return new Node[0];
        return entries.pop().nodes();
    }

}
