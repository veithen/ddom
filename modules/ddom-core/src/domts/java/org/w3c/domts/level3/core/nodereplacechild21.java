
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
 *  The method replaceChild replaces the child node oldChild with newChild in the list of 
 *  children, and returns the oldChild node.
 *  Using replaceChild on this DocumentType node attempt to replace an Entity node with
 *  a notation node of retieved from the DTD of another document and verify if a
 *  NO_MODIFICATION_ALLOWED_ERR is thrown since DocumentType node is read-only.
 *  Also try replacing the docType with an entity node and see if the same exception gets thrown.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-785887307">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-785887307</a>
*/
public final class nodereplacechild21 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodereplacechild21(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

      org.w3c.domts.DocumentBuilderSetting[] settings = 
          new org.w3c.domts.DocumentBuilderSetting[] {
org.w3c.domts.DocumentBuilderSetting.namespaceAware
        };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);

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
      Document doc;
      DocumentType docType;
      NamedNodeMap entitiesMap;
      Entity ent;
      Document doc1;
      DocumentType docType1;
      NamedNodeMap notationsMap;
      Notation notation;
      Node replacedChild;
      doc = (Document) load("hc_staff", false);
      docType = doc.getDoctype();
      entitiesMap = docType.getEntities();
      ent = (Entity) entitiesMap.getNamedItem("alpha");
      doc1 = (Document) load("hc_staff", false);
      docType1 = doc1.getDoctype();
      notationsMap = docType1.getNotations();
      notation = (Notation) notationsMap.getNamedItem("notation1");
      
      {
         boolean success = false;
         try {
            replacedChild = docType.replaceChild(notation, ent);
          } catch (DOMException ex) {
            success = (ex.code == DOMException.NO_MODIFICATION_ALLOWED_ERR);
         }
         assertTrue("NO_MODIFICATION_ALLOWED_ERR1_nodereplacechild21", success);
      }

      {
         boolean success = false;
         try {
            replacedChild = docType.replaceChild(ent, docType);
          } catch (DOMException ex) {
            success = (ex.code == DOMException.NO_MODIFICATION_ALLOWED_ERR);
         }
         assertTrue("NO_MODIFICATION_ALLOWED_ERR2_nodereplacechild21", success);
      }
}
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodereplacechild21";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodereplacechild21.class, args);
   }
}
