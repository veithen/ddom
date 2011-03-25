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
package com.googlecode.ddom.stream;

public final class SimpleXmlSource implements XmlSource {
    private final XmlInput input;
    private boolean accessed;

    public SimpleXmlSource(XmlInput input) {
        this.input = input;
    }

    public XmlInput getInput(Hints hints) {
        if (accessed) {
            throw new IllegalStateException("The source has already been accessed");
        } else {
            accessed = true;
            return input;
        }
    }

    public boolean isAccessed() {
        return accessed;
    }

    public boolean isDestructive() {
        return true;
    }
}
