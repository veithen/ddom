
/*
This Java source file was generated by test-to-java.xsl
and is a derived work from the source document.
The source document contained the following notice:



Copyright (c) 2001-2004 World Wide Web Consortium, 
(Massachusetts Institute of Technology, Institut National de
Recherche en Informatique et en Automatique, Keio University).  All 
Rights Reserved.  This program is distributed under the W3C's Software
Intellectual Property License.  This program is distributed in the 
hope that it will be useful, but WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
PURPOSE.  

See W3C License http://www.w3.org/Consortium/Legal/ for more details.


*/

package org.w3c.domts.level3.core;

import org.w3c.dom.*;


import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;



/**
 *  Using isDefaultNamespace on a DocumentElement of a new Document node with the value of the 
 *  namespaceURI parameter equal to the namespaceURI of the newly created Document and check if the 
 *  value returned is false.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-isDefaultNamespace">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-isDefaultNamespace</a>
*/
public final class nodeisdefaultnamespace05 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodeisdefaultnamespace05(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

      org.w3c.domts.DocumentBuilderSetting[] settings = 
          new org.w3c.domts.DocumentBuilderSetting[] {
org.w3c.domts.DocumentBuilderSetting.namespaceAware
        };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "hc_staff", false);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Element elem;
      DOMImplementation domImpl;
      Document newDoc;
      boolean isDefault;
      DocumentType nullDocType = null;

      String nullNSURI = null;

      Element docElem;
      String rootNS;
      String rootName;
      doc = (Document) load("hc_staff", false);
      docElem = doc.getDocumentElement();
      rootNS = docElem.getNamespaceURI();
      rootName = docElem.getLocalName();
      domImpl = doc.getImplementation();
      newDoc = domImpl.createDocument(rootNS, rootName, nullDocType);
      elem = newDoc.getDocumentElement();
      isDefault = elem.isDefaultNamespace(rootNS);
      assertTrue("nodeisdefaultnamespace05_1", isDefault);
      isDefault = elem.isDefaultNamespace(nullNSURI);
      assertFalse("nodeisdefaultnamespace05_2", isDefault);
}
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodeisdefaultnamespace05";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodeisdefaultnamespace05.class, args);
   }
}
