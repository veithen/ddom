
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
 * Create a document with an XML 1.1 valid but XML 1.0 invalid element and
 * normalize document with well-formed set to true.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-well-formed">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-well-formed</a>
*/
public final class wellformed01 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    */
   public wellformed01(final DOMTestDocumentBuilderFactory factory)  {
      super(factory);

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
      DOMImplementation domImpl;
      String nullString = null;

      DocumentType nullDoctype = null;

      Document doc;
      Element elem;
      Node retval;
      DOMConfiguration domConfig;
      org.w3c.domts.DOMErrorMonitor errorMonitor = new org.w3c.domts.DOMErrorMonitor();
      
      java.util.List errors = new java.util.ArrayList();
      
      DOMError error;
      int severity;
      String type;
      DOMLocator locator;
      Node relatedNode;
      domImpl = getImplementation();
      doc = domImpl.createDocument(nullString, nullString, nullDoctype);
      
      {
         boolean success = false;
         try {
            elem = doc.createElementNS("http://www.example.org/domts/wellformed01", "LegalNameࢎ");
          } catch (DOMException ex) {
            success = (ex.code == DOMException.INVALID_CHARACTER_ERR);
         }
         assertTrue("xml10InvalidName", success);
      }

      try {
      doc.setXmlVersion("1.1");
      
      } catch (DOMException ex) {
           switch (ex.code) {
      case 9 : 
               return ;
          default:
          throw ex;
          }
      } 
elem = doc.createElementNS("http://www.example.org/domts/wellformed01", "LegalNameࢎ");
      retval = doc.appendChild(elem);
      doc.setXmlVersion("1.0");
      domConfig = doc.getDomConfig();
      domConfig.setParameter("well-formed", Boolean.TRUE);
      domConfig.setParameter("error-handler", ((Object) /*DOMErrorMonitor */errorMonitor));
      doc.normalizeDocument();
      errors = errorMonitor.getAllErrors();
for (int indexN100A9 = 0; indexN100A9 < errors.size(); indexN100A9++) {
          error = (DOMError) errors.get(indexN100A9);
    severity = (int) error.getSeverity();
      assertEquals("severity", 2, severity);
      type = error.getType();
      assertEquals("type", "wf-invalid-character-in-node-name", type);
      locator = error.getLocation();
      relatedNode = locator.getRelatedNode();
      assertSame("relatedNode", elem, relatedNode);
  }
      assertSize("oneError", 1, errors);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/wellformed01";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(wellformed01.class, args);
   }
}

