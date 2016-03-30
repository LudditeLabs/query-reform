/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xalan.xsltc.compiler.XSLTC;

/**
 * @author Morten Jorgensen
 * @author Jacek Ambroziak
 */
public class CompileServlet extends HttpServlet {

    /**
     * Main servlet entry point. The servlet reads a stylesheet from the
     * URI specified by the "sheet" parameter. The compiled Java class
     * ends up in the CWD of the web server (a better solution would be
     * to have an environment variable point to a translet directory).
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String stylesheetName = request.getParameter("sheet");
        out.println("<html><head>");
        out.println("<title>Servlet Stylesheet Compilation</title>");
        out.println("</head><body>");
        if (stylesheetName == null) {
            out.println("<h1>Compilation error</h1>");
            out.println("The parameter <b><tt>sheet</tt></b> " + "must be specified");
        } else {
            XSLTC xsltc = new XSLTC();
            xsltc.init();
            xsltc.compile(new URL(stylesheetName));
            out.println("<h1>Compilation successful</h1>");
            out.println("The stylesheet was compiled into the translet " + "class " + xsltc.getClassName() + " and is now " + "available for transformations on this server.");
        }
        out.println("</body></html>");
    }
}
