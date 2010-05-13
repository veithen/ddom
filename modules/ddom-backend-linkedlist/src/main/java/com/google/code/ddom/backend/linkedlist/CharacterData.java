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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.core.CoreCharacterData;

public abstract class CharacterData extends LeafNode implements CoreCharacterData {
    private String data;

    public CharacterData(Document document, String data) {
        super(document);
        this.data = data;
    }

    public final String coreGetData() {
        return data;
    }

    public final void coreSetData(String data) {
        this.data = data;
    }
}
