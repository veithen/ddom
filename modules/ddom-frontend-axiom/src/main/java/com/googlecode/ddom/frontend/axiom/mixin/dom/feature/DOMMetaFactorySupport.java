/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.mixin.dom.feature;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axiom.om.dom.DOMMetaFactory;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMNodeFactory;
import com.googlecode.ddom.jaxp.DocumentBuilderFactoryAdapter;

/**
 * Mixin that adds the {@link DOMMetaFactory} interface to the {@link NodeFactory}. It assumes that
 * the DOM front-end is also enabled (and supplies the mixin that adds the {@link DOMNodeFactory}
 * interface).
 * 
 * @author Andreas Veithen
 */
@Mixin(NodeFactory.class)
public abstract class DOMMetaFactorySupport implements DOMMetaFactory, DOMNodeFactory {
    public final DocumentBuilderFactory newDocumentBuilderFactory() {
        DocumentBuilderFactory factory = new DocumentBuilderFactoryAdapter(this);
        factory.setNamespaceAware(true);
        return factory;
    }
}
