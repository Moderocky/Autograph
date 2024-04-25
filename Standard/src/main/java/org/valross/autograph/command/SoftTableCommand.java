package org.valross.autograph.command;

import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.Body;
import org.valross.autograph.document.Node;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.parser.command.ArgumentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SoftTableCommand extends HTMCommandParser {

    public SoftTableCommand(Source source, CommandDefinition... commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final String definition = this.nextArgument();
        if (definition.isBlank()) return new HTMNode(StandardElements.TABLE);
        final int columns, rows;
        try {
            final String[] split = definition.split("(x|by)");
            if (split.length != 2) {}
            assert split.length == 2;
            columns = Integer.parseInt(split[0].trim());
            rows = Integer.parseInt(split[1].trim());
        } catch (Exception ex) {
            throw new CommandException("Soft table must start with 'X by Y' definition:", this);
        }
        final LinkedList<Body> entries = new LinkedList<>();
        do {
            switch ((int) this.next()) {
                case ')', ',': break;
                default: this.stowChar();
            }
            try (TableCellParser parser = this.delegate(TableCellParser::new)) {
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

    static class TableCellParser extends ArgumentParser<Body> {

        public TableCellParser(Source source, CommandDefinition... commands) {
            super(source, commands);
        }

        @Override
        public Body parse() throws IOException {
            this.consumeWhitespace();
            final List<Node> nodes = new ArrayList<>();
            do try (InnerTextParser parser = this.delegate(InnerTextParser::new)) {
                nodes.add(parser.parse());
            } while (this.hasNext());
            return new Body(nodes.toArray(new Node[0]));
        }

    }

}
