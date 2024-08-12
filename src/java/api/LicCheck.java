package api;


import utils.APIcall;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 21701
 */
public class LicCheck extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
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
        response.getWriter().print(resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(LicCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(LicCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
