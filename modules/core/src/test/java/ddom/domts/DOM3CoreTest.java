package ddom.domts;

import junit.framework.TestSuite;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.level3.core.alltests;
import org.w3c.domts.level3.core.canonicalform06;
import org.w3c.domts.level3.core.documentnormalizedocument10;
import org.w3c.domts.level3.core.documentnormalizedocument11;
import org.w3c.domts.level3.core.documentrenamenode27;
import org.w3c.domts.level3.core.documentrenamenode28;
import org.w3c.domts.level3.core.domconfigurationcansetparameter03;
import org.w3c.domts.level3.core.domconfigurationcansetparameter06;
import org.w3c.domts.level3.core.elementsetidattributens05;

public class DOM3CoreTest extends TestSuite {
    public static TestSuite suite() throws Exception {
        DOMTestDocumentBuilderFactory factory = Factories.getFactory();
        FilteredDOMTestSuite suite = new FilteredDOMTestSuite(factory, new alltests(factory));
        
        // Tests accessing DTD information
        suite.addExclude("documentadoptnode(17|18|19|20)");
        suite.addExclude(documentrenamenode28.class);
        suite.addExclude(domconfigurationcansetparameter03.class);
        suite.addExclude("entityget.*");
        
        // Tests using entity references
        suite.addExclude(documentrenamenode27.class);
        suite.addExclude("entities.*");
        suite.addExclude("infoset(01|02)");
        suite.addExclude("textreplacewholetext(06|07|08)");
        
        // Tests relying on Attr#getSchemaTypeInfo
        suite.addExclude("attrgetschematypeinfo.*");
        suite.addExclude("elementgetschematypeinfo.*");
        suite.addExclude("typeinfo.*");
        
        // Tests relying on Text#isElementContentWhitespace
        suite.addExclude(domconfigurationcansetparameter06.class);
        
        // Tests relying on entity references
        suite.addExclude("documentadoptnode(06|16)");
        
        // Tests using setIdAttributeNode on a namespace declaration (!)
        suite.addExclude("elementsetidattributenode(01|04|08)");
        suite.addExclude("elementsetidattributens(01|03|10|11|13|14)");
        
        // This test case is actually no longer valid because the NameChar and
        // NameStartChar productions have changed in the Fifth Edition of the
        // XML 1.0 specs (and are now the same as in XML 1.1).
        suite.addExclude(canonicalform06.class);
        
        // TODO: this test looks wrong because it adds an element to a document that already has a document element
        suite.addExclude(documentnormalizedocument10.class);
        
        // TODO: this test looks wrong because it searches for an acronym node and then checks if it is called "address"
        suite.addExclude(documentnormalizedocument11.class);
        
        // TODO: looks wrong because it calls setAttributeNS with "*" as namespaceURI
        suite.addExclude(elementsetidattributens05.class);
        
        return new JUnitTestSuiteAdapter(suite);
    }
}
