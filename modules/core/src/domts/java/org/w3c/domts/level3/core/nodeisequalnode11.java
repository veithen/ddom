
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
 *  Retreive the first element node whose localName is "p".  Import it into a new
 *  Document with deep=false.  Using isEqualNode check if the original and the imported
 *  Element Node are not equal the child nodes are different.
 *  Import with deep and the should still be unequal if
 *  validating since the
 *  new document does not provide the same default attributes.
 *  Import it into another instance of the source document
 *  and then the imported node and the source should be equal.   
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-isEqualNode">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-isEqualNode</a>
* @see <a href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=529">http://www.w3.org/Bugs/Public/show_bug.cgi?id=529</a>
*/
public final class nodeisequalnode11 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodeisequalnode11(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "hc_staff", false);
    preload(contentType, "hc_staff", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      DOMImplementation domImpl;
      NodeList employeeList;
      Document newDoc;
      Document dupDoc;
      Element elem1;
      Element elem2;
      Element elem3;
      Element elem4;
      boolean isEqual;
      DocumentType nullDocType = null;

      Element docElem;
      String rootNS;
      String rootName;
      doc = (Document) load("hc_staff", false);
      docElem = doc.getDocumentElement();
      rootNS = docElem.getNamespaceURI();
      rootName = docElem.getTagName();
      domImpl = doc.getImplementation();
      newDoc = domImpl.createDocument(rootNS, rootName, nullDocType);
      employeeList = doc.getElementsByTagName("p");
      elem1 = (Element) employeeList.item(0);
      elem2 = (Element) newDoc.importNode(elem1, false);
      isEqual = elem1.isEqualNode(elem2);
      assertFalse("nodeisequalnodeFalse11", isEqual);
elem3 = (Element) newDoc.importNode(elem1, true);
      isEqual = elem1.isEqualNode(elem3);
      
      if (isValidating()) {
          assertFalse("deepImportNoDTD", isEqual);
}
    dupDoc = (Document) load("hc_staff", true);
      elem4 = (Element) dupDoc.importNode(elem1, true);
      isEqual = elem1.isEqualNode(elem4);
      assertTrue("deepImportSameDTD", isEqual);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodeisequalnode11";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodeisequalnode11.class, args);
   }
}

