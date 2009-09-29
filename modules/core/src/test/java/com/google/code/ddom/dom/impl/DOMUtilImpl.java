package com.google.code.ddom.dom.impl;

import org.w3c.dom.Document;

public interface DOMUtilImpl {
    Document newDocument();
    Document parse(boolean namespaceAware, String xml);
}
