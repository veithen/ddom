/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.googlecode.ddom.xerces;

import com.google.code.ddom.stream.spi.Output;
import com.googlecode.ddom.xerces.xni.Augmentations;
import com.googlecode.ddom.xerces.xni.NamespaceContext;
import com.googlecode.ddom.xerces.xni.QName;
import com.googlecode.ddom.xerces.xni.XMLAttributes;
import com.googlecode.ddom.xerces.xni.XMLDocumentHandler;
import com.googlecode.ddom.xerces.xni.XMLLocator;
import com.googlecode.ddom.xerces.xni.XMLResourceIdentifier;
import com.googlecode.ddom.xerces.xni.XMLString;
import com.googlecode.ddom.xerces.xni.XNIException;
import com.googlecode.ddom.xerces.xni.parser.XMLDocumentSource;

public class OutputHandler implements XMLDocumentHandler {
    private Output output;

    public void setOutput(Output output) {
        this.output = output;
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#characters(com.googlecode.ddom.xerces.xni.XMLString, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void characters(XMLString arg0, Augmentations arg1) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#comment(com.googlecode.ddom.xerces.xni.XMLString, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void comment(XMLString arg0, Augmentations arg1) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#doctypeDecl(java.lang.String, java.lang.String, java.lang.String, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void doctypeDecl(String arg0, String arg1, String arg2, Augmentations arg3)
            throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#emptyElement(com.googlecode.ddom.xerces.xni.QName, com.googlecode.ddom.xerces.xni.XMLAttributes, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void emptyElement(QName arg0, XMLAttributes arg1, Augmentations arg2)
            throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#endCDATA(com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void endCDATA(Augmentations arg0) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#endDocument(com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void endDocument(Augmentations arg0) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#endElement(com.googlecode.ddom.xerces.xni.QName, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void endElement(QName arg0, Augmentations arg1) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#endGeneralEntity(java.lang.String, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void endGeneralEntity(String arg0, Augmentations arg1) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#getDocumentSource()
     */
    public XMLDocumentSource getDocumentSource() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#ignorableWhitespace(com.googlecode.ddom.xerces.xni.XMLString, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void ignorableWhitespace(XMLString arg0, Augmentations arg1) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#processingInstruction(java.lang.String, com.googlecode.ddom.xerces.xni.XMLString, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void processingInstruction(String arg0, XMLString arg1, Augmentations arg2)
            throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#setDocumentSource(com.googlecode.ddom.xerces.xni.parser.XMLDocumentSource)
     */
    public void setDocumentSource(XMLDocumentSource arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#startCDATA(com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void startCDATA(Augmentations arg0) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#startDocument(com.googlecode.ddom.xerces.xni.XMLLocator, java.lang.String, com.googlecode.ddom.xerces.xni.NamespaceContext, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void startDocument(XMLLocator arg0, String arg1, NamespaceContext arg2,
            Augmentations arg3) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#startElement(com.googlecode.ddom.xerces.xni.QName, com.googlecode.ddom.xerces.xni.XMLAttributes, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void startElement(QName arg0, XMLAttributes arg1, Augmentations arg2)
            throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#startGeneralEntity(java.lang.String, com.googlecode.ddom.xerces.xni.XMLResourceIdentifier, java.lang.String, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void startGeneralEntity(String arg0, XMLResourceIdentifier arg1, String arg2,
            Augmentations arg3) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#textDecl(java.lang.String, java.lang.String, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void textDecl(String arg0, String arg1, Augmentations arg2) throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.xerces.xni.XMLDocumentHandler#xmlDecl(java.lang.String, java.lang.String, java.lang.String, com.googlecode.ddom.xerces.xni.Augmentations)
     */
    public void xmlDecl(String arg0, String arg1, String arg2, Augmentations arg3)
            throws XNIException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    
}
