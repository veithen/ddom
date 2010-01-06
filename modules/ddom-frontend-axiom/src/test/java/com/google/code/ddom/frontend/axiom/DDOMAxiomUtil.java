/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.frontend.axiom;

import java.io.StringReader;

import org.apache.axiom.om.OMDocument;

import com.google.code.ddom.DocumentHelper;

public class DDOMAxiomUtil implements AxiomUtil {
    public static final DDOMAxiomUtil INSTANCE = new DDOMAxiomUtil();
    
    private final DocumentHelper factory = DocumentHelper.newInstance();

    private DDOMAxiomUtil() {}
    
    public OMDocument createDocument() {
        return (OMDocument)factory.newDocument("axiom");
    }

    public OMDocument parse(String xml) {
        return (OMDocument)factory.parse("axiom", new StringReader(xml));
    }
}
