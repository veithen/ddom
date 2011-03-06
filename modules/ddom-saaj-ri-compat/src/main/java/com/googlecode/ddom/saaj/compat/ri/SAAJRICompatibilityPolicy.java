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
package com.googlecode.ddom.saaj.compat.ri;

import javax.xml.soap.SOAPMessage;

import com.googlecode.ddom.saaj.compat.CompatibilityPolicy;
import com.googlecode.ddom.spi.Provider;
import com.sun.xml.messaging.saaj.soap.MessageImpl;

/**
 * {@link CompatibilityPolicy} implementation for Sun's SAAJ reference implementation. This policy
 * can be used to provide support for Sun's JAX-RPC reference implementation (which makes use of
 * classes specific to the SAAJ reference implementation).
 * 
 * @author Andreas Veithen
 */
@Provider(name="saaj-ri")
public class SAAJRICompatibilityPolicy implements CompatibilityPolicy {
    public SOAPMessage wrapMessage(SOAPMessage message) {
        return new MessageImpl(message);
    }
}
