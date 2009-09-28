
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
 *  The item method of the DOMStringList Returns the indexth item in the collection. 
 *  If index is greater than or equal to the number of DOMStrings in the list, this returns null.
 *  
 *  Invoke the first item on the list of parameters returned by the DOMConfiguration object and
 *  make sure it is not null.  Then invoke the 100th item and verify that null is returned.
* @author IBM
* @author Jenny Hsu
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#DOMStringList-item">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#DOMStringList-item</a>
*/
public final class domstringlistitem02 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public domstringlistitem02(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "hc_staff", false);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      DOMStringList paramList;
      DOMConfiguration domConfig;
      int listSize;
      String retStr;
      doc = (Document) load("hc_staff", false);
      domConfig = doc.getDomConfig();
      paramList = domConfig.getParameterNames();
      retStr = paramList.item(0);
      assertNotNull("domstringlistitem02_notNull", retStr);
      retStr = paramList.item(100);
      assertNull("domstringlistitem02_null", retStr);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/domstringlistitem02";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(domstringlistitem02.class, args);
   }
}

