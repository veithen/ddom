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
package com.googlecode.ddom.asyncws;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.googlecode.ddom.saaj.MessageFactoryImpl;
import com.googlecode.ddom.saaj.compat.DefaultCompatibilityPolicy;

public class AsyncWSServlet extends HttpServlet {
    private MessageFactoryImpl messageFactory;
    
    @Override
    public void init() throws ServletException {
        try {
            messageFactory = new MessageFactoryImpl(null, DefaultCompatibilityPolicy.INSTANCE);
        } catch (SOAPException ex) {
            throw new ServletException("Failed to create SAAJ message factory", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", req.getContentType());
        SOAPMessage message;
        try {
            message = messageFactory.createMessage(headers, req.getInputStream());
        } catch (SOAPException ex) {
            throw new ServletException(ex);
        }
        AsyncContext asyncContext = req.startAsync();
        
    }
}
