package com.company;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class MghtSOAP {
    private final String xmlSigningSoapURL = "http://192.168.1.52:8080/mock/soap/project/6wVj45/DSServiceV3Soap";
    private final String xmlVerifySoapURL = "http://192.168.1.52:8080/mock/soap/project/6wVj45/Validation";

    private SOAPMessage formXmlSigningSoapRequest(String input, String key) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration("tem", "http://tempuri.org/");
        soapEnvelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");

        SOAPBody soapBody = soapEnvelope.getBody();

        SOAPElement soapElement = soapBody.addChildElement("XmlSigning", "tem");
        SOAPElement elementInput = soapElement.addChildElement("Input", "tem");
        SOAPElement elementKey = soapElement.addChildElement("Key", "tem");

        elementInput.addTextNode(input);
        elementKey.addTextNode(key);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private SOAPMessage formVerifyRequest(String input) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration("tem", "http://tempuri.org/");
        soapEnvelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");

        SOAPBody soapBody = soapEnvelope.getBody();

        SOAPElement soapElement = soapBody.addChildElement("XmlSigning", "tem");
        SOAPElement elementInput = soapElement.addChildElement("Input", "tem");

        elementInput.addTextNode(input);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private SOAPMessage sendRequest(SOAPMessage request, String destination) throws SOAPException {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        String url = destination;
        SOAPMessage soapResponse = soapConnection.call(request, url);
        return soapResponse;
    }

    private String getStrValueFromSoapMessage(SOAPMessage soapMessage, String tagName) throws IOException, SOAPException, ParserConfigurationException, SAXException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        String strMsg = new String(out.toByteArray());

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(strMsg));

        Document doc = builder.parse(src);
//        String value = doc.getElementsByTagName("web:XmlSigningResponse").item(0).getTextContent();
        String value = doc.getElementsByTagName(tagName).item(0).getTextContent();
        return value;
    }

}
