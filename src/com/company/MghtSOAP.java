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

    private static SOAPMessage formXmlSigningSoapRequest(String input, String key) throws SOAPException {
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

    private static SOAPMessage sendRequest(SOAPMessage request, String destination) throws SOAPException {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        String url = destination;
        SOAPMessage soapResponse = soapConnection.call(request, url);
        return soapResponse;
    }

    private static String getStrValueFromSoapMessage(SOAPMessage soapMessage, String tagName) throws IOException, SOAPException, ParserConfigurationException, SAXException {
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

    public static void main(String[] args) throws SOAPException, ParserConfigurationException, SAXException, IOException {
//        MessageFactory messageFactory = MessageFactory.newInstance();
//        SOAPMessage soapMessage = messageFactory.createMessage();
//        SOAPPart soapPart = soapMessage.getSOAPPart();
//        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
//        soapEnvelope.addNamespaceDeclaration("tem", "http://tempuri.org/");
//        soapEnvelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");
//
//        SOAPBody soapBody = soapEnvelope.getBody();
//
//        SOAPElement soapElement = soapBody.addChildElement("XmlSigning", "tem");
//        SOAPElement elementInput = soapElement.addChildElement("Input", "tem");
//        SOAPElement elementKey = soapElement.addChildElement("Key", "tem");
//
//        String dataXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CUSTOM><Data>DataSign</Data></CUSTOM>";
//        elementInput.addTextNode(dataXml);
//        elementKey.addTextNode("1");
//
//        soapMessage.saveChanges();
//
//
//        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//        String url = "http://10.224.128.93/DSService/DSServiceV3.asmx";
//        SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        soapResponse.writeTo(out);
//        String strMsg = new String(out.toByteArray());
//        System.out.println(strMsg);


//=========================================
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration("xmlns", "http://tempuri.org/");
        soapEnvelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");

        SOAPBody soapBody = soapEnvelope.getBody();

        SOAPElement soapElement = soapBody.addChildElement("Validate");
        SOAPElement soapElement2 = soapElement.addChildElement("XML");

        String verifyStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CUSTOM><Data>DataSign</Data><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><DigestValue>453Zg5o2Wu/K9kpgnCV6tnrf9D0=</DigestValue></Reference></SignedInfo><SignatureValue>NhKNQsZ1PB4iSEMpIlO41nI4yOplUV0cUOJacYFIJDTMcQnOgVmQeFh4IhqtwzNMCk5jMCv9Fi5mH6Ofs7v2U7vu/JOZaBQCb6WKaigX1AppgCcVGnIOV5PsxsOFuV3buIhDx3f/PozWaz/Q1IF7sC4FYDPndf4o4bDU+/uiRa/EpPxmD0hy+H59rI3yEZmlJEeTUZfBO16FPVAPO+kozNBZOs/dLHCu+OhsQxLBvOzVlgD0pXy6MyQtTvlVy4xrOPSoP8ZNWbQ6wVMJLbpBXWGjO0SghdzYAp54n5IQWSt7cD5OhyoRaC3Rwke0p1pOfA4KJw0UL9uRUExrpBtR4g==</SignatureValue><KeyInfo><X509Data><X509IssuerSerial><X509IssuerName>CN=Co quan chung thuc so Bo Tai chinh, O=Ban Co yeu Chinh phu, C=VN</X509IssuerName><X509SerialNumber>7017934</X509SerialNumber></X509IssuerSerial><X509Certificate>MIIFJjCCBA6gAwIBAgIDaxXOMA0GCSqGSIb3DQEBBQUAMFkxCzAJBgNVBAYTAlZOMR0wGwYDVQQKDBRCYW4gQ28geWV1IENoaW5oIHBodTErMCkGA1UEAwwiQ28gcXVhbiBjaHVuZyB0aHVjIHNvIEJvIFRhaSBjaGluaDAeFw0xNTAyMTMwODM1MTZaFw0yMDAyMTIwODM1MTZaMCoxDTALBgNVBAMMBFRDSFExDDAKBgNVBAoMA0JUQzELMAkGA1UEBhMCVk4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCJ/DZItyf/ofpqDz9g15psnW7wfSlNFFzOPgRKt+aod1iwnTKk6mIFqqzio9TvYmeRBiIybw20YRLELtDcEoYGMzvox8wC7P+h4+fyPPZEShyHx5tBQToXuymCjWiKf66PMdJlExC97ux3Pk9HlultBeCkz2Oaf//ljtP7JeoJ1MnDeFUIsHOFKwbbGADYccivY2MarkKUegjDWQm4pCG7GQO1HoN68Pnmg15lWxl092oN+WVe0vK0U4ddDYnGSRU8JaGYUN5Ph4Tp6FMx7NLQ+nWy9dspRaZvgKNx1pN9RCP+rxVFvH6ep+qvBCADH1PiiDacccGC8gjgKHIBtl0FAgMBAAGjggIkMIICIDAJBgNVHRMEAjAAMAsGA1UdDwQEAwIGQDAfBglghkgBhvhCAQ0EEhYQVXNlciBTaWduIG9mIEJUQzAdBgNVHQ4EFgQUncGEujPodX9zvYCAjfP9KLx1djEwgZUGA1UdIwSBjTCBioAUnjia1imViWoFfyr/XwGXtFcwZrKhb6RtMGsxCzAJBgNVBAYTAlZOMR0wGwYDVQQKDBRCYW4gQ28geWV1IENoaW5oIHBodTE9MDsGA1UEAww0Q28gcXVhbiBjaHVuZyB0aHVjIHNvIGNodXllbiBkdW5nIENoaW5oIHBodSAoUm9vdENBKYIBAzAJBgNVHRIEAjAAMF8GCCsGAQUFBwEBBFMwUTAfBggrBgEFBQcwAYYTaHR0cDovL29jc3AuY2EuYnRjLzAuBggrBgEFBQcwAoYiaHR0cDovL2NhLmJ0Yy9wa2kvcHViL2NlcnQvYnRjLmNydDAwBglghkgBhvhCAQQEIxYhaHR0cDovL2NhLmJ0Yy9wa2kvcHViL2NybC9idGMuY3JsMDAGCWCGSAGG+EIBAwQjFiFodHRwOi8vY2EuYnRjL3BraS9wdWIvY3JsL2J0Yy5jcmwwXgYDVR0fBFcwVTAnoCWgI4YhaHR0cDovL2NhLmJ0Yy9wa2kvcHViL2NybC9idGMuY3JsMCqgKKAmhiRodHRwOi8vY2EuZ292LnZuL3BraS9wdWIvY3JsL2J0Yy5jcmwwDQYJKoZIhvcNAQEFBQADggEBACazYSs4oCC9klzaHCCNoJaR1D78QaI8cdm+k4hGrvq+LoL5Y7CbSFF0erj/2ycmto5zh3KVTV6mhfbzWxTmumEqrSB5BgHEmUKTSNAMbISxZlqv+q1ZZ4e08dXxKFwFQesPypVocJgGP0yYTJO9+zpN79od9s6fP25ZK0vTHQE98rt6pAeNnMUrJfqDAvUpCXPsAVHLkum8WbN0URQLHNCstPmCwxAZzravhETFmLXbO9/d0oTTZkxgFAfH5Zk49TBlNxPng9FVVOqFmMuU/F6hJmI7IQUwS7AM4JOCxBk+Ha3TfXDtYO/BwjtYByOFdHXqyohSgcHM+2m1tXcvV94=</X509Certificate></X509Data></KeyInfo></Signature></CUSTOM>";
        soapElement2.addTextNode(verifyStr);

        soapMessage.saveChanges();
//        System.out.println("----------SOAP Request------------");
//        soapMessage.writeTo(System.out);
//        System.out.println("----------SOAP Request------------");

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        String url = "http://10.224.128.93/Public_x64/VerifyDS.asmx";
        SOAPMessage soapResponse = soapConnection.call(soapMessage, url);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String strMsg = new String(out.toByteArray());
        System.out.println(strMsg);
    }

}
