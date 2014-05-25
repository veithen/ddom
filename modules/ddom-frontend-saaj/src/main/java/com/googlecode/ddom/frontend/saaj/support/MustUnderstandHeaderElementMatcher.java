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
package com.googlecode.ddom.frontend.saaj.support;

import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPHeaderElement;
import com.googlecode.ddom.util.lang.ObjectUtils;

public final class MustUnderstandHeaderElementMatcher implements ElementMatcher<SAAJSOAPHeaderElement> {
    public static final MustUnderstandHeaderElementMatcher INSTANCE = new MustUnderstandHeaderElementMatcher();
    
    private MustUnderstandHeaderElementMatcher() {}

    public boolean matches(SAAJSOAPHeaderElement element, String namespaceURI, String name) {
        return element.getMustUnderstand() && ObjectUtils.equals(name, element.getActor());
    }
}
