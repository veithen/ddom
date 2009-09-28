
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
 * Check implementation of Node.getFeature on Document.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-getFeature">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-getFeature</a>
*/
public final class nodegetfeature01 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodegetfeature01(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "barfoo", false);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Node node;
      String nullVersion = null;

      Node featureImpl;
      boolean isSupported;
      DOMImplementation domImpl;
      doc = (Document) load("barfoo", false);
      domImpl = doc.getImplementation();
      node =  doc;
      featureImpl = (Node) node.getFeature("Core", nullVersion);
      assertSame("coreUnspecifiedVersion", node, featureImpl);
featureImpl = (Node) node.getFeature("cOrE", nullVersion);
      assertSame("cOrEUnspecifiedVersion", node, featureImpl);
featureImpl = (Node) node.getFeature("+cOrE", nullVersion);
      assertSame("PlusCoreUnspecifiedVersion", node, featureImpl);
featureImpl = (Node) node.getFeature("org.w3c.domts.bogus.feature", nullVersion);
      assertNull("unrecognizedFeature", featureImpl);
      featureImpl = (Node) node.getFeature("cOrE", "2.0");
      assertSame("Core20", node, featureImpl);
featureImpl = (Node) node.getFeature("cOrE", "3.0");
      assertSame("Core30", node, featureImpl);
isSupported = node.isSupported("XML", nullVersion);
      featureImpl = (Node) doc.getFeature("XML", nullVersion);
      
      if (isSupported) {
          assertSame("XMLUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("SVG", nullVersion);
      featureImpl = (Node) doc.getFeature("SVG", nullVersion);
      
      if (isSupported) {
          assertSame("SVGUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("HTML", nullVersion);
      featureImpl = (Node) doc.getFeature("HTML", nullVersion);
      
      if (isSupported) {
          assertSame("HTMLUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("Events", nullVersion);
      featureImpl = (Node) doc.getFeature("Events", nullVersion);
      
      if (isSupported) {
          assertSame("EventsUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("LS", nullVersion);
      featureImpl = (Node) doc.getFeature("LS", nullVersion);
      
      if (isSupported) {
          assertSame("LSUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("LS-Async", nullVersion);
      featureImpl = (Node) doc.getFeature("LS-Async", nullVersion);
      
      if (isSupported) {
          assertSame("LSAsyncUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("XPath", nullVersion);
      featureImpl = (Node) doc.getFeature("XPath", nullVersion);
      
      if (isSupported) {
          assertSame("XPathUnspecified", node, featureImpl);
}
    isSupported = node.isSupported("+HTML", nullVersion);
      featureImpl = (Node) doc.getFeature("HTML", nullVersion);
      
      if (isSupported) {
          assertNotNull("PlusHTMLUnspecified", featureImpl);
      }
    isSupported = node.isSupported("+SVG", nullVersion);
      featureImpl = (Node) doc.getFeature("SVG", nullVersion);
      
      if (isSupported) {
          assertNotNull("PlusSVGUnspecified", featureImpl);
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodegetfeature01";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodegetfeature01.class, args);
   }
}

