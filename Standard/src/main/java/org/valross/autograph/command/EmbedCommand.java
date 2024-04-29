package org.valross.autograph.command;

import mx.kenzie.hypertext.element.EmbeddedElement;
import mx.kenzie.hypertext.element.StandardElements;
import org.valross.autograph.document.model.HTMNode;
import org.valross.autograph.error.CommandException;
import org.valross.autograph.parser.Source;
import org.valross.autograph.util.ContentType;

import java.io.IOException;

public class EmbedCommand extends HTMCommandParser {

    public EmbedCommand(Source source, CommandSet commands) {
        super(source, commands);
    }

    @Override
    public HTMNode parse() throws IOException {
        final String definition = this.nextArgument();
        if (definition.isBlank()) return new HTMNode(StandardElements.EMBED);
        final int width, height;
        try {
            final String[] split = definition.split("(x|by)");
            if (split.length != 2) throw new IllegalArgumentException();
            width = Integer.parseInt(split[0].trim());
            height = Integer.parseInt(split[1].trim());
        } catch (Exception ex) {
            throw new CommandException("Embed must start with 'X by Y' size definition:", this);
        }
        if (width <= 0 || height <= 0) throw new CommandException("Embed dimensions must be positive", this);
        if (this.next() != ',') throw new CommandException("&embed dimensions should be followed by content URL", this);
        final String source = this.readLine().strip();
        final EmbeddedElement element = StandardElements.EMBED.src(source);
        final String extension = source.substring(source.lastIndexOf('.') + 1);
        final String mimeType = ContentType.getMimeType(extension);
        element.set("type", mimeType);
        return new HTMNode(element.dimensions(width, height));
    }

}
