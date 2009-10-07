package com.google.code.ddom.xmlts;

import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLConformanceTestSuite {
    private static String getElementText(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder buffer = new StringBuilder();
        int level = 0;
        while (reader.next() != XMLStreamReader.END_ELEMENT || level != 0) {
            if (reader.isCharacters()) {
                buffer.append(reader.getText());
            } else if (reader.isStartElement()) {
                level++;
            } else if (reader.isEndElement()) {
                level--;
            }
        }
        return buffer.toString();
    }
    
    public static void main(String[] args) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        URL url = XMLConformanceTestSuite.class.getResource("/xmlconf/xmlconf.xml");
        InputStream in = url.openStream();
        XMLStreamReader reader = factory.createXMLStreamReader(url.toExternalForm(), in);
        while (!reader.isStartElement()) {
            reader.next();
        }
        reader.require(XMLStreamReader.START_ELEMENT, null, "TESTSUITE");
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            reader.require(XMLStreamReader.START_ELEMENT, null, "TESTCASES");
            while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                reader.require(XMLStreamReader.START_ELEMENT, null, "TESTCASES");
                while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                    reader.require(XMLStreamReader.START_ELEMENT, null, "TEST");
                    System.out.println(getElementText(reader));
                }
            }
        }
    }
}
