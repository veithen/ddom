package ddom;

import org.w3c.dom.Document;

public interface DOMUtilImpl {
    Document newDocument();
    Document parse(boolean namespaceAware, String xml);
}
