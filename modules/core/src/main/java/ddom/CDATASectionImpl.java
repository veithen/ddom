package ddom;

import org.w3c.dom.CDATASection;

public class CDATASectionImpl extends TextNode implements CDATASection {
    public CDATASectionImpl(DocumentImpl document, String data) {
        super(document, data);
    }

    public final short getNodeType() {
        return CDATA_SECTION_NODE;
    }

    public final String getNodeName() {
        return "#cdata-section";
    }

    @Override
    protected TextNode createNewTextNode(String data) {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createCDATASection(document, data);
    }
}
