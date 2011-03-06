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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class MessageFactoryWrapper extends MessageFactory {
    private final MessageFactory parent;

    public MessageFactoryWrapper(MessageFactory parent) {
        this.parent = parent;
    }

    public SOAPMessage createMessage() throws SOAPException {
        return parent.createMessage();
    }

    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        return parent.createMessage(headers, in);
    }
}
