package com.company;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

public class Main {
    private static SOAPMessage createSoapRequest() throws Exception{
//        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration("tem", "http://tempuri.org/");
        soapEnvelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");

        SOAPBody soapBody = soapEnvelope.getBody();

        SOAPElement soapElement = soapBody.addChildElement("XmlSigning", "tem");
        SOAPElement element1 = soapElement.addChildElement("Input", "tem");
        SOAPElement element2 = soapElement.addChildElement("Key", "tem");

        element1.addTextNode("EveryOne");
        element2.addTextNode("1");

        soapMessage.saveChanges();
        System.out.println("----------SOAP Request------------");
        soapMessage.writeTo(System.out);
        return soapMessage;
    }
    private static void createSoapResponse(SOAPMessage soapResponse) throws Exception  {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();

        System.out.println("\n----------SOAP Response-----------");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }

    public static Document loadXMLString(String response) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(response));

        return db.parse(is);
    }


    public static void main(String[] args) {
        System.out.println("Start");
        try{
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            String url = "http://10.224.128.93/DSService/DSServiceV3.asmx";
            SOAPMessage soapRequest = createSoapRequest();
            //hit soapRequest to the server to get response
            SOAPMessage soapResponse = soapConnection.call(soapRequest, url);

//            final NodeList childNodes = soapResponse.getSOAPBody().getElementsByTagName("error-response");
//            final NodeList childNodes = soapResponse.getSOAPPart().getElementsByTagName("error-response");

            System.out.println("check");
//            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
//            messageFactory.
//            SOAPBody body = soapResponse.getSOAPBody();
//            String data = body.getAttribute("XmlSigningResponse");
//
//            System.out.println(data);
//            Source src = soapResponse.getSOAPPart().getContent();
//            System.out.println(src);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            String strMsg = new String(out.toByteArray());
            System.out.println(strMsg);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(strMsg));

            Document doc = builder.parse(src);
            String value = doc.getElementsByTagName("web:XmlSigningResponse").item(0).getTextContent();


            System.out.println(value);

//            createSoapResponse(soapResponse);
            soapConnection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
