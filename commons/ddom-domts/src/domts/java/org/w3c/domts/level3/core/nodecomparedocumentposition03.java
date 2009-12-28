
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
 *  Using compareDocumentPosition check if the document position of two Document nodes obtained from the 
 *  same xml document is disconnected, implementation specific, and that the order of these two documents
 *  is reserved.
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-compareDocumentPosition">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-compareDocumentPosition</a>
*/
public final class nodecomparedocumentposition03 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodecomparedocumentposition03(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "hc_staff", false);
    preload(contentType, "hc_staff", false);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Document docComp;
      int documentPosition1;
      int documentPosition2;
      int documentPosition3;
      doc = (Document) load("hc_staff", false);
      docComp = (Document) load("hc_staff", false);
      documentPosition1 = (int) doc.compareDocumentPosition(docComp);
      assertEquals("isImplSpecificDisconnected1", 33 & 57, documentPosition1 & 57);
      documentPosition2 = (int) docComp.compareDocumentPosition(doc);
      assertNotEquals("notBothPreceding", documentPosition1 & 2, documentPosition2 & 2);
assertNotEquals("notBothFollowing", documentPosition1 & 4, documentPosition2 & 4);
assertEquals("isImplSpecificDisconnected2", 33 & 57, documentPosition2 & 57);
      documentPosition3 = (int) doc.compareDocumentPosition(docComp);
      assertEquals("isConsistent", documentPosition1, documentPosition3);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodecomparedocumentposition03";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodecomparedocumentposition03.class, args);
   }
}
