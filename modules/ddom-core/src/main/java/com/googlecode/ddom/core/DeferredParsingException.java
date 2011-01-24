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
package com.googlecode.ddom.core;

/**
 * Indicates that a parsing error occurred. This exception may be thrown by any core model method
 * that accesses parts of the tree that have not been visited before. The cause associated with this
 * exception (as returned by {@link Throwable#getCause()}) further describes the parsing error. It
 * will be an exception specific to the underlying parser, e.g. a
 * {@link javax.xml.stream.XMLStreamException}.
 * 
 * @author Andreas Veithen
 */
public class DeferredParsingException extends CoreModelException {
    private static final long serialVersionUID = 5510092282617307847L;

    /**
     * Constructor.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public DeferredParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
