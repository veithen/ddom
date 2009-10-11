
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
 *  Using compareDocumentPosition to check if the document position of the class's attribute 
 *  when compared with a new attribute node is implementation_specific
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-compareDocumentPosition">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-compareDocumentPosition</a>
*/
public final class nodecomparedocumentposition40 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodecomparedocumentposition40(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "hc_staff", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      NodeList elemList;
      Element elem;
      Attr attr1;
      Attr attr2;
      int attrPosition;
      int swappedPosition;
      doc = (Document) load("hc_staff", true);
      elemList = doc.getElementsByTagName("acronym");
      elem = (Element) elemList.item(3);
      attr1 = elem.getAttributeNode("class");
      elem.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "FR-fr");
      attr2 = elem.getAttributeNode("xml:lang");
      attrPosition = (int) attr1.compareDocumentPosition(attr2);
      assertEquals("isImplementationSpecific", 32 & 32, attrPosition & 32);
      assertEquals("otherBitsZero", 0 & 25, attrPosition & 25);
      assertNotEquals("eitherFollowingOrPreceding", 0 & 6, attrPosition & 6);
swappedPosition = (int) attr2.compareDocumentPosition(attr1);
      assertNotEquals("onlyOnePreceding", swappedPosition & 2, attrPosition & 2);
assertNotEquals("onlyOneFollowing", swappedPosition & 4, attrPosition & 4);
}
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodecomparedocumentposition40";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodecomparedocumentposition40.class, args);
   }
}
