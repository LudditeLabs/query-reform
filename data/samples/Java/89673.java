/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.videostore.model.Article;
import com.videostore.model.ArticleFactory;
import com.videostore.model.ArticleInProduct;
import com.videostore.model.Product;
import com.videostore.model.PropertyInfo;
import com.videostore.model.User;
import com.videostore.model.WareHouse;

public class ArticleAdd extends GenericServlet {

    @Override
    protected void doGetProtected(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String prodId = request.getParameter("prodId");
        User user = getCurrentUser(request);
        WareHouse w = WarehouseFactory.load();
        Product p = w.getProducts().get(prodId);
        Article a = new Article("DEFAULT_ARTICLE", 10.0, "NOME");
        a.setPuntiPromo(0);
        p.getArticles().put(a.getId(), new ArticleInProduct(1, a));
        Page page = new Page();
        page.setUser(user);
        page.setWarehouse(w);
        page.setXslt("E:/Article/add.xslt");
        page.sectionAdd("article", "artRef", a);
        page.print(out);
    }

    @Override
    protected void doPostProtected(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String prodId = request.getParameter("prodId");
        String artId = request.getParameter("nuovoArtId");
        String name = request.getParameter("name");
        double price;
        int amount;
        int pp;
        String currentProp = null;
        try {
            currentProp = "Prezzo";
            price = Double.parseDouble(request.getParameter("price"));
            if (price <= 0) throw new NumberFormatException();
            currentProp = "Quantita'";
            amount = Integer.parseInt(request.getParameter("amount"));
            if (amount < 0) throw new NumberFormatException();
            currentProp = "Punti Promo";
            pp = Integer.parseInt(request.getParameter("puntipromo"));
            if (pp < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            ErrorPage.createPropFormat(currentProp).print(out);
            return;
        }
        WareHouse w = WarehouseFactory.load();
        Product p = w.getProducts().get(prodId);
        if (p.getArticles().containsKey(artId)) {
            Page pe = ErrorPage.create();
            pe.sectionError("ID gia presente", "Id articolo gia presente");
            pe.print(out);
            return;
        }
        Article a = new ArticleFactory(p).create(artId, price, name);
        a.setPuntiPromo(pp);
        for (Entry<String, PropertyInfo> i : p.getPropertiesInfo().entrySet()) {
            String v = request.getParameter(i.getKey());
            if ((v != null) && (v != "")) {
                try {
                    Object o = i.getValue().parse(v);
                    a.getProperties().setProperty(i.getKey(), o);
                } catch (ParseException e) {
                    ErrorPage.createPropFormat(i.getValue().getName()).print(out);
                    return;
                }
            }
        }
        ArticleInProduct aip = new ArticleInProduct(amount, a);
        p.getArticles().put(aip.getId(), aip);
        WarehouseFactory.save(w);
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/Article/Add?prodId=" + prodId));
    }
}
