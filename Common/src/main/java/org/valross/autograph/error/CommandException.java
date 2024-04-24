package org.valross.autograph.error;

import org.valross.autograph.parser.Source;

public class CommandException extends AutographException {

    private final Source source;

    public CommandException(Source source) {
        super("Position " + source.caret());
        this.source = source;
    }

    public CommandException(String message, Source source) {
        super(message + " position " + source.caret());
        this.source = source;
    }

    public CommandException(String message, Throwable cause, Source source) {
        super(message + " position " + source.caret(), cause);
        this.source = source;
    }

    public CommandException(Throwable cause, Source source) {
        super("Position " + source.caret(), cause);
        this.source = source;
    }

    public Source source() {
        return source;
    }

}
