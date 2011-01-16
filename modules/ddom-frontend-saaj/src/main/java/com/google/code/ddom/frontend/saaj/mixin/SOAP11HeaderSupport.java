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
package com.google.code.ddom.frontend.saaj.mixin;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP11Header;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAP11HeaderElement;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPElement;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPHeader;

@Mixin(SAAJSOAP11Header.class)
public abstract class SOAP11HeaderSupport implements SAAJSOAPHeader {
    public final Class<? extends SAAJSOAPElement> getChildType() {
        return SAAJSOAP11HeaderElement.class;
    }
}
