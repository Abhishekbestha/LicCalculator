package com.example.checklic.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author abhi
 */
public class APIcall {

    public static String executePostParameters(String targetURL, String urlParameters) throws IOException, Exception {
        try {
            if (targetURL.matches("^(https)://.*$")) {
                return executePostHttps(targetURL, urlParameters);
            } else {
                return executePostHttp(targetURL, urlParameters);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static String executePostHttps(String targetURL, String urlParameters) throws IOException, Exception {

        URL url;
        HttpsURLConnection connection = null;
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(new KeyManager[0],
                    new TrustManager[]{new DummyTrustManager()},
                    new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw e;
        }
        try {
            //Create posttohttpurlconnection
            SSLSocketFactory factory = sslcontext.getSocketFactory();
            url = new URL(targetURL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

//            connection.setRequestProperty("Authorization", authorization);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setSSLSocketFactory(factory);
            connection.setHostnameVerifier(new DummyHostnameVerifier());

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
//            int respcode = connection.getResponseCode();
//            apicallrespcode obj = new apicallrespcode();
//            obj.setRespcode(respcode);
            return response.toString();

        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String executePostHttp(String targetURL, String formData) throws IOException, Exception {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // Set form data content type
            // Other headers...

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(formData);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getIsoDateIst() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static Integer generatetransactionId() {
        return Integer.parseInt(Long.toString(new Date().getTime() / 1000));

    }

    public static class DummyTrustManager implements X509TrustManager {

        public DummyTrustManager() {
        }

        public boolean isClientTrusted(X509Certificate cert[]) {
            return true;
        }

        public boolean isServerTrusted(X509Certificate cert[]) {
            return true;
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }
    }

    public static class DummyHostnameVerifier implements HostnameVerifier {

        public boolean verify(String urlHostname, String certHostname) {
            return true;
        }

        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }
    public static String executePostHttpFormData(String targetURL, Map<String, String> fields) throws IOException {
        URL url;
        HttpURLConnection connection = null;
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n"; // Line separator required by multipart/form-data

        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            try (OutputStream output = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)) {

                // Write each form field
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + StandardCharsets.UTF_8).append(CRLF);
                    writer.append(CRLF).append(entry.getValue()).append(CRLF).flush();
                }

                // End of multipart/form-data
                writer.append("--" + boundary + "--").append(CRLF).flush();
            }

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
