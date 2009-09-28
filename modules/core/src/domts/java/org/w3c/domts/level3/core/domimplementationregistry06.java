
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
 * If the implementation supports "XML", DOMImplementationRegistry.getDOMImplementation("xMl 3.0 cOrE") should 
 * return a DOMImplementation where hasFeature("XML", "3.0"), and hasFeature("Core", null) returns true.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/java-binding">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/java-binding</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/ecma-script-binding">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/ecma-script-binding</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-getDOMImpl">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-getDOMImpl</a>
*/
public final class domimplementationregistry06 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public domimplementationregistry06(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);
      if (factory.hasFeature("XML", null) != true) {
         throw org.w3c.domts.DOMTestIncompatibleException.incompatibleFeature("XML", null);
      }

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      org.w3c.dom.bootstrap.DOMImplementationRegistry domImplRegistry;
      DOMImplementation domImpl;
      boolean hasFeature;
      String nullVersion = null;

      domImplRegistry = org.w3c.dom.bootstrap.DOMImplementationRegistry.newInstance();
         assertNotNull("domImplRegistryNotNull", domImplRegistry);
      domImpl = domImplRegistry.getDOMImplementation("xMl 3.0 cOrE");
         assertNotNull("domImplNotNull", domImpl);
      hasFeature = domImpl.hasFeature("XML", "3.0");
assertTrue("hasXML3", hasFeature);
      hasFeature = domImpl.hasFeature("Core", nullVersion);
assertTrue("hasCore", hasFeature);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/domimplementationregistry06";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(domimplementationregistry06.class, args);
   }
}

