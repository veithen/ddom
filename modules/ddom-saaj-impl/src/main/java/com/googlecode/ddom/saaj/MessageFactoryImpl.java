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
package com.googlecode.ddom.saaj;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;

public class MessageFactoryImpl extends MessageFactory {
    private static final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance(MessageFactoryImpl.class.getClassLoader());
    
    private final SOAPVersion soapVersion;
    
    public MessageFactoryImpl(SOAPVersion soapVersion) {
        this.soapVersion = soapVersion;
    }

    @Override
    public SOAPMessage createMessage() throws SOAPException {
        return new SOAPMessageImpl(new SOAPPartImpl(soapVersion, (SAAJDocument)documentHelper.newDocument("saaj")));
    }
    
    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        return new SOAPMessageImpl(new SOAPPartImpl(soapVersion, (SAAJDocument)documentHelper.parse("saaj", in)));
    }
}
