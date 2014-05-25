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
package com.googlecode.ddom.frontend.saaj;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import com.googlecode.ddom.core.ext.ModelExtensionMapper;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP11HeaderElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12Header;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAP12HeaderElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPBody;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPBodyElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPElement;

/**
 * {@link ModelExtensionMapper} implementation for SAAJ. It ensures that elements that are children
 * of {@link SOAPHeader} (resp. {@link SOAPBody}) nodes are of type {@link SOAPHeaderElement} (resp.
 * {@link SOAPBodyElement}).
 * <p>
 * Note that this behavior differs from the SAAJ reference implementation which always creates these
 * nodes as plain {@link SOAPElement} instances (and then later converts them to
 * {@link SOAPHeaderElement} or {@link SOAPBodyElement} instances).
 * 
 * @author Andreas Veithen
 */
public class SAAJModelExtensionMapper implements ModelExtensionMapper {
    private Class<? extends SAAJSOAPElement> childType;
    private int depth;
    
    public Class<?> startElement(String namespaceURI, String localName) {
        if (childType != null) {
            // TODO: this probably gives the wrong type for SOAP faults, but there is no unit test for that yet
            return depth++ == 0 ? childType : null;
        } else {
            Class<?> type = SAAJModelExtension.INSTANCE.mapElement(namespaceURI, localName);
            if (type != null) {
                if (SAAJSOAP11Header.class.isAssignableFrom(type)) {
                    childType = SAAJSOAP11HeaderElement.class;
                } else if (SAAJSOAP12Header.class.isAssignableFrom(type)) {
                    childType = SAAJSOAP12HeaderElement.class;
                } else if (SAAJSOAPBody.class.isAssignableFrom(type)) {
                    childType = SAAJSOAPBodyElement.class;
                }
            }
            return type;
        }
    }

    public void endElement() {
        if (childType != null) {
            if (depth > 0) {
                depth--;
            } else {
                childType = null;
            }
        }
    }
}
