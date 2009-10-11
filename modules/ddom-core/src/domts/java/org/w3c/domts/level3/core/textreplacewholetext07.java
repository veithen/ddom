
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
 * Append an entity reference and a text node after to the content of the
 * first strong element.  Then call replaceWholeText on initial content
 * of that element.  Since the entity reference does not contain any 
 * logically-adjacent text content, only the initial text element should
 * be replaced. 
* @author IBM
* @author Neil Delima
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Text3-replaceWholeText">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#Text3-replaceWholeText</a>
* @see <a href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=425">http://www.w3.org/Bugs/Public/show_bug.cgi?id=425</a>
*/
public final class textreplacewholetext07 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public textreplacewholetext07(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

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
    preload(contentType, "hc_staff", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      NodeList itemList;
      Element elementName;
      Text textNode;
      EntityReference erefNode;
      Text replacedText;
      Node appendedChild;
      Node node;
      String nodeValue;
      int nodeType;
      doc = (Document) load("hc_staff", true);
      itemList = doc.getElementsByTagName("strong");
      elementName = (Element) itemList.item(0);
      erefNode = doc.createEntityReference("ent4");
      textNode = doc.createTextNode("New Text");
      appendedChild = elementName.appendChild(erefNode);
      appendedChild = elementName.appendChild(textNode);
      textNode = (Text) elementName.getFirstChild();
      replacedText = textNode.replaceWholeText("New Text and Cdata");
      textNode = (Text) elementName.getFirstChild();
      assertSame("retval_same", textNode, replacedText);
nodeValue = textNode.getNodeValue();
      assertEquals("nodeValueSame", "New Text and Cdata", nodeValue);
      node = textNode.getNextSibling();
      assertNotNull("secondChildNotNull", node);
      nodeType = (int) node.getNodeType();
      assertEquals("secondChildIsEntRef", 5, nodeType);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/textreplacewholetext07";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(textreplacewholetext07.class, args);
   }
}
