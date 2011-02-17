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
package com.googlecode.ddom.cxf;

import javax.xml.soap.SOAPMessage;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;

public class SAAJInInterceptor extends AbstractSoapInterceptor {
    private final Model saajModel;
    
    public SAAJInInterceptor(String phase) {
        super(phase);
        try {
            saajModel = ModelRegistry.getInstance(SAAJInInterceptor.class.getClassLoader()).getModel(ModelDefinitionBuilder.buildModelDefinition("saaj"));
        } catch (ModelLoaderException ex) {
            throw new RuntimeException(ex); // TODO: what is the right exception to throw here??
        }
    }

    public SAAJInInterceptor() {
        this(Phase.PRE_PROTOCOL);
    }
    
    public void handleMessage(SoapMessage message) throws Fault {
        message.setContent(SOAPMessage.class, new SOAPMessageImpl(saajModel, message));
    }
}
