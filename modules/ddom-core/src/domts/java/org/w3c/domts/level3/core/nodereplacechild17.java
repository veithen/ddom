
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
 *  Using replaceChild on a DocumentFragment node attempt to replace a Comment node with 
 *  a ProcessingInstruction and vice versa verify the data of the replaced nodes.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-785887307">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-785887307</a>
*/
public final class nodereplacechild17 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public nodereplacechild17(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
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
      DocumentFragment docFrag;
      ProcessingInstruction pi;
      Comment cmt;
      Comment replacedCmt;
      ProcessingInstruction replacedPi;
      String data;
      String target;
      Node appendedChild;
      doc = (Document) load("hc_staff", true);
      docFrag = doc.createDocumentFragment();
      cmt = doc.createComment("Comment");
      pi = doc.createProcessingInstruction("target", "Comment");
      appendedChild = docFrag.appendChild(pi);
      appendedChild = docFrag.appendChild(cmt);
      replacedCmt = (Comment) docFrag.replaceChild(pi, cmt);
      data = replacedCmt.getData();
      assertEquals("nodereplacechild17_1", "Comment", data);
      replacedPi = (ProcessingInstruction) docFrag.replaceChild(cmt, pi);
      target = replacedPi.getTarget();
      assertEquals("nodereplacechild17_2", "target", target);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/nodereplacechild17";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(nodereplacechild17.class, args);
   }
}
