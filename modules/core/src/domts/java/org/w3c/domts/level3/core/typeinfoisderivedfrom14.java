
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
 * Check how classType is derived from itself.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#TypeInfo-isDerivedFrom">http://www.w3.org/TR/2003/CR-DOM-Level-3-Core-20031107/core#TypeInfo-isDerivedFrom</a>
*/
public final class typeinfoisderivedfrom14 extends DOMTestCase {

   /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
   public typeinfoisderivedfrom14(final DOMTestDocumentBuilderFactory factory)  throws org.w3c.domts.DOMTestIncompatibleException {

      org.w3c.domts.DocumentBuilderSetting[] settings = 
          new org.w3c.domts.DocumentBuilderSetting[] {
org.w3c.domts.DocumentBuilderSetting.schemaValidating
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
      NodeList elemList;
      Element acronymElem;
      Attr attr;
      Element elem;
      String elemName;
      TypeInfo typeInfo;
      boolean isDerived;
      String typeName;
      doc = (Document) load("hc_staff", false);
      elemList = doc.getElementsByTagName("acronym");
      acronymElem = (Element) elemList.item(2);
      attr = acronymElem.getAttributeNode("class");
      typeInfo = attr.getSchemaTypeInfo();
      assertNotNull("typeInfoNotNull", typeInfo);
      typeName = typeInfo.getTypeName();
      assertEquals("name", "classType", typeName);
      isDerived = typeInfo.isDerivedFrom("http://www.w3.org/1999/xhtml", "classType", 1);
      assertTrue("derivedFromSelfRestriction", isDerived);
      isDerived = typeInfo.isDerivedFrom("http://www.w3.org/1999/xhtml", "classType", 14);
      assertFalse("notDerivedFromSelfOther", isDerived);
isDerived = typeInfo.isDerivedFrom("http://www.w3.org/1999/xhtml", "classType", 15);
      assertTrue("derivedFromSelfAll", isDerived);
      isDerived = typeInfo.isDerivedFrom("http://www.w3.org/1999/xhtml", "classType", 0);
      assertTrue("derivedFromSelfAny", isDerived);
      }
   /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/typeinfoisderivedfrom14";
   }
   /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
   public static void main(final String[] args) {
        DOMTestCase.doMain(typeinfoisderivedfrom14.class, args);
   }
}

