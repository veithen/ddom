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
package com.googlecode.ddom.mime;

import java.io.InputStream;

public class Multipart {
    private final MultipartReader mpr;
    private final BlobFactory blobFactory;

    public Multipart(InputStream in, String boundary, BlobFactory blobFactory) {
        this.mpr = new MultipartReader(in, boundary);
        this.blobFactory = blobFactory;
    }
    
    
}
