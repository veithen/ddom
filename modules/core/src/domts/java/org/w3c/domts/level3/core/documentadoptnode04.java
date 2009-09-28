
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
 *  Invoke adoptNode on a new document to adopt a new namespace aware attribute node created by 
 *  this document.  Check if this attribute has been adopted successfully by verifying the nodeName, 
 *  namespaceURI, prefix, specified and ownerElement attributes of the adopted node.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-adoptNode">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-adoptNode</a>
*/
public final class documentadoptnode04 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public documentadoptnode04(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      Document newDoc;
      DOMImplementation domImpl;
      Attr newAttr;
      Attr adoptedAttr;
      String nodeName;
      String nodeNamespaceURI;
      String nodePrefix;
      Element attrOwnerElem;
      boolean isSpecified;
      DocumentType nullDocType = null;

      Element docElem;
      String rootNS;
      String rootName;
      String xmlNS = "http://www.w3.org/XML/1998/namespace";
      doc = (Document) load("hc_staff", true);
      docElem = doc.getDocumentElement();
      rootName = docElem.getTagName();
      rootNS = docElem.getNamespaceURI();
      domImpl = doc.getImplementation();
      newDoc = domImpl.createDocument(rootNS, rootName, nullDocType);
      newAttr = doc.createAttributeNS(xmlNS, "xml:lang");
      adoptedAttr = (Attr) newDoc.adoptNode(newAttr);
      
      if ((adoptedAttr != null)) {
          nodeName = adoptedAttr.getNodeName();
      nodeNamespaceURI = adoptedAttr.getNamespaceURI();
      nodePrefix = adoptedAttr.getPrefix();
      attrOwnerElem = adoptedAttr.getOwnerElement();
      isSpecified = adoptedAttr.getSpecified();
      assertEquals("documentadoptnode04_nodeName", "xml:lang", nodeName);
      assertEquals("documentadoptnode04_namespaceURI", xmlNS, nodeNamespaceURI);
      assertEquals("documentadoptnode04_prefix", "xml", nodePrefix);
      assertNull("documentadoptnode04_ownerDoc", attrOwnerElem);
      assertTrue("documentadoptnode04_specified", isSpecified);
      }
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/documentadoptnode04";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(documentadoptnode04.class, args);
   }
}

