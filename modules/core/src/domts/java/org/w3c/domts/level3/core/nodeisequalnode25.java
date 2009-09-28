
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
 *  
 *  Using isEqualNode check if 2 EntityNode having the same name of two DocumentType nodes 
 *  returned by parsing the same xml document are equal.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-isEqualNode">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-isEqualNode</a>
*/
public final class nodeisequalnode25 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodeisequalnode25(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      Document doc1;
      Document doc2;
      DocumentType docType1;
      DocumentType docType2;
      NamedNodeMap entitiesMap1;
      NamedNodeMap entitiesMap2;
      Entity alpha;
      Entity beta;
      boolean isEqual;
      doc1 = (Document) load("hc_staff", false);
      doc2 = (Document) load("hc_staff", false);
      docType1 = doc1.getDoctype();
      docType2 = doc2.getDoctype();
      entitiesMap1 = docType1.getEntities();
      entitiesMap2 = docType2.getEntities();
      alpha = (Entity) entitiesMap1.getNamedItem("delta");
      beta = (Entity) entitiesMap2.getNamedItem("delta");
      isEqual = alpha.isEqualNode(beta);
      assertTrue("nodeisequalnode25", isEqual);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodeisequalnode25";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodeisequalnode25.class, args);
   }
}

