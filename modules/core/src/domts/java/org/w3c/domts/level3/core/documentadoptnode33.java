
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
 *  Invoke the adoptNode method on this document using a new CDataSection node created in a new
 *  Document as the source.  Verify if the node has been adopted correctly by checking the nodeValue 
 *  of the adopted node.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-adoptNode">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-adoptNode</a>
*/
public final class documentadoptnode33 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public documentadoptnode33(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      DOMImplementation domImpl;
      Document newDoc;
      Node newCDATA;
      Node adoptedCDATA;
      String nodeValue;
      DocumentType nullDocType = null;

      Element docElem;
      String rootNS;
      String rootName;
      doc = (Document) load("hc_staff", true);
      docElem = doc.getDocumentElement();
      rootNS = docElem.getNamespaceURI();
      rootName = docElem.getTagName();
      domImpl = doc.getImplementation();
      newDoc = domImpl.createDocument(rootNS, rootName, nullDocType);
      newCDATA = newDoc.createCDATASection("Document.adoptNode test for a CDATASECTION_NODE");
      adoptedCDATA = doc.adoptNode(newCDATA);
      
      if ((adoptedCDATA != null)) {
          nodeValue = adoptedCDATA.getNodeValue();
      assertEquals("documentadoptnode33", "Document.adoptNode test for a CDATASECTION_NODE", nodeValue);
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/documentadoptnode33";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(documentadoptnode33.class, args);
   }
}

