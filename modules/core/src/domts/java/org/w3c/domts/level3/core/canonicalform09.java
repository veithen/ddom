
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
 * Normalize document based on section 3.1 with canonical-form set to true 
 * and comments to false and check normalized document.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-canonical-form">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-canonical-form</a>
*/
public final class canonicalform09 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public canonicalform09(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

      org.w3c.domts.DocumentBuilderSetting[] settings = 
          new org.w3c.domts.DocumentBuilderSetting[] {
org.w3c.domts.DocumentBuilderSetting.namespaceAware,
org.w3c.domts.DocumentBuilderSetting.notValidating
        };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "canonicalform01", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      NodeList bodyList;
      Element body;
      DOMConfiguration domConfig;
      boolean canSet;
      boolean canSetValidate;
      org.w3c.domts.DOMErrorMonitor errorMonitor = new org.w3c.domts.DOMErrorMonitor();
      
      Node node;
      String nodeName;
      String nodeValue;
      int nodeType;
      int length;
      Text text;
      doc = (Document) load("canonicalform01", true);
      domConfig = doc.getDomConfig();
      canSet = domConfig.canSetParameter("canonical-form", Boolean.TRUE);
      
      if (canSet) {
          domConfig.setParameter("error-handler", ((Object) /*DOMErrorMonitor */errorMonitor));
      domConfig.setParameter("canonical-form", Boolean.TRUE);
      domConfig.setParameter("comments", Boolean.FALSE);
      doc.normalizeDocument();
      errorMonitor.assertLowerSeverity(this, "normalizeError", 2);
     node = doc.getFirstChild();
      nodeType = (int) node.getNodeType();
      assertEquals("PIisFirstChild", 7, nodeType);
      nodeValue = ((ProcessingInstruction) /*Node */node).getData();
      length = nodeValue.length();
      assertEquals("piDataLength", 36, length);
      node = node.getNextSibling();
      nodeType = (int) node.getNodeType();
      assertEquals("TextisSecondChild", 3, nodeType);
      nodeValue = node.getNodeValue();
      length = nodeValue.length();
      assertEquals("secondChildLength", 1, length);
      node = node.getNextSibling();
      nodeType = (int) node.getNodeType();
      assertEquals("ElementisThirdChild", 1, nodeType);
      node = node.getNextSibling();
      nodeType = (int) node.getNodeType();
      assertEquals("TextisFourthChild", 3, nodeType);
      nodeValue = node.getNodeValue();
      length = nodeValue.length();
      assertEquals("fourthChildLength", 1, length);
      node = node.getNextSibling();
      nodeType = (int) node.getNodeType();
      assertEquals("PIisFifthChild", 7, nodeType);
      nodeValue = ((ProcessingInstruction) /*Node */node).getData();
      assertEquals("trailingPIData", "", nodeValue);
      node = node.getNextSibling();
      assertNull("SixthIsNull", node);
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/canonicalform09";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(canonicalform09.class, args);
   }
}

