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
package com.google.code.ddom.dom.impl.domts;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DocumentBuilderSetting;

public class Factories {
    public static DOMTestDocumentBuilderFactory getFactory() throws Exception {
        return new DOMTestDocumentBuilderFactoryImpl(
                new DocumentBuilderSetting[] {
                        DocumentBuilderSetting.notCoalescing,
                        DocumentBuilderSetting.expandEntityReferences,
                        DocumentBuilderSetting.namespaceAware,
                        DocumentBuilderSetting.notValidating });
        
//        return new JAXPDOMTestDocumentBuilderFactory(null,
//                new DocumentBuilderSetting[] {
//                        DocumentBuilderSetting.notCoalescing,
//                        DocumentBuilderSetting.expandEntityReferences,
//                        DocumentBuilderSetting.notIgnoringElementContentWhitespace,
//                        DocumentBuilderSetting.namespaceAware,
//                        DocumentBuilderSetting.notValidating });
    }
}
