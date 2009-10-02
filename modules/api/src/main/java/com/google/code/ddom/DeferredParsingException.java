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
package com.google.code.ddom;

/**
 * Indicates that a parsing error occurred. Since DDOM used deferred parsing, this exception may be
 * thrown by any DOM method that accesses parts of the tree that have not been visited before. This
 * exception does not extend {@link org.w3c.dom.DOMException} since there is no meaningful way of
 * representing this error condition using {@link org.w3c.dom.DOMException}. It is an unchecked
 * exception because the DOM API doesn't foresee any checked exceptions and because in general the
 * user code will not be able to recover from this error.
 * <p>
 * The cause associated with this exception (as returned by {@link Throwable#getCause()}) further
 * describes the parsing error. It will be an exception specific to the underlying parser, typically
 * a {@link javax.xml.stream.XMLStreamException} (though this may not always be the case).
 * 
 * @author Andreas Veithen
 */
public class DeferredParsingException extends RuntimeException {
    private static final long serialVersionUID = 8181209190936565978L;

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
