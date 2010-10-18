/*
 * Copyright  2009 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.xml.security.utils;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface ElementChecker {
	 /**
	  * Check that the elemnt is the one expect
	  *
	  * @throws XMLSecurityException
	  */
	   public void guaranteeThatElementInCorrectSpace(ElementProxy expected, Element actual)
	           throws XMLSecurityException;
	   
	   public boolean isNamespaceElement(Node el, String type, String ns);
}
