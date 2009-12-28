
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
 * Call setUserData on a node providing a UserDataHandler and rename the node.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-handleUserDataEvent">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#ID-handleUserDataEvent</a>
*/
public final class userdatahandler01 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public userdatahandler01(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {
      super(factory);

    //
    //   check if loaded documents are supported for content type
    //
    String contentType = getContentType();
    preload(contentType, "barfoo", true);
    }

   /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
   public void runTest() throws Throwable {
      Document doc;
      Node node;
      NodeList pList;
      org.w3c.domts.UserDataMonitor userDataMonitor = new org.w3c.domts.UserDataMonitor();
      
      Object oldUserData;
      String elementNS;
      Node newNode;
      java.util.List notifications = new java.util.ArrayList();
      
      org.w3c.domts.UserDataNotification notification;
      short operation;
      String key;
      String data;
      Node src;
      Node dst;
      int greetingCount = 0;
      int salutationCount = 0;
      String hello = "Hello";
      String mister = "Mr.";
      doc = (Document) load("barfoo", true);
      pList = doc.getElementsByTagName("p");
      node = pList.item(0);
      oldUserData = node.setUserData("greeting", ((Object) /*DOMString */hello), ((UserDataHandler) /*UserDataMonitor */userDataMonitor));
      oldUserData = node.setUserData("salutation", ((Object) /*DOMString */mister), ((UserDataHandler) /*UserDataMonitor */userDataMonitor));
      elementNS = node.getNamespaceURI();
      newNode = doc.renameNode(node, elementNS, "div");
      notifications = userDataMonitor.getAllNotifications();
assertSize("twoNotifications", 2, notifications);
      for (int indexN1009E = 0; indexN1009E < notifications.size(); indexN1009E++) {
          notification = (org.w3c.domts.UserDataNotification) notifications.get(indexN1009E);
    operation = notification.getOperation();
assertEquals("operationIsRename", 4, operation);
      key = notification.getKey();
data = (String) notification.getData();

      if (equals("greeting", key)) {
          assertEquals("greetingDataHello", hello, data);
      greetingCount += 1;
      } else {
          assertEquals("saluationKey", "salutation", key);
      assertEquals("salutationDataMr", mister, data);
      salutationCount += 1;
      }
        
    src = notification.getSrc();
assertSame("srcIsNode", node, src);
dst = notification.getDst();

      if ((dst == null)) {
          assertSame("ifDstNullRenameMustReuseNode", node, newNode);
} else {
          assertSame("dstIsNewNode", newNode, dst);
}
        
      }
      assertEquals("greetingCountIs1", 1, greetingCount);
      assertEquals("salutationCountIs1", 1, salutationCount);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/userdatahandler01";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(userdatahandler01.class, args);
   }
}
