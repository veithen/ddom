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
package com.googlecode.ddom.saaj.compat;

import javax.xml.soap.SOAPMessage;

/**
 * Encapsulates behavior that is necessary to ensure compatibility with a particular SAAJ
 * implementations.
 * 
 * @author Andreas Veithen
 */
public interface CompatibilityPolicy {
    /**
     * Wrap the given {@link SOAPMessage}. This method may be used to support client code that
     * expects the {@link SOAPMessage} to implement a particular interface (or to extend a
     * particular class) defined by a specific SAAJ implementation.
     * <p>
     * Wrappers can be constructed with the help of {@link SOAPMessageWrapper}.
     * 
     * @param message
     *            the SOAP message to wrap
     * @return the wrapped SOAP message, or the original message if no wrapping is needed
     */
    SOAPMessage wrapMessage(SOAPMessage message);
}
