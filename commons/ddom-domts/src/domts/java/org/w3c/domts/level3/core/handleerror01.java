
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
 * Add two CDATASection containing "]]>" and call Node.normalize
 * with an error handler that stops processing.  Only one of the
 * CDATASections should be split.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-normalize">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-normalize</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-split-cdata-sections">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-split-cdata-sections</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ERRORS-DOMErrorHandler-handleError">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ERRORS-DOMErrorHandler-handleError</a>
*/
public final class handleerror01 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public handleerror01(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
    preload(contentType, "barfoo", true);
    }

     /**
      *    Inner class implementation for variable errorHandler 
      */
      private static class DOMErrorHandlerN10054
           extends org.w3c.domts.DOMTestInnerClass 
           implements DOMErrorHandler {
       /**
        * Constructor
        * @param test test case
        */
        public DOMErrorHandlerN10054(DOMTestCase test) { 
        super(test);
           }
   
        /**
         *    
This method is called on the error handler when an error occurs.
If an exception is thrown from this method, it is considered to be equivalent of returningtrue.

         * @param error The error object that describes the error. This object may be reused by the DOM implementation across multiple calls to thehandleErrormethod.
         * @return If thehandleErrormethod returnsfalse, the DOM implementation should stop the current processing when possible. If the method returnstrue, the processing may continue depending onDOMError.severity.
         */
         public boolean handleError(DOMError error) {
                return false;
      }
}

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Element elem;
      DOMConfiguration domConfig;
      NodeList elemList;
      CDATASection newChild;
      Node oldChild;
      Node child;
      String childValue;
      int childType;
      Node retval;
      java.util.List errors = new java.util.ArrayList();
      
      DOMErrorHandler errorHandler = new DOMErrorHandlerN10054(this);
      doc = (Document) load("barfoo", true);
      elemList = doc.getElementsByTagName("p");
      elem = (Element) elemList.item(0);
      oldChild = elem.getFirstChild();
      newChild = doc.createCDATASection("this is not ]]> good");
      retval = elem.replaceChild(newChild, oldChild);
      newChild = doc.createCDATASection("this is not ]]> bad");
      retval = elem.appendChild(newChild);
      domConfig = doc.getDomConfig();
      domConfig.setParameter("split-cdata-sections", Boolean.TRUE);
      domConfig.setParameter("error-handler", ((Object) /*DOMErrorHandler */errorHandler));
      doc.normalizeDocument();
      elemList = doc.getElementsByTagName("p");
      elem = (Element) elemList.item(0);
      child = elem.getLastChild();
      childValue = child.getNodeValue();
      
      if (equals("this is not ]]> bad", childValue)) {
          childType = (int) child.getNodeType();
      assertEquals("lastChildCDATA", 4, childType);
      child = elem.getFirstChild();
      childValue = child.getNodeValue();
      assertNotEquals("firstChildNotIntact", "this is not ]]> good", childValue);
} else {
          child = elem.getFirstChild();
      childValue = child.getNodeValue();
      assertEquals("firstChildIntact", "this is not ]]> good", childValue);
      }
        
    }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/handleerror01";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(handleerror01.class, args);
   }
}
