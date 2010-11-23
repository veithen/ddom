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
package com.googlecode.ddom.axiom;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.soap.SOAPFactory;

import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public class OMMetaFactoryImpl implements OMMetaFactory {
    private final OMMetaFactory delegate;
    
    public OMMetaFactoryImpl() {
        delegate = DocumentHelperFactory.INSTANCE.getDefaultInstance().getAPIObject(
                ModelDefinitionBuilder.buildModelDefinition("axiom-soap"), OMMetaFactory.class);
    }
    
    public OMFactory getOMFactory() {
        return delegate.getOMFactory();
    }

    public SOAPFactory getSOAP11Factory() {
        return delegate.getSOAP11Factory();
    }

    public SOAPFactory getSOAP12Factory() {
        return delegate.getSOAP12Factory();
    }
}