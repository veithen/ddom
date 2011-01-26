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
package com.googlecode.ddom.stream.sax;

public class SAXStreamUtils {
    public static String normalizeNamespaceURI(String uri) {
        return uri.length() == 0 ? null : uri;
    }
    
    public static String getPrefixFromQName(String qName) {
        int i = qName.indexOf(':');
        return i == -1 ? null : qName.substring(0, i);
    }
    
    public static String getDeclaredPrefixFromQName(String qName) {
        int i = qName.indexOf(':');
        return i == -1 ? null : qName.substring(i+1);
    }
}
