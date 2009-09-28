
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
 *  Set the strictErrorChecking attribute value on this documentNode to false and then to true.
 *  Call the createAttributeNS method on this document with an illegal character in the qualifiedName
 *  and check if the INVALID_CHARACTER_ERR is thrown.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-strictErrorChecking">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-strictErrorChecking</a>
*/
public final class documentsetstricterrorchecking01 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public documentsetstricterrorchecking01(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      Attr newAttr;
      doc = (Document) load("hc_staff", true);
      doc.setStrictErrorChecking(false);
      doc.setStrictErrorChecking(true);
      
      {
         boolean success = false;
         try {
            newAttr = doc.createAttributeNS("http://www.w3.org/DOM/Test", "@");
          } catch (DOMException ex) {
            success = (ex.code == DOMException.INVALID_CHARACTER_ERR);
         }
         assertTrue("INVALID_CHARACTER_ERR_documentsetstricterrorchecking01", success);
      }
}
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/documentsetstricterrorchecking01";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(documentsetstricterrorchecking01.class, args);
   }
}

