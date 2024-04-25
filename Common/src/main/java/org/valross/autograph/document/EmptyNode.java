package org.valross.autograph.document;

import org.valross.constantine.Constant;

import java.io.OutputStream;
import java.nio.charset.Charset;

public class EmptyNode implements Node, Constant.UnitConstant {

    @Override
    public void write(OutputStream outputStream, Charset charset) {

    }

}
