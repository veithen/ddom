package ddom.domts;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DocumentBuilderSetting;

public class Factories {
    public static DOMTestDocumentBuilderFactory getFactory() throws Exception {
        return new DOMTestDocumentBuilderFactoryImpl(
                new DocumentBuilderSetting[] {
                        DocumentBuilderSetting.notCoalescing,
                        DocumentBuilderSetting.expandEntityReferences,
                        DocumentBuilderSetting.namespaceAware,
                        DocumentBuilderSetting.notValidating });
        
//        return new JAXPDOMTestDocumentBuilderFactory(null,
//                new DocumentBuilderSetting[] {
//                        DocumentBuilderSetting.notCoalescing,
//                        DocumentBuilderSetting.expandEntityReferences,
//                        DocumentBuilderSetting.notIgnoringElementContentWhitespace,
//                        DocumentBuilderSetting.namespaceAware,
//                        DocumentBuilderSetting.notValidating });
    }
}
