
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
 *  Using removeChild on the first 'p' Element node attempt to remove a EntityReference 
 *  node child and verify the nodeName of the returned node that was removed.  Attempt
 *  to remove a non-child from an entity reference and expect either a NOT_FOUND_ERR or
 *  a NO_MODIFICATION_ALLOWED_ERR.  Renove a child from an entity reference and expect
 *  a NO_MODIFICATION_ALLOWED_ERR.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-1734834066">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-1734834066</a>
*/
public final class noderemovechild19 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public noderemovechild19(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

      org.w3c.domts.DocumentBuilderSetting[] settings = 
          new org.w3c.domts.DocumentBuilderSetting[] {
org.w3c.domts.DocumentBuilderSetting.notExpandEntityReferences
        };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);

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
      NodeList parentList;
      Element parent;
      EntityReference child;
      EntityReference removed;
      String removedName;
      Node removedNode;
      Node entRefChild;
      doc = (Document) load("hc_staff", true);
      parentList = doc.getElementsByTagName("acronym");
      parent = (Element) parentList.item(1);
      child = (EntityReference) parent.getFirstChild();
      removed = (EntityReference) parent.removeChild(child);
      removedName = removed.getNodeName();
      assertEquals("noderemovechild19", "beta", removedName);
      
      try {
      removedNode = child.removeChild(parent);
      fail("throw_DOMException");
     
      } catch (DOMException ex) {
           switch (ex.code) {
      case 7 : 
       break;
      case 8 : 
       break;
          default:
          throw ex;
          }
      } 
entRefChild = child.getFirstChild();
      
      if ((entRefChild != null)) {
          
      {
         boolean success = false;
         try {
            removedNode = child.removeChild(entRefChild);
          } catch (DOMException ex) {
            success = (ex.code == DOMException.NO_MODIFICATION_ALLOWED_ERR);
         }
         assertTrue("throw_NO_MODIFICATION_ALLOWED_ERR", success);
      }
}
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/noderemovechild19";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(noderemovechild19.class, args);
   }
}

