
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
 *  Invoke setUserData on a new Attr to set its UserData to two Document nodes
 *  obtained by parsing the same xml document.  Using getUserData and isNodeEqual 
 *  verify if the returned nodes are Equal.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-setUserData">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Node3-setUserData</a>
*/
public final class nodesetuserdata05 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodesetuserdata05(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "hc_staff", true);
    preload(contentType, "hc_staff", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Document doc2;
      Object userData;
      Object returned1;
      Object returned2;
      Object retUserData;
      boolean success;
      Attr attr;
      UserDataHandler nullHandler = null;

      doc = (Document) load("hc_staff", true);
      doc2 = (Document) load("hc_staff", true);
      attr = doc.createAttributeNS("http://www.w3.org/XML/1998/namespace", "lang");
      retUserData = attr.setUserData("Key1", ((Object) /*Node */doc), nullHandler);
      retUserData = attr.setUserData("Key2", ((Object) /*Node */doc2), nullHandler);
      returned1 = attr.getUserData("Key1");
      returned2 = attr.getUserData("Key2");
      success = ((Node) /*DOMUserData */returned1).isEqualNode(((Node) /*DOMUserData */returned2));
      assertTrue("nodesetuserdata05", success);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodesetuserdata05";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodesetuserdata05.class, args);
   }
}

