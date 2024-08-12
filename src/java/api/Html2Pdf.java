package api;


import com.itextpdf.html2pdf.HtmlConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 21701
 */
public class Html2Pdf extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }

            String htmlContent = stringBuilder.toString();
            String boundary = "------" + htmlContent.split("------")[1].split("\r\n")[0];
            String lineToRemove = "Content-Disposition: form-data; name=\"htmlContent\"";
            htmlContent = htmlContent.replace(boundary, "").replace(lineToRemove, "");
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

            String outputPdfFile = "output.pdf";
            File pdfFile = new File(outputPdfFile);
            try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
                HtmlConverter.convertToPdf(finalHtml, outputStream);
                response.getWriter().print(pdfFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
