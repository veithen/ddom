
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
 *  The adoptNode method changes the ownerDocument of a node, its children, as well as the 
 *  attached attribute nodes if there are any. If the node has a parent it is first removed 
 *  from its parent child list. 
 *  For Element Nodes, specified attribute nodes of the source element are adopted, Default 
 *  attributes are discarded and descendants of the source element are recursively adopted. 
 *  Invoke the adoptNode method on a new document with the first code element node of this
 *  Document as the source.  Verify if the node has been adopted correctly by checking the 
 *  length of the this elements childNode list before and after.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-adoptNode">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-adoptNode</a>
*/
public final class documentadoptnode24 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public documentadoptnode24(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Document newDoc;
      DOMImplementation domImpl;
      NodeList childList;
      Node adoptedNode;
      Element codeElem;
      NodeList codeElemChildren;
      NodeList adoptedChildren;
      int codeElemLen;
      int adoptedLen;
      DocumentType nullDocType = null;

      doc = (Document) load("hc_staff", false);
      domImpl = doc.getImplementation();
      newDoc = domImpl.createDocument("http://www.w3.org/DOM/Test", "dom:test", nullDocType);
      childList = doc.getElementsByTagNameNS("*", "code");
      codeElem = (Element) childList.item(0);
      adoptedNode = newDoc.adoptNode(codeElem);
      codeElemChildren = codeElem.getChildNodes();
      adoptedChildren = adoptedNode.getChildNodes();
      codeElemLen = (int) codeElemChildren.getLength();
      adoptedLen = (int) adoptedChildren.getLength();
      assertEquals("documentadoptnode24", adoptedLen, codeElemLen);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/documentadoptnode24";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(documentadoptnode24.class, args);
   }
}
