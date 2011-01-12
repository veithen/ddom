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
package com.google.code.ddom.commons.lang;

public class StringAccumulator {
    private Object content;
    
    public void append(String data) {
        if (content == null) {
            content = data;
        } else if (content instanceof String) {
            StringBuilder buffer = new StringBuilder((String)content);
            buffer.append(data);
            content = buffer;
        } else {
            ((StringBuilder)content).append(data);
        }
    }
    
    public boolean isEmpty() {
        return content == null;
    }
    
    public String toString() {
        if (content == null) {
            return "";
        } else if (content instanceof String) {
            return (String)content;
        } else {
            return ((StringBuilder)content).toString();
        }
    }
    
    public void clear() {
        content = null;
    }
}
