package org.valross.autograph.parser;

import org.valross.autograph.document.Node;

public interface MultiNodeParser {

    void addNode(Node node);

    Node[] nodes();

}
