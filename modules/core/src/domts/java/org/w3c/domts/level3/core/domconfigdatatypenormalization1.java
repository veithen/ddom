
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
 * Checks behavior of "datatype-normalization" configuration parameter.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-datatype-normalization">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#parameter-datatype-normalization</a>
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#DOMConfiguration">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#DOMConfiguration</a>
*/
public final class domconfigdatatypenormalization1 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    */
   public domconfigdatatypenormalization1(final DOMTestDocumentBuilderFactory factory)  {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      DOMImplementation domImpl;
      Document doc;
      DOMConfiguration domConfig;
      DocumentType nullDocType = null;

      boolean canSet;
      boolean state;
      String parameter = "dAtAtype-normalization";
      domImpl = getImplementation();
      doc = domImpl.createDocument("http://www.w3.org/1999/xhtml", "html", nullDocType);
      domConfig = doc.getDomConfig();
      state = ((Boolean) domConfig.getParameter(parameter)).booleanValue();
      assertFalse("defaultFalse", state);
canSet = domConfig.canSetParameter(parameter, Boolean.FALSE);
      assertTrue("canSetFalse", canSet);
      canSet = domConfig.canSetParameter(parameter, Boolean.TRUE);
      
      if (canSet) {
          domConfig.setParameter(parameter, Boolean.TRUE);
      state = ((Boolean) domConfig.getParameter(parameter)).booleanValue();
      assertTrue("setTrueEffective", state);
      } else {
          
      {
         boolean success = false;
         try {
            domConfig.setParameter(parameter, Boolean.TRUE);
          } catch (DOMException ex) {
            success = (ex.code == DOMException.NOT_SUPPORTED_ERR);
         }
         assertTrue("throw_NOT_SUPPORTED_ERR", success);
      }
state = ((Boolean) domConfig.getParameter(parameter)).booleanValue();
      assertFalse("setTrueNotEffective", state);
}
        
    domConfig.setParameter(parameter, Boolean.FALSE);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/domconfigdatatypenormalization1";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(domconfigdatatypenormalization1.class, args);
   }
}

