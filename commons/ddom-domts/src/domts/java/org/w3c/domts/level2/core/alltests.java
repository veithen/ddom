
/*
This Java source file was generated by test-to-java.xsl
and is a derived work from the source document.
The source document contained the following notice:


Copyright (c) 2001-2004 World Wide Web Consortium,
(Massachusetts Institute of Technology, Institut National de
Recherche en Informatique et en Automatique, Keio University). All
Rights Reserved. This program is distributed under the W3C's Software
Intellectual Property License. This program is distributed in the
hope that it will be useful, but WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.
See W3C License http://www.w3.org/Consortium/Legal/ for more details.

*/

package org.w3c.domts.level2.core;

import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestSuite;
import org.w3c.domts.DOMTestSink;
import org.w3c.domts.DOMTestDocumentBuilderFactory;


/**
 * 
* @author DOM Test Suite Project
*/
public class alltests extends DOMTestSuite {

    /**
    * Constructor
    * @param factory document factory, may not be null
    * @throws Exception Thrown if test is not compatible with settings
    */
   public alltests(DOMTestDocumentBuilderFactory factory) throws Exception {
      super(factory);

      setFactory(factory);
   }

   /**
    *   Build test suite by adding each test to the test sink
    *   @param sink test sink
    */
   public void build(DOMTestSink sink) {
      sink.addTest(attrgetownerelement01.class);
      sink.addTest(attrgetownerelement02.class);
      sink.addTest(attrgetownerelement03.class);
      sink.addTest(attrgetownerelement04.class);
      sink.addTest(attrgetownerelement05.class);
      sink.addTest(createAttributeNS01.class);
      sink.addTest(createAttributeNS02.class);
      sink.addTest(createAttributeNS03.class);
      sink.addTest(createAttributeNS04.class);
      sink.addTest(createAttributeNS05.class);
      sink.addTest(createAttributeNS06.class);
      sink.addTest(createDocument01.class);
      sink.addTest(createDocument02.class);
      sink.addTest(createDocument03.class);
      sink.addTest(createDocument04.class);
      sink.addTest(createDocument05.class);
      sink.addTest(createDocument06.class);
      sink.addTest(createDocument07.class);
      sink.addTest(createDocument08.class);
      sink.addTest(createDocumentType01.class);
      sink.addTest(createDocumentType02.class);
      sink.addTest(createDocumentType03.class);
      sink.addTest(createDocumentType04.class);
      sink.addTest(createElementNS01.class);
      sink.addTest(createElementNS02.class);
      sink.addTest(createElementNS03.class);
      sink.addTest(createElementNS04.class);
      sink.addTest(createElementNS05.class);
      sink.addTest(documentcreateattributeNS01.class);
      sink.addTest(documentcreateattributeNS02.class);
      sink.addTest(documentcreateattributeNS03.class);
      sink.addTest(documentcreateattributeNS04.class);
      sink.addTest(documentcreateattributeNS05.class);
      sink.addTest(documentcreateattributeNS06.class);
      sink.addTest(documentcreateattributeNS07.class);
      sink.addTest(documentcreateelementNS01.class);
      sink.addTest(documentcreateelementNS02.class);
      sink.addTest(documentcreateelementNS05.class);
      sink.addTest(documentcreateelementNS06.class);
      sink.addTest(documentgetelementbyid01.class);
      sink.addTest(documentgetelementsbytagnameNS01.class);
      sink.addTest(documentgetelementsbytagnameNS02.class);
      sink.addTest(documentgetelementsbytagnameNS03.class);
      sink.addTest(documentgetelementsbytagnameNS04.class);
      sink.addTest(documentgetelementsbytagnameNS05.class);
      sink.addTest(documentimportnode01.class);
      sink.addTest(documentimportnode02.class);
      sink.addTest(documentimportnode03.class);
      sink.addTest(documentimportnode04.class);
      sink.addTest(documentimportnode05.class);
      sink.addTest(documentimportnode06.class);
      sink.addTest(documentimportnode07.class);
      sink.addTest(documentimportnode08.class);
      sink.addTest(documentimportnode09.class);
      sink.addTest(documentimportnode10.class);
      sink.addTest(documentimportnode11.class);
      sink.addTest(documentimportnode12.class);
      sink.addTest(documentimportnode13.class);
      sink.addTest(documentimportnode14.class);
      sink.addTest(documentimportnode15.class);
      sink.addTest(documentimportnode17.class);
      sink.addTest(documentimportnode18.class);
      sink.addTest(documentimportnode19.class);
      sink.addTest(documentimportnode20.class);
      sink.addTest(documentimportnode21.class);
      sink.addTest(documentimportnode22.class);
      sink.addTest(documenttypeinternalSubset01.class);
      sink.addTest(documenttypepublicid01.class);
      sink.addTest(documenttypesystemid01.class);
      sink.addTest(domimplementationcreatedocument03.class);
      sink.addTest(domimplementationcreatedocument04.class);
      sink.addTest(domimplementationcreatedocument05.class);
      sink.addTest(domimplementationcreatedocument07.class);
      sink.addTest(domimplementationcreatedocumenttype01.class);
      sink.addTest(domimplementationcreatedocumenttype02.class);
      sink.addTest(domimplementationcreatedocumenttype04.class);
      sink.addTest(domimplementationfeaturecore.class);
      sink.addTest(domimplementationfeaturexmlversion2.class);
      sink.addTest(domimplementationhasfeature01.class);
      sink.addTest(domimplementationhasfeature02.class);
      sink.addTest(elementgetattributenodens01.class);
      sink.addTest(elementgetattributenodens02.class);
      sink.addTest(elementgetattributenodens03.class);
      sink.addTest(elementgetattributens02.class);
      sink.addTest(elementgetelementsbytagnamens02.class);
      sink.addTest(elementgetelementsbytagnamens04.class);
      sink.addTest(elementgetelementsbytagnamens05.class);
      sink.addTest(elementhasattribute01.class);
      sink.addTest(elementhasattribute02.class);
      sink.addTest(elementhasattribute03.class);
      sink.addTest(elementhasattribute04.class);
      sink.addTest(elementhasattributens01.class);
      sink.addTest(elementhasattributens02.class);
      sink.addTest(elementhasattributens03.class);
      sink.addTest(elementremoveattributens01.class);
      sink.addTest(elementsetattributenodens01.class);
      sink.addTest(elementsetattributenodens02.class);
      sink.addTest(elementsetattributenodens03.class);
      sink.addTest(elementsetattributenodens04.class);
      sink.addTest(elementsetattributenodens05.class);
      sink.addTest(elementsetattributenodens06.class);
      sink.addTest(elementsetattributens01.class);
      sink.addTest(elementsetattributens02.class);
      sink.addTest(elementsetattributens03.class);
      sink.addTest(elementsetattributens04.class);
      sink.addTest(elementsetattributens05.class);
      sink.addTest(elementsetattributens08.class);
      sink.addTest(elementsetattributensurinull.class);
      sink.addTest(getAttributeNS01.class);
      sink.addTest(getAttributeNS02.class);
      sink.addTest(getAttributeNS03.class);
      sink.addTest(getAttributeNS04.class);
      sink.addTest(getAttributeNS05.class);
      sink.addTest(getAttributeNodeNS01.class);
      sink.addTest(getAttributeNodeNS02.class);
      sink.addTest(getElementById01.class);
      sink.addTest(getElementById02.class);
      sink.addTest(getElementsByTagNameNS01.class);
      sink.addTest(getElementsByTagNameNS02.class);
      sink.addTest(getElementsByTagNameNS03.class);
      sink.addTest(getElementsByTagNameNS04.class);
      sink.addTest(getElementsByTagNameNS05.class);
      sink.addTest(getElementsByTagNameNS06.class);
      sink.addTest(getElementsByTagNameNS07.class);
      sink.addTest(getElementsByTagNameNS08.class);
      sink.addTest(getElementsByTagNameNS09.class);
      sink.addTest(getElementsByTagNameNS10.class);
      sink.addTest(getElementsByTagNameNS11.class);
      sink.addTest(getElementsByTagNameNS12.class);
      sink.addTest(getElementsByTagNameNS13.class);
      sink.addTest(getElementsByTagNameNS14.class);
      sink.addTest(getNamedItemNS01.class);
      sink.addTest(getNamedItemNS02.class);
      sink.addTest(getNamedItemNS03.class);
      sink.addTest(getNamedItemNS04.class);
      sink.addTest(hasAttribute01.class);
      sink.addTest(hasAttribute02.class);
      sink.addTest(hasAttribute03.class);
      sink.addTest(hasAttribute04.class);
      sink.addTest(hasAttributeNS01.class);
      sink.addTest(hasAttributeNS02.class);
      sink.addTest(hasAttributeNS03.class);
      sink.addTest(hasAttributeNS04.class);
      sink.addTest(hasAttributeNS05.class);
      sink.addTest(hasAttributes01.class);
      sink.addTest(hasAttributes02.class);
      sink.addTest(hc_entitiesremovenameditemns1.class);
      sink.addTest(hc_entitiessetnameditemns1.class);
      sink.addTest(hc_namednodemapinvalidtype1.class);
      sink.addTest(hc_nodedocumentfragmentnormalize1.class);
      sink.addTest(hc_nodedocumentfragmentnormalize2.class);
      sink.addTest(hc_notationsremovenameditemns1.class);
      sink.addTest(hc_notationssetnameditemns1.class);
      sink.addTest(importNode01.class);
      sink.addTest(importNode02.class);
      sink.addTest(importNode03.class);
      sink.addTest(importNode04.class);
      sink.addTest(importNode05.class);
      sink.addTest(importNode06.class);
      sink.addTest(importNode07.class);
      sink.addTest(importNode08.class);
      sink.addTest(importNode09.class);
      sink.addTest(importNode10.class);
      sink.addTest(importNode11.class);
      sink.addTest(importNode12.class);
      sink.addTest(importNode13.class);
      sink.addTest(importNode14.class);
      sink.addTest(importNode15.class);
      sink.addTest(importNode16.class);
      sink.addTest(importNode17.class);
      sink.addTest(internalSubset01.class);
      sink.addTest(isSupported01.class);
      sink.addTest(isSupported02.class);
      sink.addTest(isSupported04.class);
      sink.addTest(isSupported05.class);
      sink.addTest(isSupported06.class);
      sink.addTest(isSupported07.class);
      sink.addTest(isSupported09.class);
      sink.addTest(isSupported10.class);
      sink.addTest(isSupported11.class);
      sink.addTest(isSupported12.class);
      sink.addTest(isSupported13.class);
      sink.addTest(isSupported14.class);
      sink.addTest(localName01.class);
      sink.addTest(localName02.class);
      sink.addTest(localName03.class);
      sink.addTest(localName04.class);
      sink.addTest(namednodemapgetnameditemns01.class);
      sink.addTest(namednodemapgetnameditemns02.class);
      sink.addTest(namednodemapgetnameditemns03.class);
      sink.addTest(namednodemapgetnameditemns04.class);
      sink.addTest(namednodemapgetnameditemns05.class);
      sink.addTest(namednodemapgetnameditemns06.class);
      sink.addTest(namednodemapremovenameditemns01.class);
      sink.addTest(namednodemapremovenameditemns02.class);
      sink.addTest(namednodemapremovenameditemns03.class);
      sink.addTest(namednodemapremovenameditemns04.class);
      sink.addTest(namednodemapremovenameditemns05.class);
      sink.addTest(namednodemapremovenameditemns06.class);
      sink.addTest(namednodemapremovenameditemns07.class);
      sink.addTest(namednodemapremovenameditemns08.class);
      sink.addTest(namednodemapremovenameditemns09.class);
      sink.addTest(namednodemapsetnameditemns01.class);
      sink.addTest(namednodemapsetnameditemns02.class);
      sink.addTest(namednodemapsetnameditemns03.class);
      sink.addTest(namednodemapsetnameditemns04.class);
      sink.addTest(namednodemapsetnameditemns05.class);
      sink.addTest(namednodemapsetnameditemns06.class);
      sink.addTest(namednodemapsetnameditemns07.class);
      sink.addTest(namednodemapsetnameditemns08.class);
      sink.addTest(namednodemapsetnameditemns09.class);
      sink.addTest(namednodemapsetnameditemns10.class);
      sink.addTest(namednodemapsetnameditemns11.class);
      sink.addTest(namespaceURI01.class);
      sink.addTest(namespaceURI02.class);
      sink.addTest(namespaceURI03.class);
      sink.addTest(namespaceURI04.class);
      sink.addTest(nodegetlocalname03.class);
      sink.addTest(nodegetnamespaceuri03.class);
      sink.addTest(nodegetownerdocument01.class);
      sink.addTest(nodegetownerdocument02.class);
      sink.addTest(nodegetprefix03.class);
      sink.addTest(nodehasattributes01.class);
      sink.addTest(nodehasattributes02.class);
      sink.addTest(nodehasattributes03.class);
      sink.addTest(nodehasattributes04.class);
      sink.addTest(nodeissupported01.class);
      sink.addTest(nodeissupported02.class);
      sink.addTest(nodeissupported03.class);
      sink.addTest(nodeissupported04.class);
      sink.addTest(nodeissupported05.class);
      sink.addTest(nodenormalize01.class);
      sink.addTest(nodesetprefix01.class);
      sink.addTest(nodesetprefix02.class);
      sink.addTest(nodesetprefix03.class);
      sink.addTest(nodesetprefix04.class);
      sink.addTest(nodesetprefix05.class);
      sink.addTest(nodesetprefix06.class);
      sink.addTest(nodesetprefix07.class);
      sink.addTest(nodesetprefix08.class);
      sink.addTest(nodesetprefix09.class);
      sink.addTest(normalize01.class);
      sink.addTest(ownerDocument01.class);
      sink.addTest(ownerElement01.class);
      sink.addTest(ownerElement02.class);
      sink.addTest(prefix01.class);
      sink.addTest(prefix02.class);
      sink.addTest(prefix03.class);
      sink.addTest(prefix04.class);
      sink.addTest(prefix05.class);
      sink.addTest(prefix06.class);
      sink.addTest(prefix07.class);
      sink.addTest(prefix08.class);
      sink.addTest(prefix09.class);
      sink.addTest(prefix10.class);
      sink.addTest(prefix11.class);
      sink.addTest(publicId01.class);
      sink.addTest(removeAttributeNS01.class);
      sink.addTest(removeAttributeNS02.class);
      sink.addTest(removeNamedItemNS01.class);
      sink.addTest(removeNamedItemNS02.class);
      sink.addTest(removeNamedItemNS03.class);
      sink.addTest(setAttributeNS01.class);
      sink.addTest(setAttributeNS02.class);
      sink.addTest(setAttributeNS03.class);
      sink.addTest(setAttributeNS04.class);
      sink.addTest(setAttributeNS05.class);
      sink.addTest(setAttributeNS06.class);
      sink.addTest(setAttributeNS07.class);
      sink.addTest(setAttributeNS09.class);
      sink.addTest(setAttributeNS10.class);
      sink.addTest(setAttributeNodeNS01.class);
      sink.addTest(setAttributeNodeNS02.class);
      sink.addTest(setAttributeNodeNS03.class);
      sink.addTest(setAttributeNodeNS04.class);
      sink.addTest(setAttributeNodeNS05.class);
      sink.addTest(setNamedItemNS01.class);
      sink.addTest(setNamedItemNS02.class);
      sink.addTest(setNamedItemNS03.class);
      sink.addTest(setNamedItemNS04.class);
      sink.addTest(setNamedItemNS05.class);
      sink.addTest(systemId01.class);

   }
   /**
    *  Gets URI that identifies the test suite
    *  @return uri identifier of test suite
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/alltests";
   }

   /**
    * Runs individual test
    * @param args command line arguments
    */
   public static void main(String[] args) {
        DOMTestCase.doMain(alltests.class, args);
   }

}