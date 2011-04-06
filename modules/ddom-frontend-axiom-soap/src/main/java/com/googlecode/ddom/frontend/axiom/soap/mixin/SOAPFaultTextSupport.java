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
package com.googlecode.ddom.frontend.axiom.soap.mixin;

import org.apache.axiom.soap.SOAP12Constants;

import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultText;
import com.googlecode.ddom.frontend.axiom.support.AxiomAttributeMatcher;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(AxiomSOAPFaultText.class)
public abstract class SOAPFaultTextSupport implements AxiomSOAPFaultText {
    public final String getLang() {
        try {
            CoreAttribute attr = coreGetAttribute(AxiomAttributeMatcher.INSTANCE, SOAP12Constants.SOAP_FAULT_TEXT_LANG_ATTR_NS_URI,
                    SOAP12Constants.SOAP_FAULT_TEXT_LANG_ATTR_LOCAL_NAME);
            return attr == null ? null : attr.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setLang(String lang) {
        try {
            coreSetAttribute(AxiomAttributeMatcher.INSTANCE, SOAP12Constants.SOAP_FAULT_TEXT_LANG_ATTR_NS_URI,
                    SOAP12Constants.SOAP_FAULT_TEXT_LANG_ATTR_LOCAL_NAME, SOAP12Constants.SOAP_FAULT_TEXT_LANG_ATTR_NS_PREFIX, lang);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
}
