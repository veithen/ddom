
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
 *      Declares the attribute specified by local name and namespace URI to be of type ID. If the value of the 
 *      specified attribute is unique then this element node can later be retrieved using getElementById on Document. 
 *      Note, however, that this simply affects this node and does not change any grammar that may be in use. 
 *      
 *      Invoke setIdAttributeNS on two existing attributes of the second p element and the third
 *      acronym element.  Verify by calling isId on the attributes and getElementById with different values on document node.  
 *     
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ElSetIdAttrNS">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ElSetIdAttrNS</a>
*/
public final class elementsetidattributens14 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public elementsetidattributens14(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
      NodeList elemList;
      Element pElem;
      Element acronymElem;
      NamedNodeMap attributesMap;
      Attr attr;
      boolean id = false;
      Element elem;
      String elemName;
      doc = (Document) load("hc_staff", false);
      elemList = doc.getElementsByTagNameNS("*", "p");
      pElem = (Element) elemList.item(1);
      elemList = doc.getElementsByTagNameNS("*", "acronym");
      acronymElem = (Element) elemList.item(2);
      pElem.setIdAttributeNS("http://www.w3.org/2000/xmlns/", "dmstc", true);
      acronymElem.setIdAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", true);
      attributesMap = pElem.getAttributes();
      attr = (Attr) attributesMap.getNamedItem("xmlns:dmstc");
      id = attr.isId();
      assertTrue("elementsetidattributensIsId1True14", id);
      attributesMap = acronymElem.getAttributes();
      attr = (Attr) attributesMap.getNamedItem("xsi:noNamespaceSchemaLocation");
      id = attr.isId();
      assertTrue("elementsetidattributensIsId2True14", id);
      elem = doc.getElementById("Yes");
      elemName = elem.getTagName();
      assertEquals("elementsetidattributens1GetElementById14", "acronym", elemName);
      elem = doc.getElementById("http://www.usa.com");
      elemName = elem.getTagName();
      assertEquals("elementsetidattributens2GetElementById14", "p", elemName);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/elementsetidattributens14";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(elementsetidattributens14.class, args);
   }
}
