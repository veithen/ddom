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
package com.google.code.ddom.xmlts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class XMLConformanceTest {
    public enum Type { VALID, INVALID, NOT_WELL_FORMED, ERROR }
    
    private final String id;
    private final Type type;
    private final URL url;
    private final String description;
    
    XMLConformanceTest(String id, Type type, URL url, String description) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.description = description;
    }

    public String getId() {
        return id;
    }
    
    public Type getType() {
        return type;
    }
    
    public URL getUrl() {
        return url;
    }
    
    public String getSystemId() {
        return url.toExternalForm();
    }
    
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }
    
    public String getDescription() {
        return description;
    }
}
