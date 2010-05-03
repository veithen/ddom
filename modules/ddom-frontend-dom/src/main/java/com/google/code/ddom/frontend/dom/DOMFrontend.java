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
package com.google.code.ddom.frontend.dom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Frontend;

@Provider(name="dom")
public class DOMFrontend implements Frontend {
    public List<String> getMixins(Map<String,Frontend> frontends) {
        return Arrays.asList(new String[] {
                "com.google.code.ddom.frontend.dom.mixin.AttributeSupport",
                "com.google.code.ddom.frontend.dom.mixin.CDATASectionSupport",
                "com.google.code.ddom.frontend.dom.mixin.CharacterDataSupport",
                "com.google.code.ddom.frontend.dom.mixin.CommentSupport",
                "com.google.code.ddom.frontend.dom.mixin.CoreChildNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.CoreNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.DocumentFragmentSupport",
                "com.google.code.ddom.frontend.dom.mixin.DocumentSupport",
                "com.google.code.ddom.frontend.dom.mixin.DocumentTypeDeclarationSupport",
                "com.google.code.ddom.frontend.dom.mixin.ElementSupport",
                "com.google.code.ddom.frontend.dom.mixin.EntityReferenceSupport",
                "com.google.code.ddom.frontend.dom.mixin.LeafNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.NamespaceDeclarationSupport",
                "com.google.code.ddom.frontend.dom.mixin.NodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.NSAwareAttributeSupport",
                "com.google.code.ddom.frontend.dom.mixin.NSAwareElementSupport",
                "com.google.code.ddom.frontend.dom.mixin.NSAwareNamedNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.NSUnawareAttributeSupport",
                "com.google.code.ddom.frontend.dom.mixin.NSUnawareElementSupport",
                "com.google.code.ddom.frontend.dom.mixin.NSUnawareNamedNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.ParentNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.ProcessingInstructionSupport",
                "com.google.code.ddom.frontend.dom.mixin.TextNodeSupport",
                "com.google.code.ddom.frontend.dom.mixin.TextSupport",
                "com.google.code.ddom.frontend.dom.mixin.TypedAttributeSupport" });
    }
}
