
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
 * Create a document with an XML 1.1 valid but XML 1.0 invalid attribute and
 * normalize document with well-formed set to false.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-well-formed">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-well-formed</a>
*/
public final class wellformed04 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    */
   public wellformed04(final DOMTestDocumentBuilderFactory factory)  {
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
      DocumentType nullDoctype = null;

      Document doc;
      Element docElem;
      Attr attr;
      Node retval;
      DOMConfiguration domConfig;
      org.w3c.domts.DOMErrorMonitor errorMonitor = new org.w3c.domts.DOMErrorMonitor();
      
      java.util.List errors = new java.util.ArrayList();
      
      DOMError error;
      boolean canSet;
      String nullNS = null;

      domImpl = getImplementation();
      doc = domImpl.createDocument("http://www.w3.org/1999/xhtml", "html", nullDoctype);
      docElem = doc.getDocumentElement();
      
      {
         boolean success = false;
         try {
            attr = doc.createAttributeNS(nullNS, "LegalNameࢎ");
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
docElem.setAttributeNS(nullNS, "LegalNameࢎ", "foo");
      doc.setXmlVersion("1.0");
      domConfig = doc.getDomConfig();
      canSet = domConfig.canSetParameter("well-formed", Boolean.FALSE);
      
      if (canSet) {
          domConfig.setParameter("well-formed", Boolean.FALSE);
      domConfig.setParameter("error-handler", ((Object) /*DOMErrorMonitor */errorMonitor));
      doc.normalizeDocument();
      errors = errorMonitor.getAllErrors();
for (int indexN100AA = 0; indexN100AA < errors.size(); indexN100AA++) {
          error = (DOMError) errors.get(indexN100AA);
    assertNull("noErrorsExpected", error);
        }
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/wellformed04";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(wellformed04.class, args);
   }
}
