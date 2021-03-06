
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
 *      Invoke setIdAttributeNS on newly added attribute on the third strong element.  Verify by calling
 *      isID on the attribute node and getElementById on document node.
 *      Call setIdAttributeNS on the same element to reset ID but with a non-existing attribute should generate
 *      NOT_FOUND_ERR
 *     
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ElSetIdAttrNS">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ElSetIdAttrNS</a>
*/
public final class elementsetidattributens13 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public elementsetidattributens13(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      Element nameElem;
      NamedNodeMap attributesMap;
      Attr attr;
      boolean id = false;
      Element elem;
      String elemName;
      doc = (Document) load("hc_staff", true);
      elemList = doc.getElementsByTagName("strong");
      nameElem = (Element) elemList.item(2);
      nameElem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:newAttr", "newValue");
      nameElem.setIdAttributeNS("http://www.w3.org/2000/xmlns/", "newAttr", true);
      attributesMap = nameElem.getAttributes();
      attr = (Attr) attributesMap.getNamedItem("xmlns:newAttr");
      id = attr.isId();
      assertTrue("elementsetidattributensIsIdTrue13", id);
      elem = doc.getElementById("newValue");
      elemName = elem.getTagName();
      assertEquals("elementsetidattributensGetElementById13", "strong", elemName);
      
      {
         boolean success = false;
         try {
            nameElem.setIdAttributeNS("http://www.w3.org/XML/1998/namespace", "lang", false);
          } catch (DOMException ex) {
            success = (ex.code == DOMException.NOT_FOUND_ERR);
         }
         assertTrue("throw_NOT_FOUND_ERR", success);
      }
}
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/elementsetidattributens13";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(elementsetidattributens13.class, args);
   }
}

