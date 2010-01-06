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
package com.google.code.ddom.stream.spi;

// TODO: specify that args must not be null
// TODO: we should support interning as well; reason: when importing or adopting nodes from another document, we don't need to lookup the symbols again (if the symbol tables of both documents have interning enabled)
public interface Symbols {
    String lookupSymbol(String str);
    String lookupSymbol(String str, int beginIndex, int endIndex);
    String lookupSymbol(char[] str, int offset, int length);
    String getSymbol(String str);
    String getSymbol(String str, int beginIndex, int endIndex);
    String getSymbol(char[] str, int offset, int length);
}
