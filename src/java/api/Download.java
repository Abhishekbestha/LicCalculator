package api;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 21701
 */
@WebServlet("/Download")
public class Download extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String filename1 = "D:\\Servers\\apache-tomcat-9.0.64\\bin\\output.pdf";
            String pdfPath = request.getParameter("filename");
            byte[] pdf = Files.readAllBytes(Paths.get(filename1));
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=Signed.pdf");
            response.getOutputStream().write(pdf);
        } catch (Exception ex) {
            System.out.println(ex);
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
    }

}
