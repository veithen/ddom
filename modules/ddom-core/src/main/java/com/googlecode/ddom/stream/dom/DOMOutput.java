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
package com.googlecode.ddom.stream.dom;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlOutput;

/**
 * 
 * 
 * Limitations:
 * <ul>
 * <li>Although a {@link DocumentType} instance is created and added to the output document, events
 * related to the internal subset are not processed. The reason is that in the DOM API all DTD
 * information is read-only. E.g. {@link Entity} objects are read-only, and there is also no factory
 * method to create them.
 * <li>Because the DTD information is not added to the document, {@link EntityReference} objects
 * created by this class will all appear as references to unknown entities.
 * <li>Information about attribute types is lost.
 * </ul>
 * 
 * @author Andreas Veithen
 */
public class DOMOutput extends XmlOutput {
    private final Document document;
    
    public DOMOutput(Document document) {
        this.document = document;
    }

    @Override
    protected XmlHandler createXmlHandler() {
        return new DOMOutputHandler(document);
    }
}
