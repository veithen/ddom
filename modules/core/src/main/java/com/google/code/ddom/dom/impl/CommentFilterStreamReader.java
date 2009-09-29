package com.google.code.ddom.dom.impl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

public class CommentFilterStreamReader extends StreamReaderDelegate {
    public CommentFilterStreamReader(XMLStreamReader reader) {
        super(reader);
    }

    @Override
    public int next() throws XMLStreamException {
        int event;
        do {
            event = super.next();
        } while (event == COMMENT);
        return event;
    }
}
