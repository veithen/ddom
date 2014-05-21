/*
 * Copyright 2009-2011,2014 Andreas Veithen
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
package com.googlecode.ddom.saaj;

import java.io.InputStream;

import com.github.veithen.ddom.SOAPSpec;

public abstract class MessageSet {
    public static final MessageSet SOAP11 = new MessageSet("soap11", SOAPSpec.SOAP11) {
        @Override
        public MessageSet getAltMessageSet() {
            return SOAP12;
        }
    };
    
    public static final MessageSet SOAP12 = new MessageSet("soap12", SOAPSpec.SOAP12) {
        @Override
        public MessageSet getAltMessageSet() {
            return SOAP11;
        }
    };
    
    private final String pkg;
    private final SOAPSpec version;
    
    public MessageSet(String pkg, SOAPSpec version) {
        this.pkg = pkg;
        this.version = version;
    }

    public final SOAPSpec getVersion() {
        return version;
    }

    public final InputStream getTestMessage(String name) {
        return MessageSet.class.getResourceAsStream(pkg + "/" + name);
    }
    
    public abstract MessageSet getAltMessageSet();
}
