Autograph
=====

An extensible markup language with a simple design.

# Common Design

## Elements

### Text

Text is stored in text nodes.

Nodes are separated by two or more (consecutive) line breaks.

```
This is text.
This is more text in the same node.

This is text in a different node.
```

### Commands

Commands are stored in command nodes.

Commands begin with an ampersand `&`, followed by the command's name.
The command name is followed by the command header: opened by `(` and closed by `)`.

```
&command label here(this is the command header)
```

All commands need a header, even if they take no input.

The processing of the command's header depends on the command used.
Some commands may take one or more arguments, separated by a comma `,`.

```
&my cool command(an argument, another argument)
```

Commands have complete authority over how to parse anything inside their `()` header.

The only rule is that they must terminate at a closing `)` bracket.

# Standard Command Set

##