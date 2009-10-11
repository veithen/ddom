
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
 *      Invoke setIdAttributeNS on an existing attribute with a namespace URI and a qualified name.  Verify by calling
 *      isID on the attribute node and getElementById on document node. Assume the grammar has not defined any
 *      element of typeID. Call setIdAttributeNS with isId=false to reset. Method isId should now return false. 
 *     
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ElSetIdAttrNS">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ElSetIdAttrNS</a>
*/
public final class elementsetidattributens02 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public elementsetidattributens02(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
    preload(contentType, "hc_staff", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      NodeList elemList;
      Element addressElem;
      NamedNodeMap attributesMap;
      Attr attr;
      boolean id = false;
      Element elem;
      String elemName;
      String xsiNS = "http://www.w3.org/2001/XMLSchema-instance";
      doc = (Document) load("hc_staff", true);
      elemList = doc.getElementsByTagNameNS("*", "acronym");
      addressElem = (Element) elemList.item(2);
      addressElem.setIdAttributeNS(xsiNS, "noNamespaceSchemaLocation", true);
      attributesMap = addressElem.getAttributes();
      attr = (Attr) attributesMap.getNamedItem("xsi:noNamespaceSchemaLocation");
      id = attr.isId();
      assertTrue("elementsetidattributensIsIdTrue02", id);
      elem = doc.getElementById("Yes");
      assertNotNull("getElementByIDNotNull", elem);
      elemName = elem.getTagName();
      assertEquals("elementsetidattributensGetElementById01", "acronym", elemName);
      addressElem.setIdAttributeNS(xsiNS, "noNamespaceSchemaLocation", false);
      id = attr.isId();
      assertFalse("elementsetidattributensIsIdFalse02", id);
}
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/elementsetidattributens02";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(elementsetidattributens02.class, args);
   }
}
