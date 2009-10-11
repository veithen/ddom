
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

package org.w3c.domts.level3.core;

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
      sink.addTest(attrgetschematypeinfo01.class);
      sink.addTest(attrgetschematypeinfo02.class);
      sink.addTest(attrgetschematypeinfo03.class);
      sink.addTest(attrgetschematypeinfo04.class);
      sink.addTest(attrgetschematypeinfo05.class);
      sink.addTest(attrgetschematypeinfo06.class);
      sink.addTest(attrgetschematypeinfo07.class);
      sink.addTest(attrgetschematypeinfo08.class);
      sink.addTest(attrisid01.class);
      sink.addTest(attrisid02.class);
      sink.addTest(attrisid03.class);
      sink.addTest(attrisid04.class);
      sink.addTest(attrisid05.class);
      sink.addTest(attrisid06.class);
      sink.addTest(attrisid07.class);
      sink.addTest(canonicalform01.class);
      sink.addTest(canonicalform02.class);
      sink.addTest(canonicalform03.class);
      sink.addTest(canonicalform04.class);
      sink.addTest(canonicalform05.class);
      sink.addTest(canonicalform06.class);
      sink.addTest(canonicalform07.class);
      sink.addTest(canonicalform08.class);
      sink.addTest(canonicalform09.class);
      sink.addTest(canonicalform10.class);
      sink.addTest(canonicalform11.class);
      sink.addTest(canonicalform12.class);
      sink.addTest(cdatasections01.class);
      sink.addTest(checkcharacternormalization01.class);
      sink.addTest(checkcharacternormalization02.class);
      sink.addTest(checkcharacternormalization03.class);
      sink.addTest(comments01.class);
      sink.addTest(datatypenormalization01.class);
      sink.addTest(datatypenormalization02.class);
      sink.addTest(datatypenormalization03.class);
      sink.addTest(datatypenormalization04.class);
      sink.addTest(datatypenormalization05.class);
      sink.addTest(datatypenormalization06.class);
      sink.addTest(datatypenormalization07.class);
      sink.addTest(datatypenormalization08.class);
      sink.addTest(datatypenormalization09.class);
      sink.addTest(datatypenormalization10.class);
      sink.addTest(datatypenormalization11.class);
      sink.addTest(datatypenormalization12.class);
      sink.addTest(datatypenormalization13.class);
      sink.addTest(datatypenormalization14.class);
      sink.addTest(datatypenormalization15.class);
      sink.addTest(datatypenormalization16.class);
      sink.addTest(datatypenormalization17.class);
      sink.addTest(datatypenormalization18.class);
      sink.addTest(documentadoptnode01.class);
      sink.addTest(documentadoptnode02.class);
      sink.addTest(documentadoptnode03.class);
      sink.addTest(documentadoptnode04.class);
      sink.addTest(documentadoptnode05.class);
      sink.addTest(documentadoptnode06.class);
      sink.addTest(documentadoptnode07.class);
      sink.addTest(documentadoptnode08.class);
      sink.addTest(documentadoptnode09.class);
      sink.addTest(documentadoptnode10.class);
      sink.addTest(documentadoptnode11.class);
      sink.addTest(documentadoptnode12.class);
      sink.addTest(documentadoptnode13.class);
      sink.addTest(documentadoptnode14.class);
      sink.addTest(documentadoptnode15.class);
      sink.addTest(documentadoptnode16.class);
      sink.addTest(documentadoptnode17.class);
      sink.addTest(documentadoptnode18.class);
      sink.addTest(documentadoptnode19.class);
      sink.addTest(documentadoptnode20.class);
      sink.addTest(documentadoptnode21.class);
      sink.addTest(documentadoptnode22.class);
      sink.addTest(documentadoptnode23.class);
      sink.addTest(documentadoptnode24.class);
      sink.addTest(documentadoptnode25.class);
      sink.addTest(documentadoptnode26.class);
      sink.addTest(documentadoptnode27.class);
      sink.addTest(documentadoptnode28.class);
      sink.addTest(documentadoptnode30.class);
      sink.addTest(documentadoptnode31.class);
      sink.addTest(documentadoptnode32.class);
      sink.addTest(documentadoptnode33.class);
      sink.addTest(documentadoptnode34.class);
      sink.addTest(documentadoptnode35.class);
      sink.addTest(documentadoptnode36.class);
      sink.addTest(documentgetdoctype01.class);
      sink.addTest(documentgetdocumenturi01.class);
      sink.addTest(documentgetdocumenturi02.class);
      sink.addTest(documentgetdocumenturi03.class);
      sink.addTest(documentgetinputencoding01.class);
      sink.addTest(documentgetinputencoding02.class);
      sink.addTest(documentgetinputencoding03.class);
      sink.addTest(documentgetinputencoding04.class);
      sink.addTest(documentgetstricterrorchecking01.class);
      sink.addTest(documentgetstricterrorchecking02.class);
      sink.addTest(documentgetxmlencoding01.class);
      sink.addTest(documentgetxmlencoding02.class);
      sink.addTest(documentgetxmlencoding03.class);
      sink.addTest(documentgetxmlencoding04.class);
      sink.addTest(documentgetxmlencoding05.class);
      sink.addTest(documentgetxmlstandalone01.class);
      sink.addTest(documentgetxmlstandalone02.class);
      sink.addTest(documentgetxmlstandalone03.class);
      sink.addTest(documentgetxmlstandalone04.class);
      sink.addTest(documentgetxmlstandalone05.class);
      sink.addTest(documentgetxmlversion01.class);
      sink.addTest(documentgetxmlversion02.class);
      sink.addTest(documentgetxmlversion03.class);
      sink.addTest(documentnormalizedocument01.class);
      sink.addTest(documentnormalizedocument02.class);
      sink.addTest(documentnormalizedocument03.class);
      sink.addTest(documentnormalizedocument04.class);
      sink.addTest(documentnormalizedocument05.class);
      sink.addTest(documentnormalizedocument06.class);
      sink.addTest(documentnormalizedocument07.class);
      sink.addTest(documentnormalizedocument08.class);
      sink.addTest(documentnormalizedocument09.class);
      sink.addTest(documentnormalizedocument10.class);
      sink.addTest(documentnormalizedocument11.class);
      sink.addTest(documentnormalizedocument12.class);
      sink.addTest(documentnormalizedocument13.class);
      sink.addTest(documentrenamenode01.class);
      sink.addTest(documentrenamenode02.class);
      sink.addTest(documentrenamenode03.class);
      sink.addTest(documentrenamenode04.class);
      sink.addTest(documentrenamenode05.class);
      sink.addTest(documentrenamenode06.class);
      sink.addTest(documentrenamenode07.class);
      sink.addTest(documentrenamenode08.class);
      sink.addTest(documentrenamenode09.class);
      sink.addTest(documentrenamenode10.class);
      sink.addTest(documentrenamenode11.class);
      sink.addTest(documentrenamenode12.class);
      sink.addTest(documentrenamenode13.class);
      sink.addTest(documentrenamenode14.class);
      sink.addTest(documentrenamenode15.class);
      sink.addTest(documentrenamenode16.class);
      sink.addTest(documentrenamenode17.class);
      sink.addTest(documentrenamenode18.class);
      sink.addTest(documentrenamenode19.class);
      sink.addTest(documentrenamenode20.class);
      sink.addTest(documentrenamenode21.class);
      sink.addTest(documentrenamenode22.class);
      sink.addTest(documentrenamenode23.class);
      sink.addTest(documentrenamenode24.class);
      sink.addTest(documentrenamenode25.class);
      sink.addTest(documentrenamenode26.class);
      sink.addTest(documentrenamenode27.class);
      sink.addTest(documentrenamenode28.class);
      sink.addTest(documentrenamenode29.class);
      sink.addTest(documentsetdocumenturi01.class);
      sink.addTest(documentsetdocumenturi02.class);
      sink.addTest(documentsetdocumenturi03.class);
      sink.addTest(documentsetstricterrorchecking01.class);
      sink.addTest(documentsetstricterrorchecking02.class);
      sink.addTest(documentsetstricterrorchecking03.class);
      sink.addTest(documentsetxmlstandalone01.class);
      sink.addTest(documentsetxmlstandalone02.class);
      sink.addTest(documentsetxmlversion01.class);
      sink.addTest(documentsetxmlversion02.class);
      sink.addTest(documentsetxmlversion03.class);
      sink.addTest(documentsetxmlversion05.class);
      sink.addTest(domconfigcanonicalform1.class);
      sink.addTest(domconfigcdatasections1.class);
      sink.addTest(domconfigcheckcharacternormalization1.class);
      sink.addTest(domconfigcomments1.class);
      sink.addTest(domconfigdatatypenormalization1.class);
      sink.addTest(domconfigdatatypenormalization2.class);
      sink.addTest(domconfigelementcontentwhitespace1.class);
      sink.addTest(domconfigentities1.class);
      sink.addTest(domconfigerrorhandler1.class);
      sink.addTest(domconfigerrorhandler2.class);
      sink.addTest(domconfiginfoset1.class);
      sink.addTest(domconfignamespacedeclarations1.class);
      sink.addTest(domconfignamespaces1.class);
      sink.addTest(domconfignamespaces2.class);
      sink.addTest(domconfignormalizecharacters1.class);
      sink.addTest(domconfigparameternames01.class);
      sink.addTest(domconfigschemalocation1.class);
      sink.addTest(domconfigschematype1.class);
      sink.addTest(domconfigsplitcdatasections1.class);
      sink.addTest(domconfigurationcansetparameter01.class);
      sink.addTest(domconfigurationcansetparameter02.class);
      sink.addTest(domconfigurationcansetparameter03.class);
      sink.addTest(domconfigurationcansetparameter04.class);
      sink.addTest(domconfigurationcansetparameter06.class);
      sink.addTest(domconfigurationgetparameter01.class);
      sink.addTest(domconfigurationgetparameter02.class);
      sink.addTest(domconfigvalidate1.class);
      sink.addTest(domconfigvalidateifschema1.class);
      sink.addTest(domconfigwellformed1.class);
      sink.addTest(domimplementationgetfeature01.class);
      sink.addTest(domimplementationgetfeature02.class);
      sink.addTest(domimplementationgetfeature03.class);
      sink.addTest(domimplementationgetfeature05.class);
      sink.addTest(domimplementationgetfeature06.class);
      sink.addTest(domimplementationregistry01.class);
      sink.addTest(domimplementationregistry02.class);
      sink.addTest(domimplementationregistry03.class);
      sink.addTest(domimplementationregistry04.class);
      sink.addTest(domimplementationregistry05.class);
      sink.addTest(domimplementationregistry06.class);
      sink.addTest(domimplementationregistry07.class);
      sink.addTest(domimplementationregistry08.class);
      sink.addTest(domimplementationregistry09.class);
      sink.addTest(domimplementationregistry10.class);
      sink.addTest(domimplementationregistry11.class);
      sink.addTest(domimplementationregistry12.class);
      sink.addTest(domimplementationregistry13.class);
      sink.addTest(domimplementationregistry14.class);
      sink.addTest(domimplementationregistry15.class);
      sink.addTest(domimplementationregistry16.class);
      sink.addTest(domimplementationregistry17.class);
      sink.addTest(domimplementationregistry18.class);
      sink.addTest(domimplementationregistry19.class);
      sink.addTest(domimplementationregistry20.class);
      sink.addTest(domimplementationregistry21.class);
      sink.addTest(domimplementationregistry22.class);
      sink.addTest(domimplementationregistry23.class);
      sink.addTest(domimplementationregistry24.class);
      sink.addTest(domimplementationregistry25.class);
      sink.addTest(domstringlistcontains01.class);
      sink.addTest(domstringlistcontains02.class);
      sink.addTest(domstringlistgetlength01.class);
      sink.addTest(domstringlistitem01.class);
      sink.addTest(domstringlistitem02.class);
      sink.addTest(elementcontentwhitespace01.class);
      sink.addTest(elementcontentwhitespace02.class);
      sink.addTest(elementcontentwhitespace03.class);
      sink.addTest(elementgetschematypeinfo01.class);
      sink.addTest(elementgetschematypeinfo02.class);
      sink.addTest(elementgetschematypeinfo03.class);
      sink.addTest(elementgetschematypeinfo04.class);
      sink.addTest(elementgetschematypeinfo05.class);
      sink.addTest(elementgetschematypeinfo06.class);
      sink.addTest(elementgetschematypeinfo07.class);
      sink.addTest(elementsetidattribute01.class);
      sink.addTest(elementsetidattribute03.class);
      sink.addTest(elementsetidattribute04.class);
      sink.addTest(elementsetidattribute05.class);
      sink.addTest(elementsetidattribute06.class);
      sink.addTest(elementsetidattribute07.class);
      sink.addTest(elementsetidattribute08.class);
      sink.addTest(elementsetidattribute09.class);
      sink.addTest(elementsetidattribute10.class);
      sink.addTest(elementsetidattribute11.class);
      sink.addTest(elementsetidattributenode01.class);
      sink.addTest(elementsetidattributenode02.class);
      sink.addTest(elementsetidattributenode03.class);
      sink.addTest(elementsetidattributenode04.class);
      sink.addTest(elementsetidattributenode05.class);
      sink.addTest(elementsetidattributenode06.class);
      sink.addTest(elementsetidattributenode07.class);
      sink.addTest(elementsetidattributenode08.class);
      sink.addTest(elementsetidattributenode09.class);
      sink.addTest(elementsetidattributenode10.class);
      sink.addTest(elementsetidattributens01.class);
      sink.addTest(elementsetidattributens02.class);
      sink.addTest(elementsetidattributens03.class);
      sink.addTest(elementsetidattributens04.class);
      sink.addTest(elementsetidattributens05.class);
      sink.addTest(elementsetidattributens06.class);
      sink.addTest(elementsetidattributens07.class);
      sink.addTest(elementsetidattributens08.class);
      sink.addTest(elementsetidattributens09.class);
      sink.addTest(elementsetidattributens10.class);
      sink.addTest(elementsetidattributens11.class);
      sink.addTest(elementsetidattributens12.class);
      sink.addTest(elementsetidattributens13.class);
      sink.addTest(elementsetidattributens14.class);
      sink.addTest(entities01.class);
      sink.addTest(entities02.class);
      sink.addTest(entities03.class);
      sink.addTest(entities04.class);
      sink.addTest(entitygetinputencoding01.class);
      sink.addTest(entitygetinputencoding02.class);
      sink.addTest(entitygetinputencoding03.class);
      sink.addTest(entitygetinputencoding04.class);
      sink.addTest(entitygetxmlencoding01.class);
      sink.addTest(entitygetxmlencoding02.class);
      sink.addTest(entitygetxmlencoding03.class);
      sink.addTest(entitygetxmlencoding04.class);
      sink.addTest(entitygetxmlversion01.class);
      sink.addTest(entitygetxmlversion02.class);
      sink.addTest(entitygetxmlversion03.class);
      sink.addTest(entitygetxmlversion04.class);
      sink.addTest(handleerror01.class);
      sink.addTest(handleerror02.class);
      sink.addTest(hasFeature01.class);
      sink.addTest(hasFeature02.class);
      sink.addTest(hasFeature03.class);
      sink.addTest(hasFeature04.class);
      sink.addTest(infoset01.class);
      sink.addTest(infoset02.class);
      sink.addTest(infoset03.class);
      sink.addTest(infoset04.class);
      sink.addTest(infoset05.class);
      sink.addTest(infoset06.class);
      sink.addTest(infoset07.class);
      sink.addTest(infoset08.class);
      sink.addTest(infoset09.class);
      sink.addTest(namespacedeclarations01.class);
      sink.addTest(namespacedeclarations02.class);
      sink.addTest(nodeappendchild01.class);
      sink.addTest(nodeappendchild02.class);
      sink.addTest(nodecomparedocumentposition01.class);
      sink.addTest(nodecomparedocumentposition02.class);
      sink.addTest(nodecomparedocumentposition03.class);
      sink.addTest(nodecomparedocumentposition04.class);
      sink.addTest(nodecomparedocumentposition05.class);
      sink.addTest(nodecomparedocumentposition06.class);
      sink.addTest(nodecomparedocumentposition07.class);
      sink.addTest(nodecomparedocumentposition08.class);
      sink.addTest(nodecomparedocumentposition09.class);
      sink.addTest(nodecomparedocumentposition10.class);
      sink.addTest(nodecomparedocumentposition11.class);
      sink.addTest(nodecomparedocumentposition12.class);
      sink.addTest(nodecomparedocumentposition13.class);
      sink.addTest(nodecomparedocumentposition14.class);
      sink.addTest(nodecomparedocumentposition15.class);
      sink.addTest(nodecomparedocumentposition16.class);
      sink.addTest(nodecomparedocumentposition17.class);
      sink.addTest(nodecomparedocumentposition18.class);
      sink.addTest(nodecomparedocumentposition19.class);
      sink.addTest(nodecomparedocumentposition20.class);
      sink.addTest(nodecomparedocumentposition21.class);
      sink.addTest(nodecomparedocumentposition22.class);
      sink.addTest(nodecomparedocumentposition23.class);
      sink.addTest(nodecomparedocumentposition24.class);
      sink.addTest(nodecomparedocumentposition25.class);
      sink.addTest(nodecomparedocumentposition26.class);
      sink.addTest(nodecomparedocumentposition27.class);
      sink.addTest(nodecomparedocumentposition28.class);
      sink.addTest(nodecomparedocumentposition29.class);
      sink.addTest(nodecomparedocumentposition30.class);
      sink.addTest(nodecomparedocumentposition31.class);
      sink.addTest(nodecomparedocumentposition32.class);
      sink.addTest(nodecomparedocumentposition33.class);
      sink.addTest(nodecomparedocumentposition34.class);
      sink.addTest(nodecomparedocumentposition35.class);
      sink.addTest(nodecomparedocumentposition36.class);
      sink.addTest(nodecomparedocumentposition37.class);
      sink.addTest(nodecomparedocumentposition38.class);
      sink.addTest(nodecomparedocumentposition39.class);
      sink.addTest(nodecomparedocumentposition40.class);
      sink.addTest(nodegetbaseuri01.class);
      sink.addTest(nodegetbaseuri02.class);
      sink.addTest(nodegetbaseuri03.class);
      sink.addTest(nodegetbaseuri04.class);
      sink.addTest(nodegetbaseuri05.class);
      sink.addTest(nodegetbaseuri06.class);
      sink.addTest(nodegetbaseuri07.class);
      sink.addTest(nodegetbaseuri09.class);
      sink.addTest(nodegetbaseuri10.class);
      sink.addTest(nodegetbaseuri11.class);
      sink.addTest(nodegetbaseuri12.class);
      sink.addTest(nodegetbaseuri13.class);
      sink.addTest(nodegetbaseuri14.class);
      sink.addTest(nodegetbaseuri15.class);
      sink.addTest(nodegetbaseuri16.class);
      sink.addTest(nodegetbaseuri17.class);
      sink.addTest(nodegetbaseuri18.class);
      sink.addTest(nodegetbaseuri19.class);
      sink.addTest(nodegetbaseuri20.class);
      sink.addTest(nodegetfeature01.class);
      sink.addTest(nodegetfeature02.class);
      sink.addTest(nodegetfeature03.class);
      sink.addTest(nodegetfeature04.class);
      sink.addTest(nodegetfeature05.class);
      sink.addTest(nodegetfeature06.class);
      sink.addTest(nodegetfeature07.class);
      sink.addTest(nodegetfeature08.class);
      sink.addTest(nodegetfeature09.class);
      sink.addTest(nodegetfeature10.class);
      sink.addTest(nodegetfeature11.class);
      sink.addTest(nodegetfeature12.class);
      sink.addTest(nodegetfeature13.class);
      sink.addTest(nodegettextcontent01.class);
      sink.addTest(nodegettextcontent02.class);
      sink.addTest(nodegettextcontent03.class);
      sink.addTest(nodegettextcontent04.class);
      sink.addTest(nodegettextcontent05.class);
      sink.addTest(nodegettextcontent06.class);
      sink.addTest(nodegettextcontent07.class);
      sink.addTest(nodegettextcontent08.class);
      sink.addTest(nodegettextcontent09.class);
      sink.addTest(nodegettextcontent10.class);
      sink.addTest(nodegettextcontent11.class);
      sink.addTest(nodegettextcontent12.class);
      sink.addTest(nodegettextcontent13.class);
      sink.addTest(nodegettextcontent14.class);
      sink.addTest(nodegettextcontent15.class);
      sink.addTest(nodegettextcontent16.class);
      sink.addTest(nodegettextcontent17.class);
      sink.addTest(nodegettextcontent18.class);
      sink.addTest(nodegettextcontent19.class);
      sink.addTest(nodegetuserdata01.class);
      sink.addTest(nodegetuserdata02.class);
      sink.addTest(nodegetuserdata03.class);
      sink.addTest(nodegetuserdata04.class);
      sink.addTest(nodegetuserdata05.class);
      sink.addTest(nodegetuserdata06.class);
      sink.addTest(nodegetuserdata07.class);
      sink.addTest(nodeinsertbefore01.class);
      sink.addTest(nodeinsertbefore02.class);
      sink.addTest(nodeinsertbefore03.class);
      sink.addTest(nodeinsertbefore04.class);
      sink.addTest(nodeinsertbefore05.class);
      sink.addTest(nodeinsertbefore06.class);
      sink.addTest(nodeinsertbefore07.class);
      sink.addTest(nodeinsertbefore08.class);
      sink.addTest(nodeinsertbefore09.class);
      sink.addTest(nodeinsertbefore10.class);
      sink.addTest(nodeinsertbefore11.class);
      sink.addTest(nodeinsertbefore12.class);
      sink.addTest(nodeinsertbefore13.class);
      sink.addTest(nodeinsertbefore14.class);
      sink.addTest(nodeinsertbefore15.class);
      sink.addTest(nodeinsertbefore16.class);
      sink.addTest(nodeinsertbefore17.class);
      sink.addTest(nodeinsertbefore18.class);
      sink.addTest(nodeinsertbefore19.class);
      sink.addTest(nodeinsertbefore20.class);
      sink.addTest(nodeinsertbefore21.class);
      sink.addTest(nodeinsertbefore22.class);
      sink.addTest(nodeinsertbefore23.class);
      sink.addTest(nodeinsertbefore24.class);
      sink.addTest(nodeinsertbefore25.class);
      sink.addTest(nodeisdefaultnamespace01.class);
      sink.addTest(nodeisdefaultnamespace02.class);
      sink.addTest(nodeisdefaultnamespace03.class);
      sink.addTest(nodeisdefaultnamespace04.class);
      sink.addTest(nodeisdefaultnamespace05.class);
      sink.addTest(nodeisdefaultnamespace06.class);
      sink.addTest(nodeisdefaultnamespace07.class);
      sink.addTest(nodeisdefaultnamespace08.class);
      sink.addTest(nodeisdefaultnamespace09.class);
      sink.addTest(nodeisdefaultnamespace10.class);
      sink.addTest(nodeisdefaultnamespace11.class);
      sink.addTest(nodeisdefaultnamespace13.class);
      sink.addTest(nodeisdefaultnamespace14.class);
      sink.addTest(nodeisdefaultnamespace15.class);
      sink.addTest(nodeisdefaultnamespace16.class);
      sink.addTest(nodeisequalnode01.class);
      sink.addTest(nodeisequalnode02.class);
      sink.addTest(nodeisequalnode03.class);
      sink.addTest(nodeisequalnode04.class);
      sink.addTest(nodeisequalnode05.class);
      sink.addTest(nodeisequalnode06.class);
      sink.addTest(nodeisequalnode07.class);
      sink.addTest(nodeisequalnode08.class);
      sink.addTest(nodeisequalnode09.class);
      sink.addTest(nodeisequalnode10.class);
      sink.addTest(nodeisequalnode11.class);
      sink.addTest(nodeisequalnode12.class);
      sink.addTest(nodeisequalnode13.class);
      sink.addTest(nodeisequalnode14.class);
      sink.addTest(nodeisequalnode15.class);
      sink.addTest(nodeisequalnode16.class);
      sink.addTest(nodeisequalnode17.class);
      sink.addTest(nodeisequalnode18.class);
      sink.addTest(nodeisequalnode19.class);
      sink.addTest(nodeisequalnode20.class);
      sink.addTest(nodeisequalnode21.class);
      sink.addTest(nodeisequalnode22.class);
      sink.addTest(nodeisequalnode25.class);
      sink.addTest(nodeisequalnode26.class);
      sink.addTest(nodeisequalnode27.class);
      sink.addTest(nodeisequalnode28.class);
      sink.addTest(nodeisequalnode29.class);
      sink.addTest(nodeisequalnode31.class);
      sink.addTest(nodeisequalnode32.class);
      sink.addTest(nodeissamenode01.class);
      sink.addTest(nodeissamenode02.class);
      sink.addTest(nodeissamenode03.class);
      sink.addTest(nodeissamenode04.class);
      sink.addTest(nodeissamenode05.class);
      sink.addTest(nodeissamenode06.class);
      sink.addTest(nodeissamenode07.class);
      sink.addTest(nodeissamenode08.class);
      sink.addTest(nodeissamenode09.class);
      sink.addTest(nodeissamenode10.class);
      sink.addTest(nodelookupnamespaceuri01.class);
      sink.addTest(nodelookupnamespaceuri02.class);
      sink.addTest(nodelookupnamespaceuri03.class);
      sink.addTest(nodelookupnamespaceuri04.class);
      sink.addTest(nodelookupnamespaceuri05.class);
      sink.addTest(nodelookupnamespaceuri06.class);
      sink.addTest(nodelookupnamespaceuri07.class);
      sink.addTest(nodelookupnamespaceuri08.class);
      sink.addTest(nodelookupnamespaceuri09.class);
      sink.addTest(nodelookupnamespaceuri10.class);
      sink.addTest(nodelookupnamespaceuri11.class);
      sink.addTest(nodelookupnamespaceuri13.class);
      sink.addTest(nodelookupnamespaceuri14.class);
      sink.addTest(nodelookupnamespaceuri15.class);
      sink.addTest(nodelookupnamespaceuri16.class);
      sink.addTest(nodelookupnamespaceuri17.class);
      sink.addTest(nodelookupnamespaceuri18.class);
      sink.addTest(nodelookupnamespaceuri19.class);
      sink.addTest(nodelookupnamespaceuri20.class);
      sink.addTest(nodelookupprefix01.class);
      sink.addTest(nodelookupprefix02.class);
      sink.addTest(nodelookupprefix03.class);
      sink.addTest(nodelookupprefix04.class);
      sink.addTest(nodelookupprefix05.class);
      sink.addTest(nodelookupprefix06.class);
      sink.addTest(nodelookupprefix07.class);
      sink.addTest(nodelookupprefix08.class);
      sink.addTest(nodelookupprefix09.class);
      sink.addTest(nodelookupprefix10.class);
      sink.addTest(nodelookupprefix11.class);
      sink.addTest(nodelookupprefix12.class);
      sink.addTest(nodelookupprefix13.class);
      sink.addTest(nodelookupprefix14.class);
      sink.addTest(nodelookupprefix15.class);
      sink.addTest(nodelookupprefix16.class);
      sink.addTest(nodelookupprefix17.class);
      sink.addTest(nodelookupprefix18.class);
      sink.addTest(nodelookupprefix19.class);
      sink.addTest(nodelookupprefix20.class);
      sink.addTest(noderemovechild01.class);
      sink.addTest(noderemovechild02.class);
      sink.addTest(noderemovechild03.class);
      sink.addTest(noderemovechild04.class);
      sink.addTest(noderemovechild05.class);
      sink.addTest(noderemovechild07.class);
      sink.addTest(noderemovechild08.class);
      sink.addTest(noderemovechild09.class);
      sink.addTest(noderemovechild10.class);
      sink.addTest(noderemovechild11.class);
      sink.addTest(noderemovechild12.class);
      sink.addTest(noderemovechild13.class);
      sink.addTest(noderemovechild14.class);
      sink.addTest(noderemovechild15.class);
      sink.addTest(noderemovechild16.class);
      sink.addTest(noderemovechild17.class);
      sink.addTest(noderemovechild18.class);
      sink.addTest(noderemovechild19.class);
      sink.addTest(noderemovechild20.class);
      sink.addTest(noderemovechild21.class);
      sink.addTest(noderemovechild22.class);
      sink.addTest(noderemovechild23.class);
      sink.addTest(noderemovechild24.class);
      sink.addTest(noderemovechild25.class);
      sink.addTest(noderemovechild26.class);
      sink.addTest(noderemovechild27.class);
      sink.addTest(noderemovechild28.class);
      sink.addTest(noderemovechild29.class);
      sink.addTest(noderemovechild30.class);
      sink.addTest(noderemovechild31.class);
      sink.addTest(nodereplacechild01.class);
      sink.addTest(nodereplacechild02.class);
      sink.addTest(nodereplacechild03.class);
      sink.addTest(nodereplacechild04.class);
      sink.addTest(nodereplacechild06.class);
      sink.addTest(nodereplacechild07.class);
      sink.addTest(nodereplacechild08.class);
      sink.addTest(nodereplacechild10.class);
      sink.addTest(nodereplacechild12.class);
      sink.addTest(nodereplacechild13.class);
      sink.addTest(nodereplacechild14.class);
      sink.addTest(nodereplacechild15.class);
      sink.addTest(nodereplacechild16.class);
      sink.addTest(nodereplacechild17.class);
      sink.addTest(nodereplacechild18.class);
      sink.addTest(nodereplacechild19.class);
      sink.addTest(nodereplacechild20.class);
      sink.addTest(nodereplacechild21.class);
      sink.addTest(nodereplacechild22.class);
      sink.addTest(nodereplacechild23.class);
      sink.addTest(nodereplacechild24.class);
      sink.addTest(nodereplacechild25.class);
      sink.addTest(nodereplacechild26.class);
      sink.addTest(nodereplacechild27.class);
      sink.addTest(nodereplacechild28.class);
      sink.addTest(nodereplacechild29.class);
      sink.addTest(nodereplacechild30.class);
      sink.addTest(nodereplacechild31.class);
      sink.addTest(nodereplacechild32.class);
      sink.addTest(nodereplacechild33.class);
      sink.addTest(nodereplacechild34.class);
      sink.addTest(nodereplacechild35.class);
      sink.addTest(nodereplacechild36.class);
      sink.addTest(nodereplacechild37.class);
      sink.addTest(nodereplacechild38.class);
      sink.addTest(nodereplacechild39.class);
      sink.addTest(nodereplacechild40.class);
      sink.addTest(nodesettextcontent01.class);
      sink.addTest(nodesettextcontent02.class);
      sink.addTest(nodesettextcontent03.class);
      sink.addTest(nodesettextcontent04.class);
      sink.addTest(nodesettextcontent05.class);
      sink.addTest(nodesettextcontent06.class);
      sink.addTest(nodesettextcontent07.class);
      sink.addTest(nodesettextcontent08.class);
      sink.addTest(nodesettextcontent10.class);
      sink.addTest(nodesettextcontent11.class);
      sink.addTest(nodesettextcontent12.class);
      sink.addTest(nodesettextcontent13.class);
      sink.addTest(nodesetuserdata01.class);
      sink.addTest(nodesetuserdata02.class);
      sink.addTest(nodesetuserdata03.class);
      sink.addTest(nodesetuserdata04.class);
      sink.addTest(nodesetuserdata05.class);
      sink.addTest(nodesetuserdata06.class);
      sink.addTest(nodesetuserdata07.class);
      sink.addTest(nodesetuserdata08.class);
      sink.addTest(nodesetuserdata09.class);
      sink.addTest(nodesetuserdata10.class);
      sink.addTest(normalizecharacters01.class);
      sink.addTest(normalizecharacters02.class);
      sink.addTest(normalizecharacters03.class);
      sink.addTest(normalizecharacters04.class);
      sink.addTest(normalizecharacters05.class);
      sink.addTest(normalizecharacters06.class);
      sink.addTest(normalizecharacters07.class);
      sink.addTest(normalizecharacters08.class);
      sink.addTest(splitcdatasections01.class);
      sink.addTest(textiselementcontentwhitespace01.class);
      sink.addTest(textiselementcontentwhitespace02.class);
      sink.addTest(textiselementcontentwhitespace03.class);
      sink.addTest(textiselementcontentwhitespace04.class);
      sink.addTest(textiselementcontentwhitespace05.class);
      sink.addTest(textiselementcontentwhitespace06.class);
      sink.addTest(textreplacewholetext01.class);
      sink.addTest(textreplacewholetext02.class);
      sink.addTest(textreplacewholetext03.class);
      sink.addTest(textreplacewholetext04.class);
      sink.addTest(textreplacewholetext05.class);
      sink.addTest(textreplacewholetext06.class);
      sink.addTest(textreplacewholetext07.class);
      sink.addTest(textreplacewholetext08.class);
      sink.addTest(textwholetext01.class);
      sink.addTest(textwholetext02.class);
      sink.addTest(textwholetext03.class);
      sink.addTest(typeinfogettypename03.class);
      sink.addTest(typeinfogettypename04.class);
      sink.addTest(typeinfogettypenamespace01.class);
      sink.addTest(typeinfogettypenamespace03.class);
      sink.addTest(typeinfogettypenamespace04.class);
      sink.addTest(typeinfoisderivedfrom01.class);
      sink.addTest(typeinfoisderivedfrom02.class);
      sink.addTest(typeinfoisderivedfrom03.class);
      sink.addTest(typeinfoisderivedfrom04.class);
      sink.addTest(typeinfoisderivedfrom05.class);
      sink.addTest(typeinfoisderivedfrom06.class);
      sink.addTest(typeinfoisderivedfrom07.class);
      sink.addTest(typeinfoisderivedfrom08.class);
      sink.addTest(typeinfoisderivedfrom09.class);
      sink.addTest(typeinfoisderivedfrom10.class);
      sink.addTest(typeinfoisderivedfrom11.class);
      sink.addTest(typeinfoisderivedfrom12.class);
      sink.addTest(typeinfoisderivedfrom13.class);
      sink.addTest(typeinfoisderivedfrom14.class);
      sink.addTest(typeinfoisderivedfrom15.class);
      sink.addTest(typeinfoisderivedfrom16.class);
      sink.addTest(typeinfoisderivedfrom17.class);
      sink.addTest(typeinfoisderivedfrom18.class);
      sink.addTest(typeinfoisderivedfrom19.class);
      sink.addTest(typeinfoisderivedfrom20.class);
      sink.addTest(typeinfoisderivedfrom21.class);
      sink.addTest(typeinfoisderivedfrom22.class);
      sink.addTest(typeinfoisderivedfrom23.class);
      sink.addTest(typeinfoisderivedfrom24.class);
      sink.addTest(typeinfoisderivedfrom25.class);
      sink.addTest(typeinfoisderivedfrom26.class);
      sink.addTest(typeinfoisderivedfrom27.class);
      sink.addTest(typeinfoisderivedfrom28.class);
      sink.addTest(typeinfoisderivedfrom29.class);
      sink.addTest(typeinfoisderivedfrom30.class);
      sink.addTest(typeinfoisderivedfrom31.class);
      sink.addTest(typeinfoisderivedfrom32.class);
      sink.addTest(typeinfoisderivedfrom33.class);
      sink.addTest(typeinfoisderivedfrom34.class);
      sink.addTest(typeinfoisderivedfrom35.class);
      sink.addTest(typeinfoisderivedfrom36.class);
      sink.addTest(typeinfoisderivedfrom37.class);
      sink.addTest(typeinfoisderivedfrom38.class);
      sink.addTest(typeinfoisderivedfrom39.class);
      sink.addTest(typeinfoisderivedfrom40.class);
      sink.addTest(typeinfoisderivedfrom41.class);
      sink.addTest(typeinfoisderivedfrom42.class);
      sink.addTest(typeinfoisderivedfrom43.class);
      sink.addTest(typeinfoisderivedfrom44.class);
      sink.addTest(typeinfoisderivedfrom45.class);
      sink.addTest(typeinfoisderivedfrom46.class);
      sink.addTest(typeinfoisderivedfrom47.class);
      sink.addTest(typeinfoisderivedfrom48.class);
      sink.addTest(typeinfoisderivedfrom49.class);
      sink.addTest(typeinfoisderivedfrom50.class);
      sink.addTest(typeinfoisderivedfrom51.class);
      sink.addTest(typeinfoisderivedfrom52.class);
      sink.addTest(typeinfoisderivedfrom53.class);
      sink.addTest(typeinfoisderivedfrom54.class);
      sink.addTest(typeinfoisderivedfrom55.class);
      sink.addTest(typeinfoisderivedfrom56.class);
      sink.addTest(typeinfoisderivedfrom57.class);
      sink.addTest(typeinfoisderivedfrom58.class);
      sink.addTest(typeinfoisderivedfrom59.class);
      sink.addTest(typeinfoisderivedfrom60.class);
      sink.addTest(typeinfoisderivedfrom61.class);
      sink.addTest(typeinfoisderivedfrom62.class);
      sink.addTest(typeinfoisderivedfrom63.class);
      sink.addTest(typeinfoisderivedfrom64.class);
      sink.addTest(typeinfoisderivedfrom65.class);
      sink.addTest(typeinfoisderivedfrom66.class);
      sink.addTest(typeinfoisderivedfrom67.class);
      sink.addTest(typeinfoisderivedfrom68.class);
      sink.addTest(typeinfoisderivedfrom69.class);
      sink.addTest(typeinfoisderivedfrom70.class);
      sink.addTest(typeinfoisderivedfrom71.class);
      sink.addTest(typeinfoisderivedfrom72.class);
      sink.addTest(typeinfoisderivedfrom73.class);
      sink.addTest(userdatahandler01.class);
      sink.addTest(userdatahandler02.class);
      sink.addTest(userdatahandler03.class);
      sink.addTest(userdatahandler04.class);
      sink.addTest(wellformed01.class);
      sink.addTest(wellformed02.class);
      sink.addTest(wellformed03.class);
      sink.addTest(wellformed04.class);

   }
   /**
    *  Gets URI that identifies the test suite
    *  @return uri identifier of test suite
    */
   public String getTargetURI() {
      return "http://www.w3.org/2001/DOM-Test-Suite/level3/core/alltests";
   }

   /**
    * Runs individual test
    * @param args command line arguments
    */
   public static void main(String[] args) {
        DOMTestCase.doMain(alltests.class, args);
   }

}