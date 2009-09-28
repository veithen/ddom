package ddom;

import org.w3c.dom.Node;

public interface AbstractNode extends Node {
    /**
     * Get the document to which this node belongs. In contrast to {@link Node#getOwnerDocument()},
     * this method will never return <code>null</code>.
     * 
     * @return the document
     */
    DocumentImpl getDocument();
    
    CharSequence collectTextContent(CharSequence appendTo);
}
