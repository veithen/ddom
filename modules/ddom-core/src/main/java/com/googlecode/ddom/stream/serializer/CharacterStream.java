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
package com.googlecode.ddom.stream.serializer;

import java.io.IOException;
import java.io.Writer;

public class CharacterStream implements UnicodeWriter {
    private Writer writer;
    
    public CharacterStream(Writer writer) {
        this.writer = writer;
    }

    public boolean canEncode(int codePoint) {
        return true;
    }

    public void write(int codePoint) throws IOException {
        // TODO: handle surrogates here
        writer.write(codePoint);
    }

    public void write(String data) throws IOException {
        writer.write(data);
    }
}
