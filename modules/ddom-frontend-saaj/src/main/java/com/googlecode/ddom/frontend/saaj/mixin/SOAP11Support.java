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
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Body;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Envelope;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Fault;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11HeaderElement;
import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;

@Mixin({SAAJSOAP11Body.class, SAAJSOAP11Envelope.class, SAAJSOAP11Fault.class, SAAJSOAP11Header.class, SAAJSOAP11HeaderElement.class})
public abstract class SOAP11Support implements HasSOAPVersion {
    public SOAPVersion getSOAPVersion() {
        return SOAPVersion.SOAP11;
    }
}
