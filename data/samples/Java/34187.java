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
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: Lab
 *
 */
public class Lab extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    public Lab() {
        super();
    }

    PrintWriter out;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");
        out = response.getWriter();
        String a = request.getParameter("a").toString();
        if (a.matches("LabEkle")) this.LabEkle(request.getParameter("labID"), request.getParameter("labAdi"), request.getParameter("labGrupID")); else if (a.matches("LabSil")) this.LabSil(request.getParameter("labID")); else if (a.matches("LabIstekEkle")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date tarih = null;
            java.sql.Date date = null;
            Date utilDate1 = null;
            java.sql.Time sqlTime = null;
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss");
            try {
                tarih = df.parse(request.getParameter("labIstekTarihi"));
                date = new java.sql.Date(tarih.getTime());
                utilDate1 = df1.parse(request.getParameter("labIstekSaati"));
                sqlTime = new java.sql.Time(utilDate1.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.LabIstekEkle(request.getParameter("labID"), date, request.getParameter("hekimKimlikNo"), sqlTime);
        } else if (a.matches("LabIstekIslemEkle")) this.LabIstekIslemEkle(request.getParameter("labTakipNo"), Integer.parseInt(request.getParameter("islemID"))); else if (a.matches("LabIstekSil")) this.LabIstekSil(request.getParameter("labTakipNo")); else if (a.matches("LabIstekListele")) {
            if (request.getParameter("kayitSayisi") != null && request.getParameter("hastaKimlikNo") != null) this.LabIstekListele(Integer.parseInt(request.getParameter("kayitSayisi")), request.getParameter("hastaKimlikNo")); else {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date tarih = null;
                java.sql.Date date = null;
                try {
                    tarih = df.parse(request.getParameter("labIstekTarihi"));
                    date = new java.sql.Date(tarih.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                this.LabIstekListele(date, request.getParameter("hastaKimlikNo"));
            }
        }
    }

    /**
	 * LAB tablosuna,
	 * Yeni satir ekler
	 * @param labID
	 * @param labAdi
	 * @param labGrupID
	 */
    public void LabEkle(String labID, String labAdi, String labGrupID) {
        LabEkleJ myLab = new LabEkleJ();
        myLab.setVariables(labID, labAdi, labGrupID);
        try {
            myLab.execute();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<root>");
            out.print("<status>");
            out.print("OK");
            out.println("</status>");
            out.print("<labid>");
            out.print(labID);
            out.println("</labid>");
            out.print("<labadi>");
            out.print(labAdi);
            out.println("</labadi>");
            out.print("<labgrupid>");
            out.print(labGrupID);
            out.println("</labgrupid>");
        } catch (Exception e) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<status>");
            out.print("ERROR");
            out.println("</status>");
        } finally {
            try {
                myLab.close();
                out.print("<connection>");
                out.print("closed");
                out.println("</connection>");
            } catch (SQLException e) {
                out.print("<connection>");
                out.print("open");
                out.println("</connection>");
            }
            out.println("</root>");
            out.close();
        }
    }

    /**
	 * LAB tablosundan,
	 * Ilgili lab bilgisini siler
	 * @param labID
	 */
    public void LabSil(String labID) {
        LabSilJ myLab = new LabSilJ();
        myLab.setVariables(labID);
        try {
            myLab.execute();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<root>");
            out.print("<status>");
            out.print("OK");
            out.println("</status>");
            out.print("<labid>");
            out.print(labID);
            out.println("</labid>");
        } catch (Exception e) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<status>");
            out.print("ERROR");
            out.println("</status>");
        } finally {
            try {
                myLab.close();
                out.print("<connection>");
                out.print("closed");
                out.println("</connection>");
            } catch (SQLException e) {
                out.print("<connection>");
                out.print("open");
                out.println("</connection>");
            }
            out.println("</root>");
            out.close();
        }
    }

    /**
			 * LABISTEK tablosuna,
			 * Yeni lab randevusu ekler
			 * @param labTakipNo
			 * @param labID
			 * @param labIstekTarihi
			 * @param hekimKimlikNo
			 * @param labSaati
			 */
    public void LabIstekEkle(String labID, java.sql.Date labIstekTarihi, String hekimKimlikNo, java.sql.Time labSaati) {
        LabIstekEkleJ myLab = new LabIstekEkleJ();
        myLab.setVariables(labID, labIstekTarihi, hekimKimlikNo, labSaati);
        try {
            myLab.execute();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<root>");
            out.print("<status>");
            out.print("OK");
            out.println("</status>");
            out.print("<labtakipno>");
            out.print(myLab.getLabTakipNo());
            out.println("</labtakipno>");
            out.print("<labid>");
            out.print(labID);
            out.println("</labid>");
            out.print("<labistektarihi>");
            out.print(labIstekTarihi);
            out.println("</labistektarihi>");
            out.print("<hekimkimlikno>");
            out.print(hekimKimlikNo);
            out.println("</hekimkimlikno>");
            out.print("<labsaati>");
            out.print(labSaati);
            out.println("</labsaati>");
        } catch (Exception e) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<status>");
            out.print("ERROR");
            out.println("</status>");
        } finally {
            try {
                myLab.close();
                out.print("<connection>");
                out.print("closed");
                out.println("</connection>");
            } catch (SQLException e) {
                out.print("<connection>");
                out.print("open");
                out.println("</connection>");
            }
            out.println("</root>");
            out.close();
        }
    }

    /**
			 * LABISTEK tablosuna,
			 * Hastanin geldigi gun Islem no ekler
			 * @param labTakipNo
			 * @param islemID
			 */
    public void LabIstekIslemEkle(String labTakipNo, Integer islemID) {
        LabIstekIslemEkleJ myLab = new LabIstekIslemEkleJ();
        myLab.setVariables(labTakipNo, islemID);
        try {
            myLab.execute();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<root>");
            out.print("<status>");
            out.print("OK");
            out.println("</status>");
            out.print("<labtakipno>");
            out.print(labTakipNo);
            out.println("</labtakipno>");
            out.print("<islemid>");
            out.print(islemID);
            out.println("</islemid>");
        } catch (Exception e) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<status>");
            out.print("ERROR");
            out.println("</status>");
        } finally {
            try {
                myLab.close();
                out.print("<connection>");
                out.print("closed");
                out.println("</connection>");
            } catch (SQLException e) {
                out.print("<connection>");
                out.print("open");
                out.println("</connection>");
            }
            out.println("</root>");
            out.close();
        }
    }

    /**
			 * LABISTEK tablosundan,
			 * Lab randevusunu siler
			 * @param labTakipNo
			 */
    public void LabIstekSil(String labTakipNo) {
        LabIstekSilJ myLab = new LabIstekSilJ();
        myLab.setVariables(labTakipNo);
        try {
            myLab.execute();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<root>");
            out.print("<status>");
            out.print("OK");
            out.println("</status>");
            out.print("<labtakipno>");
            out.print(labTakipNo);
            out.println("</labtakipno>");
        } catch (Exception e) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<status>");
            out.print("ERROR");
            out.println("</status>");
        } finally {
            try {
                myLab.close();
                out.print("<connection>");
                out.print("closed");
                out.println("</connection>");
            } catch (SQLException e) {
                out.print("<connection>");
                out.print("open");
                out.println("</connection>");
            }
            out.println("</root>");
            out.close();
        }
    }

    public void LabIstekListele(int kayitSayisi, String hastaKimlikNo) {
    }

    /**
			 * LABISTEK tablosundan,
			 * Bir hastanin belli bir tarihteki lab bilgilerini listeler
			 * @param labTarihi
			 * @param hastaKimlikNo
			 */
    public void LabIstekListele(java.sql.Date labTarihi, String hastaKimlikNo) {
        LabIstekListeleJ myLab = new LabIstekListeleJ();
        myLab.setVariables(labTarihi, hastaKimlikNo);
        try {
            myLab.execute();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<root>");
            out.print("<status>");
            out.print("OK");
            out.println("</status>");
            while (myLab.next()) {
                out.print("<labtarihi>");
                out.print(labTarihi);
                out.println("</labtarihi>");
                out.print("<doktoradi>");
                out.print(myLab.getDOKTOR_AD());
                out.println("</doktoradi>");
                out.print("<labtakipno>");
                out.print(myLab.getLABISTEK_LABTAKIPNO());
                out.println("</labtakipno>");
                out.print("<labadi>");
                out.print(myLab.getLAB_LABADI());
                out.println("</labadi>");
                out.print("<labgrupadi>");
                out.print(myLab.getLABGRUP_GRUPADI());
                out.println("</labgrupadi>");
                out.print("<labsaati>");
                out.print(myLab.getLABISTEK_LABSAATI());
                out.println("</labsaati>");
                out.print("<hastakimlikno>");
                out.print(hastaKimlikNo);
                out.println("</hastakimlikno>");
            }
        } catch (Exception e) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<status>");
            out.print("ERROR");
            out.println("</status>");
        } finally {
            try {
                myLab.close();
                out.print("<connection>");
                out.print("closed");
                out.println("</connection>");
            } catch (SQLException e) {
                out.print("<connection>");
                out.print("open");
                out.println("</connection>");
            }
            out.println("</root>");
            out.close();
        }
    }
}
