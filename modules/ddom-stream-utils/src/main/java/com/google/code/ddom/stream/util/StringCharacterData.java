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
package com.google.code.ddom.stream.util;

import com.google.code.ddom.stream.spi.CharacterData;
import com.google.code.ddom.stream.spi.StreamException;

public class StringCharacterData implements CharacterData {
    private String data;

    // TODO: hardcoded for now; will be replaced by something else anyway
    public Scope getScope() {
        return Scope.CONSUMER_INVOCATION;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void clear() {
        data = null;
    }
    
    public String getString() throws StreamException {
        return data;
    }
}
