package com.example.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import com.example.file.model.SoapValidateResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class SoapClient {

    @Value("${soap.service.url}")
    private String soapUrl;

    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    public SoapValidateResult validateToken(String token) {
        String requestXml =
            "<ValidateTokenRequest xmlns='http://example.com/auth'>" +
                "<token>" + token + "</token>" +
            "</ValidateTokenRequest>";
        try {
            StreamSource source = new StreamSource(new StringReader(requestXml));
            StringWriter writer = new StringWriter();
            webServiceTemplate.sendSourceAndReceiveToResult(
                soapUrl, source, new StreamResult(writer)
            );
            String xml = writer.toString();

            if (xml.contains("<valid>true</valid>")) {
                String userId = extractTag(xml, "userId");
                String role = extractTag(xml, "role");
                return new SoapValidateResult(userId, role);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractTag(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        int end = xml.indexOf(close);
        if (start == -1 || end == -1) {
            open = ":" + tag + ">";
            start = xml.indexOf(open);
            end = xml.indexOf("</", start);
        }
        if (start == -1 || end == -1) return null;
        return xml.substring(start + open.length(), end);
    }
}