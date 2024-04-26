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

### `&comment`

The `&comment(...)` command resolves to an empty node, not appearing in the final document.
Any notes inside will not be visible in a compiled document.

**Developer's note**: comments will produce an empty node in the document structure.

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

## Article Commands

Autograph is designed with journalistic article markup in mind.
As such, special support is given to commonly-used article elements (e.g. footnotes & citations).

### `&article`

This command wraps an entire 'article' node -- potentially the whole document.
Anything inside an article is wrapped in an `<article>` block during HTML compilation.

```
&article(
    Your content goes in here.
    
    This is arranged in paragraphs like a regular document.
)
```

Articles are special; some content behaves differently or is only permitted inside them (e.g. footers, footnotes).

### `&header`

A regular header block, designed to contain an article's header, abstract and other meta details.

```
&header(
    Your headnotes here!
)
```

### `&footer`

A footer block, designed to be placed at the end of an article.

```
&footer(
    Your endnotes here!
)
```

If the footer is used in an article, and that article contains **footnotes**, these will be inserted
at the bottom of their next footer.

```
&footer(
    Your endnotes here!
    
    &comment(Footnotes would be inserted here!)
)
```

If an article contains multiple footer blocks, footnotes will be inserted only into the next one.

### `&footnote`

The `&footnote(content here)` command is a powerful tool to inject references, notes and citations into a document.

Footnotes can **only** be used within an article.

```
&article(
    ...as stated in my previous publication.&footnote(&i(The Odyssey), Homer, 800BC)
    
    &footer(References:)
)
```

The footnote command produces an anchor link (e.g. `1`) in its location.
The actual content of the footnote is then added to the next `&footer` notes list.

#### Rendering Example

<article class="ag-article">
    <p>...as stated in my previous publication.<sup class="ag-reference"><a href="#footnote-ebb08fe6">1</a></sup></p>
    <footer class="ag-footer">
        <p>References:</p>
        <dl class="ag-footnote" id="footnote-ebb08fe6">
            <dt>1</dt>
            <dd>
                <p><i>The Odyssey</i>, Homer, 800BC</p>
            </dd>
        </dl>
    </footer>
</article>

---

Footnote numbering will restart whenever a footer consumes the available references.

Articles containing footnotes **must** contain a footer to display them in.

### `&cite`

The citation command is designed for inserting sourced quotes.

Its first argument is a reference/url (like `&link`) followed by its content.
The citation switches between inline and block-mode based on its content.

Citations (of either inline or block type) have the `.ag-citation` class applied in HTML compilation.

When compiled to HTML, in-text citations inject a little source `span` note at the end.

#### Inline (in-text)

```
The empire state building is &cite(https://bad-facts, 5 metres tall)!
```

#### Block (in-text)

```
&cite(https://my-rubbish-poem, 
    O empire state
    You are pretty tall
    I myself am not 
    Quite 5 metres
)
```

#### Footnote Citations

A footnote can be the target of a citation, either in inline or block mode.
This will add the footnote anchor to the citation rather than citing a URL.

As with regular footnotes, this can only be used within an `&article` that has a `&footer` to display the footnote.

```
&cite(&footnote(&i(Epic Poem), B. Shakespeare, 2006), 
    O empire state
    You are pretty tall
    I myself am not 
    Quite 5 metres
)
```

The above example will insert the numbered anchor, and then add the citation (_Epic Poem_, B. Shakespeare, 2006)
to the footnotes of the article.

Multiple footnotes may be used in a citation. This will add multiple anchors after the quote.

```
&cite(&footnote(foo)&footnote(bar), 
    O empire state
    You are pretty tall
    I myself am not 
    Quite 5 metres
)
```

**Note**: if something _other than_ a footnote is used in a footnote citation, this will inject an in-text citation as
well.
