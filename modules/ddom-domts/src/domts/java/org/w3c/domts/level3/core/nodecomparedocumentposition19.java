
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
 *  The method compareDocumentPosition compares a node with this node with regard to their position in the 
 *  document and according to the document order.
 *  
 *  Using compareDocumentPosition check if the document position of the first CDATASection node
 *  of the second element whose localName is name compared with the second CDATASection node
 *  is PRECEDING and is FOLLOWING vice versa.
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-compareDocumentPosition">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-compareDocumentPosition</a>
*/
public final class nodecomparedocumentposition19 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodecomparedocumentposition19(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

      org.w3c.domts.DocumentBuilderSetting[] settings = 
          new org.w3c.domts.DocumentBuilderSetting[] {
org.w3c.domts.DocumentBuilderSetting.notCoalescing,
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
      NodeList elemList;
      Element elemStrong;
      CDATASection cdata1;
      CDATASection cdata2;
      Node aNode;
      int cdata1Position;
      int cdata2Position;
      doc = (Document) load("hc_staff", false);
      elemList = doc.getElementsByTagNameNS("*", "strong");
      elemStrong = (Element) elemList.item(1);
      cdata2 = (CDATASection) elemStrong.getLastChild();
      aNode = cdata2.getPreviousSibling();
      cdata1 = (CDATASection) aNode.getPreviousSibling();
      cdata1Position = (int) cdata1.compareDocumentPosition(cdata2);
      assertEquals("nodecomparedocumentposition19_cdata2Follows", 4, cdata1Position);
      cdata2Position = (int) cdata2.compareDocumentPosition(cdata1);
      assertEquals("nodecomparedocumentposition_cdata1Precedes", 2, cdata2Position);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodecomparedocumentposition19";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodecomparedocumentposition19.class, args);
   }
}
