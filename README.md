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

## Text Formatting

These commands can contain text (and other commands).

| Command   | Description                            | HTML Tag |
|-----------|----------------------------------------|----------|
| `&i()`    | Italic                                 | `<i>`    |
| `&b()`    | Bold                                   | `<b>`    |
| `&u()`    | Underline                              | `<u>`    |
| `&s()`    | Strikethrough                          | `<s>`    |
| `&q()`    | Quotation (inline)                     | `<q>`    |
| `&code()` | Code well: permits (balanced) brackets | `<code>` |

## Block Elements

These commands are designed to take in multi-line content sections (and other commands).

| Command        | Description                                  | HTML Tag         |
|----------------|----------------------------------------------|------------------|
| `&aside()`     | An 'aside' block                             | `<aside>`        |
| `&article()`   | An article section                           | `<article>`      |
| `&quote()`     | A quote block                                | `<blockquote>`   |
| `&codeBlock()` | Code block well: permits (balanced) brackets | `<pre>` `<code>` |

Note: the `&code` and `&codeBlock` commands permit unescaped `(` parentheses `)`,
**as long as** they are balanced.

## Table Elements

| Command    | Description            | HTML Tag  |
|------------|------------------------|-----------|
| `&table()` | A table.               | `<table>` |
| `&row()`   | A row in a table.      | `<tr>`    |
| `&cell()`  | A cell in a table row. | `<td>`    |

## Special Commands

These commands have special argument handling or parsing rules.

### `&link`

With a single argument, the `&link(https://url)` command acts as an anchor `<a>` tag with an `href`.
The text inside the tag is the link itself.

If a second argument is provided, the `&link(https://url, content here)` command acts as an anchor `<a>`
tag with an `href`.
The content inside the tag is the second argument of the command.

### `&html`

The `&html(<p>content</p>)` command inserts unescaped HTML characters,
allowing raw HTML to be written (providing the document is being compiled as HTML).

If this document is not being written as HTML the command will have no difference from regular text.

### `&softTable`

The 'soft table' command is for generating less rigid tables based on the content provided.

The first argument is the table dimensions, in the format `columns by rows`, e.g. `2 by 3`.

Every subsequent argument is inserted into the next available table cell. Unfilled cells are left empty.
Arguments exceeding the table's cell count are discarded.

```
&softTable(2 by 3,
  cell 1, cell 2,
  cell 3, cell 4,
  cell 5, cell 6
)
```