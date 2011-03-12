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
package com.googlecode.ddom.frontend.axiom.soap.intf;

import org.apache.axiom.soap.SOAPHeaderBlock;

import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.ext.ModelExtensionInterface;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;

@ModelExtensionInterface(isAbstract=true, parent=CoreNSAwareElement.class)
public interface AxiomSOAPHeaderBlock extends AxiomElement, SOAPHeaderBlock, HasSOAPVersion {

}
