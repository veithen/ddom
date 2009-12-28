
/*
This Java source file was generated by test-to-java.xsl
and is a derived work from the source document.
The source document contained the following notice:



Copyright (c) 2004 World Wide Web Consortium, 
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
 * Normalize document with infoset set to true,  
 * check if string values were not normalized.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-infoset">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-infoset</a>
*/
public final class infoset03 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public infoset03(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
    preload(contentType, "datatype_normalization2", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      NodeList elemList;
      Element element;
      DOMConfiguration domConfig;
      String str;
      boolean canSetValidate;
      boolean canSetXMLSchema;
      String xsdNS = "http://www.w3.org/2001/XMLSchema";
      org.w3c.domts.DOMErrorMonitor errorMonitor = new org.w3c.domts.DOMErrorMonitor();
      
      Node childNode;
      String childValue;
      int childLength;
      doc = (Document) load("datatype_normalization2", true);
      domConfig = doc.getDomConfig();
      canSetValidate = domConfig.canSetParameter("validate", Boolean.TRUE);
      canSetXMLSchema = domConfig.canSetParameter("schema-type", ((Object) /*DOMString */xsdNS));
      
      if (
    (canSetValidate & canSetXMLSchema)
) {
          domConfig.setParameter("infoset", Boolean.TRUE);
      domConfig.setParameter("validate", Boolean.TRUE);
      domConfig.setParameter("schema-type", ((Object) /*DOMString */xsdNS));
      domConfig.setParameter("error-handler", ((Object) /*DOMErrorMonitor */errorMonitor));
      doc.normalizeDocument();
      errorMonitor.assertLowerSeverity(this, "normalizeError", 2);
     elemList = doc.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "code");
      element = (Element) elemList.item(0);
      childNode = element.getFirstChild();
      childValue = childNode.getNodeValue();
      childLength = childValue.length();
      assertEquals("content1", 18, childLength);
      element = (Element) elemList.item(1);
      childNode = element.getFirstChild();
      childValue = childNode.getNodeValue();
      assertEquals("content2", "EMP  0001", childValue);
      element = (Element) elemList.item(2);
      childNode = element.getFirstChild();
      childValue = childNode.getNodeValue();
      assertEquals("content3", "EMP 0001", childValue);
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/infoset03";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(infoset03.class, args);
   }
}
