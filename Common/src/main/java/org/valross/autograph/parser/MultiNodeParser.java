package org.valross.autograph.parser;

import org.valross.autograph.document.Node;

interface MultiNodeParser {

    void addNode(Node node);

    Node[] nodes();

}
