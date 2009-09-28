
/*
This Java source file was generated by test-to-java.xsl
and is a derived work from the source document.
The source document contained the following notice:



Copyright (c) 2004 World Wide Web Consortium, 
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
 * Normalize document with two DOM L1 nodes.
 * Use an error handler to continue from errors and check that more than one
 * error was reported.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Document3-normalizeDocument</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-namespaces">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-namespaces</a>
* @see <a href="http://www.w3.org/TR/2003/WD-charmod-20030822/">http://www.w3.org/TR/2003/WD-charmod-20030822/</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ERRORS-DOMErrorHandler-handleError">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-ERRORS-DOMErrorHandler-handleError</a>
*/
public final class handleerror02 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public handleerror02(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
      private static class DOMErrorHandlerN10053
           extends org.w3c.domts.DOMTestInnerClass 
           implements DOMErrorHandler {
        /**
        * local copy of variable specified in value parameter
        */
        private java.util.List errors;
        /**
        * Constructor
        * @param test test case
        * @param errors Value from value attribute of nested var element
        */
        public DOMErrorHandlerN10053(DOMTestCase test, java.util.List errors) { 
        super(test);
           this.errors = errors;
           }
   
        /**
         *    
This method is called on the error handler when an error occurs.
If an exception is thrown from this method, it is considered to be equivalent of returningtrue.

         * @param error The error object that describes the error. This object may be reused by the DOM implementation across multiple calls to thehandleErrormethod.
         * @return If thehandleErrormethod returnsfalse, the DOM implementation should stop the current processing when possible. If the method returnstrue, the processing may continue depending onDOMError.severity.
         */
         public boolean handleError(DOMError error) {
        int severity;
      severity = (int) error.getSeverity();
      
      if (equals(2, severity)) {
          errors.add(error);
      }
            return true;
      }
}

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Element docElem;
      DOMConfiguration domConfig;
      NodeList pList;
      Element pElem;
      Text text;
      String textValue;
      Node retval;
      Element brElem;
      java.util.List errors = new java.util.ArrayList();
      
      DOMErrorHandler errorHandler = new DOMErrorHandlerN10053(this, errors);
      doc = (Document) load("barfoo", true);
      domConfig = doc.getDomConfig();
      domConfig.setParameter("error-handler", ((Object) /*DOMErrorHandler */errorHandler));
      pList = doc.getElementsByTagName("p");
      pElem = (Element) pList.item(0);
      brElem = doc.createElement("br");
      retval = pElem.appendChild(brElem);
      brElem = doc.createElement("br");
      retval = pElem.appendChild(brElem);
      doc.normalizeDocument();
      assertSize("twoErrors", 2, errors);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/handleerror02";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(handleerror02.class, args);
   }
}

