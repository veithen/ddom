
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
 * Normalize document with datatype-normalization set to true.  
 * Check if double values were normalized.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-datatype-normalization">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-datatype-normalization</a>
*/
public final class datatypenormalization01 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public datatypenormalization01(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
    preload(contentType, "datatype_normalization", true);
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
      boolean canSetNormalization;
      boolean canSetValidate;
      boolean canSetXMLSchema;
      String xsdNS = "http://www.w3.org/2001/XMLSchema";
      org.w3c.domts.DOMErrorMonitor errorMonitor = new org.w3c.domts.DOMErrorMonitor();
      
      doc = (Document) load("datatype_normalization", true);
      domConfig = doc.getDomConfig();
      canSetNormalization = domConfig.canSetParameter("datatype-normalization", Boolean.TRUE);
      canSetValidate = domConfig.canSetParameter("validate", Boolean.TRUE);
      canSetXMLSchema = domConfig.canSetParameter("schema-type", ((Object) /*DOMString */xsdNS));
      
      if (
    (canSetNormalization & canSetValidate & canSetXMLSchema)
) {
          domConfig.setParameter("datatype-normalization", Boolean.TRUE);
      domConfig.setParameter("validate", Boolean.TRUE);
      domConfig.setParameter("schema-type", ((Object) /*DOMString */xsdNS));
      domConfig.setParameter("error-handler", ((Object) /*DOMErrorMonitor */errorMonitor));
      doc.normalizeDocument();
      errorMonitor.assertLowerSeverity(this, "normalizeError", 2);
     elemList = doc.getElementsByTagNameNS("http://www.w3.org/2001/DOM-Test-Suite/Level-3/datatype_normalization", "double");
      element = (Element) elemList.item(0);
      str = element.getAttribute("value");
      assertEquals("firstValue", "+0003.141592600E+0000", str);
      str = element.getAttribute("union");
      assertEquals("firstUnion", "+0003.141592600E+0000", str);
      str = element.getTextContent();
      assertEquals("firstList", "-31415926.00E-7 2.718", str);
      element = (Element) elemList.item(1);
      str = element.getAttribute("value");
      assertEquals("secondValue", "NaN", str);
      str = element.getAttribute("union");
      assertEquals("secondUnion", "NaN", str);
      str = element.getTextContent();
      assertEquals("secondList", "INF -INF", str);
      element = (Element) elemList.item(2);
      str = element.getAttribute("value");
      assertEquals("thirdValue", "1", str);
      str = element.getAttribute("union");
      assertEquals("thirdUnion", "1", str);
      str = element.getTextContent();
      assertEquals("thirdList", "-0", str);
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/datatypenormalization01";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(datatypenormalization01.class, args);
   }
}

