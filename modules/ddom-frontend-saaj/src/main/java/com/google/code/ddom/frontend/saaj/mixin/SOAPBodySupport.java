/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.frontend.saaj.mixin;

import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

import org.w3c.dom.Document;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.ext.SOAPBodyExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPBody;

@Mixin(SOAPBodyExtension.class)
public abstract class SOAPBodySupport implements SAAJSOAPBody {
    public SOAPBodyElement addBodyElement(Name arg0) throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPBodyElement addBodyElement(QName arg0) throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPBodyElement addDocument(Document arg0) throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPFault addFault() throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPFault addFault(Name arg0, String arg1, Locale arg2)
            throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPFault addFault(Name arg0, String arg1) throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPFault addFault(QName arg0, String arg1, Locale arg2)
            throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPFault addFault(QName arg0, String arg1) throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public Document extractContentAsDocument() throws SOAPException {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPFault getFault() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasFault() {
        // TODO Auto-generated method stub
        return false;
    }
}
