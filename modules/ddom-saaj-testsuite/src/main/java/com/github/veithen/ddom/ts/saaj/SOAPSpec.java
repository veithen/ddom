/*
 * Copyright 2014 Andreas Veithen
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
package com.github.veithen.ddom.ts.saaj;

public class SOAPSpec {
    public static final SOAPSpec SOAP11 = new SOAPSpec("1.1", "text/xml");
    public static final SOAPSpec SOAP12 = new SOAPSpec("1.2", "application/soap+xml");
    
    private final String versionNumber;
    private final String contentType;

    public SOAPSpec(String versionNumber, String contentType) {
        this.versionNumber = versionNumber;
        this.contentType = contentType;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getContentType() {
        return contentType;
    }
}
