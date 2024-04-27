package org.valross.autograph.command;

import java.util.*;

public interface CommandSet extends Cloneable {

    static CommandSet of(CommandDefinition... commands) {
        final DefaultCommandSet set = new DefaultCommandSet();
        set.commands = commands;
        for (CommandDefinition command : commands) set.put(command.command(), command.parser());
        return set;
    }

    CommandDefinition.ParserSupplier get(String name);

    CommandDefinition[] commands();

    CommandSet with(CommandDefinition... commands);

    CommandSet clone();

}

final class DefaultCommandSet extends LinkedHashMap<String, CommandDefinition.ParserSupplier>
    implements CommandSet, Cloneable {

    CommandDefinition[] commands;

    DefaultCommandSet() {
    }

    public DefaultCommandSet(DefaultCommandSet set) {
        super(set);
        this.commands = Arrays.copyOf(set.commands, set.commands.length);
    }

    @Override
    public CommandDefinition.ParserSupplier get(String name) {
        return this.get((Object) name);
    }

    @Override
    public CommandDefinition[] commands() {
        return commands;
    }

    @Override
    public CommandSet with(CommandDefinition... commands) {
        final DefaultCommandSet set = new DefaultCommandSet(this);
        final Set<CommandDefinition> working = new LinkedHashSet<>();
        for (CommandDefinition command : commands) {
            working.add(command);
            set.put(command.command(), command.parser());
        }
        working.addAll(List.of(this.commands));
        set.commands = working.toArray(new CommandDefinition[0]);
        return set;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public CommandSet clone() {
        return new DefaultCommandSet(this);
    }

}