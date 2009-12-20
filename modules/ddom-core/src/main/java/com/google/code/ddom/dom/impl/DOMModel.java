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
package com.google.code.ddom.dom.impl;

import java.util.Arrays;
import java.util.List;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Model;

// TODO: this name is a bit silly (double "com.google.code.ddom.dom.impl.Model"), but DOModel is also not very attractive
@Provider(name="dom")
public class DOMModel implements Model {
    public List<String> getAspectClasses() {
        return Arrays.asList(new String[] {
                "com.google.code.ddom.dom.impl.AttributeSupport",
                "com.google.code.ddom.dom.impl.Attributes",
                "com.google.code.ddom.dom.impl.CharacterDataSupport",
                "com.google.code.ddom.dom.impl.ChildNodes",
                "com.google.code.ddom.dom.impl.Clone",
                "com.google.code.ddom.dom.impl.Create",
                "com.google.code.ddom.dom.impl.DocumentFragmentSupport",
                "com.google.code.ddom.dom.impl.DocumentSupport",
                "com.google.code.ddom.dom.impl.DocumentTypeSupport",
                "com.google.code.ddom.dom.impl.ElementSupport",
                "com.google.code.ddom.dom.impl.EntityReferenceSupport",
                "com.google.code.ddom.dom.impl.GetElementsBy",
                "com.google.code.ddom.dom.impl.Hierarchy",
                "com.google.code.ddom.dom.impl.NamedNodeSupport",
                "com.google.code.ddom.dom.impl.NamespaceLookup",
                "com.google.code.ddom.dom.impl.NodeName",
                "com.google.code.ddom.dom.impl.NodeSupport",
                "com.google.code.ddom.dom.impl.NodeType",
                "com.google.code.ddom.dom.impl.NodeValue",
                "com.google.code.ddom.dom.impl.Normalization",
                "com.google.code.ddom.dom.impl.ProcessingInstructionSupport",
                "com.google.code.ddom.dom.impl.SchemaTypeInfo",
                "com.google.code.ddom.dom.impl.Sibling",
                "com.google.code.ddom.dom.impl.TextContent",
                "com.google.code.ddom.dom.impl.TextSupport",
                "com.google.code.ddom.dom.impl.UserDataSupport" });
    }
}
