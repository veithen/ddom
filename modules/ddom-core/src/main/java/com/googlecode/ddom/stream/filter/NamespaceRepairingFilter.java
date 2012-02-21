/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.stream.filter;

import com.googlecode.ddom.stream.XmlFilter;
import com.googlecode.ddom.stream.XmlHandler;

/**
 * Filter that performs namespace repairing. This filter does two things:
 * <ol>
 * <li>It generates additional namespace declarations where they are missing. This occurs if a
 * prefix-URI binding is used for which no namespace declaration is in scope.
 * <li>It removes redundant namespace declarations. A namespace declaration is redundant if there is
 * another equivalent namespace declaration already in scope.
 * </ol>
 * 
 * @author Andreas Veithen
 */
public class NamespaceRepairingFilter extends XmlFilter {
    @Override
    protected XmlHandler createXmlHandler(XmlHandler target) {
        return new NamespaceRepairingFilterHandler(target);
    }
}
