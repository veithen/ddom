/*
 * Copyright 2009-2011 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.ddom.frontend.axiom.support;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.pivot.XmlPivot;

public class PullTextExtractor extends XmlPivot {
    private int depth;
    private String text;
    private int pos;
    private boolean complete;
    
    public int read(char[] cbuf, int off, int len) throws StreamException {
        if (complete) {
            return -1;
        } else {
            if (text == null) {
                nextEvent();
                if (complete) {
                    return -1;
                }
            }
            int textLen = text.length();
            int c = Math.min(len, textLen-pos);
            text.getChars(pos, pos+c, cbuf, off);
            pos += c;
            if (pos == textLen) {
                text = null;
                pos = 0;
            }
            return c;
        }
    }

    @Override
    protected boolean startEntity(boolean fragment, String inputEncoding) {
        return true;
    }

    @Override
    protected boolean processXmlDeclaration(String version, String encoding, Boolean standalone) {
        return true;
    }

    @Override
    protected boolean startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        return true;
    }

    @Override
    protected boolean endDocumentTypeDeclaration() {
        return true;
    }

    @Override
    protected boolean startElement(String tagName) {
        depth++;
        return true;
    }

    @Override
    protected boolean startElement(String namespaceURI, String localName, String prefix) {
        depth++;
        return true;
    }

    @Override
    protected boolean endElement() {
        depth--;
        return true;
    }

    @Override
    protected boolean startAttribute(String name, String type) {
        depth++;
        return true;
    }

    @Override
    protected boolean startAttribute(String namespaceURI, String localName, String prefix, String type) {
        depth++;
        return true;
    }

    @Override
    protected boolean startNamespaceDeclaration(String prefix) {
        depth++;
        return true;
    }

    @Override
    protected boolean endAttribute() {
        depth--;
        return true;
    }

    @Override
    protected boolean attributesCompleted() {
        return true;
    }

    @Override
    protected boolean processCharacterData(String data, boolean ignorable) {
        if (depth == 1) {
            text = data;
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected boolean startProcessingInstruction(String target) {
        depth++;
        return true;
    }

    @Override
    protected boolean endProcessingInstruction() {
        depth--;
        return true;
    }

    @Override
    protected boolean startComment() {
        depth++;
        return true;
    }

    @Override
    protected boolean endComment() {
        depth--;
        return true;
    }

    @Override
    protected boolean startCDATASection() {
        return true;
    }

    @Override
    protected boolean endCDATASection() {
        return true;
    }

    @Override
    protected boolean processEntityReference(String name) {
        return true;
    }

    @Override
    protected void completed() {
        complete = true;
    }
}
