
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
 *  Invoke setUserData on this Document to set this Documents UserData to a new
 *  Element node and using getUserData and isEqualNode check if the returned 
 *  UserData object is the same as the object that was set.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-getUserData">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-getUserData</a>
*/
public final class nodegetuserdata03 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodegetuserdata03(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      Object userData;
      Object retUserData;
      boolean success;
      Element elem;
      Object returnedUserData;
      UserDataHandler nullHandler = null;

      doc = (Document) load("barfoo", false);
      elem = doc.createElementNS("http://www.w3.org/1999/xhtml", "body");
      returnedUserData = doc.setUserData("something", ((Object) /*Node */elem), nullHandler);
      retUserData = doc.getUserData("something");
      success = ((Node) /*DOMUserData */retUserData).isEqualNode(elem);
      assertTrue("nodegetuserdata03", success);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodegetuserdata03";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodegetuserdata03.class, args);
   }
}
