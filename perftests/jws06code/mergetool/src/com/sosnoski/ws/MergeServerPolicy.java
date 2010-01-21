package com.sosnoski.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MergeServerPolicy
{
    /**
     * Create module reference element.
     * 
     * @param doc containing document
     * @param module module name
     * @return element
     */
    private static Element createModuleRefElement(Document doc, String module) {
        Element ref = doc.createElementNS(null, "module");
        ref.setAttribute("ref", module);
        return ref;
    }
    
    /**
     * Main program to update services.xml file.
     * 
     * @param args
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     * @throws TransformerException 
     */
    public static void main(String[] args)
        throws ParserConfigurationException, SAXException, IOException, TransformerException {
        
        // check for required command line parameters
        if (args.length < 2) {
            System.out.println("Usage:\n" +
                "  java com.sosnoski.ws.MergeServerPolicy services-file policy-file module1 module2 ...");
            System.exit(1);
        }
        
        // configure the document builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // parse the policy file to be merged
        Document policy = builder.parse(new File(args[1]));
        
        // parse the services.xml file to be modified
        File file = new File(args[0]);
        Document doc = builder.parse(file);
        
        // add module references and policy to each service definition
        NodeList services = doc.getElementsByTagName("service");
        for (int i = 0; i < services.getLength(); i++) {
            
            // start by adding module reference elements at end of service definition
            Element service = (Element)services.item(i);
            if (args.length > 2) {
                service.appendChild(doc.createTextNode("\n"));
                for (int j = 2; j < args.length; j++) {
                    service.appendChild(createModuleRefElement(doc, args[j]));
                }
            }
            
            // finish by appending policy
            service.appendChild(doc.createTextNode("\n"));
            service.appendChild(doc.importNode(policy.getDocumentElement(), true));
        }
        
        // write modified services.xml back to file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        FileOutputStream fos = new FileOutputStream(file);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, new StreamResult(fos));
        fos.close();
    }
}