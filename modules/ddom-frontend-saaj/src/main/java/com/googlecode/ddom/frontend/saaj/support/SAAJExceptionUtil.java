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
package com.googlecode.ddom.frontend.saaj.support;

import javax.xml.soap.SOAPException;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionUtil;

public final class SAAJExceptionUtil {
    private SAAJExceptionUtil() {}
    
    public static SOAPException toSOAPException(CoreModelException ex) {
        return new SOAPException(ex);
    }
    
    public static RuntimeException toRuntimeException(CoreModelException ex) {
        return DOMExceptionUtil.translate(ex);
    }
}
