package com.example.checklic.controller;

import com.example.checklic.utils.APIcall;
import com.itextpdf.html2pdf.HtmlConverter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@Component
@Service
public class Controllers {

    @RequestMapping(value = "/GetHtml", method = RequestMethod.POST)
    protected ResponseEntity GetHtml(HttpServletRequest request) throws Exception {
        String Value = request.getParameter("planId");
        String url = "https://www.licpremiumcalculator.in/forms/plan"+Value+".php";
        String resp = APIcall.executePostHttps(url,"");
        return new ResponseEntity(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "/Html2Pdf", method = RequestMethod.POST)
    protected ResponseEntity Html2Pdf(HttpServletRequest request) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        File pdfFile = null;
        String base64Pdf = null;
        //            InputStream inputStream = request.getInputStream();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead;
//                while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            } else {
//                stringBuilder.append("");
//            }
//        String htmlContent = stringBuilder.toString();
//        String boundary = "------" + htmlContent.split("------")[1].split("\r\n")[0];
//        String lineToRemove = "Content-Disposition: form-data; name=\"htmlContent\"";
//        htmlContent = htmlContent.replace(boundary, "").replace(lineToRemove, "");

        String htmlContent = request.getParameter("htmlContent");

        String htmlCss = "<style> \n"
                + ".table {\n"
                + "                width: 100%;\n"
                + "                border-collapse: collapse;\n"
                + "                margin-bottom: 20px;\n"
                + "                text-align: center;\n"
                + "                margin: 0 auto;\n"
                + "            }\n"
                + "\n"
                + "            .table th,\n"
                + "            .table td {\n"
                + "                border: 1px solid #ddd;\n"
                + "                padding: 8px;\n"
                + "            }\n"
                + "\n"
                + "            .table th {\n"
                + "                background-color: #333;\n"
                + "                color: #fff;\n"
                + "            }\n"
                + "\n"
                + "            .table tr:nth-child(even) {\n"
                + "                background-color: #f2f2f2;\n"
                + "            }\n"
                + "\n"
                + "            .table tr:hover {\n"
                + "                background-color: #ddd;\n"
                + "            }\n"
                + "</style>";

        String finalHtml = htmlContent + htmlCss;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(finalHtml, outputStream);
        byte[] pdfBytes = outputStream.toByteArray();

        base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
        return new ResponseEntity(base64Pdf, HttpStatus.OK);
    }

    @RequestMapping(value = "/LicCheck", method = RequestMethod.POST)
    protected ResponseEntity LicCheck(HttpServletRequest request) throws Exception {
        String table = ("0".equals(request.getParameter("table")) || request.getParameter("table") == null) ? "914" : request.getParameter("table");
        String sa = request.getParameter("sa");
        String age = request.getParameter("age");
        String term = request.getParameter("term");
        String dab = request.getParameter("dab");
        String termRd = request.getParameter("termRd");
        Map<String, String> formData = new HashMap<>();
        formData.put("table", table);
        formData.put("sa", sa);
        formData.put("age", age);
        formData.put("term", term);
        formData.put("dab", dab);
        formData.put("termRd", termRd);
        String url = "https://www.licpremiumcalculator.in/calc/calc" + table + ".php";
        String resp = APIcall.executePostHttpFormData(url, formData);
        return new ResponseEntity(resp, HttpStatus.OK);
    }

}
