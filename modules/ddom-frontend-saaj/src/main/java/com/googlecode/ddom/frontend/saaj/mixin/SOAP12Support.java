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
package com.googlecode.ddom.frontend.saaj.mixin;

import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.intf.HasSOAPVersion;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Body;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Envelope;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12HeaderElement;
import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;

@Mixin({SAAJSOAP12Body.class, SAAJSOAP12Envelope.class, SAAJSOAP12Fault.class, SAAJSOAP12HeaderElement.class})
public abstract class SOAP12Support implements HasSOAPVersion {
    public SOAPVersion getSOAPVersion() {
        return SOAPVersion.SOAP12;
    }
}
