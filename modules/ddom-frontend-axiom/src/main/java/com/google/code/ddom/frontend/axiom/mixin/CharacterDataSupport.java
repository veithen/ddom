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
package com.google.code.ddom.frontend.axiom.mixin;

import org.apache.axiom.om.OMNode;

import com.google.code.ddom.frontend.axiom.intf.AxiomCharacterData;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(CoreCharacterData.class)
public abstract class CharacterDataSupport implements AxiomCharacterData {
    public final int getType() {
        return OMNode.TEXT_NODE;
    }

    public String getText() {
        return coreGetData();
    }
}