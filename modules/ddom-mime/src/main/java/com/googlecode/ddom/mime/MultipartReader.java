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

import java.io.IOException;
import java.io.InputStream;

public class MultipartReader {
    private final MultipartInputStream stream;
    private String contentTransferEncoding;
    
    public MultipartReader(InputStream in, String boundary) {
        stream = new MultipartInputStream(in, boundary);
    }

    public boolean nextPart() throws IOException {
        return stream.nextPart();
    }

    public boolean nextHeader() throws IOException {
        boolean moreHeaders = stream.nextHeader();
        if (moreHeaders && getHeaderName().equalsIgnoreCase("Content-Transfer-Encoding")) {
            contentTransferEncoding = getHeaderValue();
        }
        return moreHeaders;
    }

    public String getHeaderName() {
        return stream.getHeaderName();
    }

    public String getHeaderValue() {
        return stream.getHeaderValue();
    }
    
    public InputStream getContent() {
        if (contentTransferEncoding == null || contentTransferEncoding.equals("binary")) {
            return stream;
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
    }
}
