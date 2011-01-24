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
package com.google.code.ddom.frontend.axiom.support;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMException;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.stream.StreamException;

public class AxiomExceptionUtil {
    private AxiomExceptionUtil() {}
    
    public static OMException translate(CoreModelException ex) {
        if (ex instanceof DeferredParsingException) {
            // For a DeferredParsingException, the cause is required.
            return new OMException(ex.getCause());
        } else {
            return new OMException(ex);
        }
    }
    
    public static XMLStreamException translate(StreamException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof XMLStreamException) {
            return (XMLStreamException)cause;
        } else {
            return new XMLStreamException(ex.getMessage(), ex);
        }
    }
}
