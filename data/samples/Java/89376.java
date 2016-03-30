/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.sql.ResultSet;
import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

/**
 * The ReportCopier class copies an existing report to multiple reports,
 * based on the open incident selection criteria passed to the buildReports
 * method.  The criteria are set by ORing the class flags as shown below:
 * <ul>
 *   <li>Source address (always selected automatically)</li>
 *   <li>Destination address: Flag = copyDestAddr</li>
 *   <li>Destination port: Flag = copyDestPort</li>
 *   <li>Site: Flag = copySite</li>
 * </ul>
 */
public class ReportCopier {

    private Shell reportCopierWindow = null;

    private Text copyLog;

    private AnalyzeTab analyzeTab = null;

    private TrendsTab trendsTab = null;

    private int incidentSelectedID = 0;

    private int selectCriteria = 0;

    protected static final int copyDestAddr = 0x01;

    protected static final int copyDestPort = 0x02;

    protected static final int copySite = 0x04;

    /**
 * The constructor for the ReportCopier class sets the primary key of the
 * Incident/Report to be copied.
 *
 * @param incidentKey	The primary key of the incident to be copied.
 * @param selectFlag	The ORed values indicating which selection criteria to use
 * 						for getting the list of incidents to have an Incident Report
 * 						copied to them.
 * @param analyzeTab	The analyze Tab from which the call was made.  This allows
 * 						the status icon to be updated.
 */
    public ReportCopier(int incidentID, int selectFlag, AnalyzeTab analyzeTab) {
        this.incidentSelectedID = incidentID;
        this.selectCriteria = selectFlag;
        this.analyzeTab = analyzeTab;
        this.reportCopierWindow = new Shell();
        reportCopierWindow.setText("Report Copier");
        reportCopierWindow.setSize(500, 400);
        reportCopierWindow.setLayout(new FillLayout());
        this.copyLog = new Text(reportCopierWindow, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        this.copyLog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        this.copyLog.setRedraw(true);
        this.reportCopierWindow.open();
        this.buildReports();
    }

    /**
 * The constructor for the ReportCopier class sets the primary key of the
 * Incident/Report to be copied.
 *
 * @param incidentKey	The primary key of the incident to be copied.
 * @param selectFlag	The ORed values indicating which selection criteria to use
 * 						for getting the list of incidents to have an Incident Report
 * 						copied to them.
 * @param analyzeTab	The analyze Tab from which the call was made.  This allows
 * 						the status icon to be updated.
 */
    public ReportCopier(int incidentID, int selectFlag, TrendsTab trendsTab) {
        this.incidentSelectedID = incidentID;
        this.selectCriteria = selectFlag;
        this.trendsTab = trendsTab;
        this.reportCopierWindow = new Shell();
        reportCopierWindow.setText("Report Copier");
        reportCopierWindow.setSize(500, 400);
        reportCopierWindow.setLayout(new FillLayout());
        this.copyLog = new Text(reportCopierWindow, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        this.copyLog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        this.copyLog.setRedraw(true);
        this.reportCopierWindow.open();
        this.buildReports();
    }

    /**
 * Get the source report information.  Then get the list of incidents to
 * have reports created, and build those reports.
 */
    public void buildReports() {
        String sqlStatement = "";
        String rptSev = "", rptSrcAddr = "", rptSrcName = "", rptProto = "", rptNotes = "", rptSrcWhois = "", rptSrcCountry = "";
        String incID, incSite, incDate, incSrcPort, incDestAddr, incDestPort, incDestName, incEvents, incDestWhois, incDestCountry;
        boolean success = true;
        int iStmts = 0;
        String verifySelectList, selectList = "  ";
        ResultSet reportData = Globals.gDatabase.query("SELECT * FROM reports WHERE rpts_unique_id_nr = " + this.incidentSelectedID);
        String sqlInsertReport = "INSERT INTO reports (rpts_unique_id_nr, sites_unique_id_nr, desc_severity_unique_id_nr, " + "rpts_date_dt, rpts_insert_date_dt, rpts_hostile_ip, rpts_hostile_port_nr, " + "rpts_hostile_name_tx, rpts_target_ip, rpts_target_port_nr, rpts_target_name_tx, " + "rpts_protocol_tx, rpts_events_tx, rpts_notes_tx, rpts_hstl_whois_tx, " + "rpts_trgt_whois_tx, rpts_hostile_cntry_tx, rpts_target_cntry_tx) VALUES (";
        if (reportData != null) {
            try {
                if (reportData.next()) {
                    rptSev = reportData.getString("desc_severity_unique_id_nr");
                    rptSrcAddr = reportData.getString("rpts_hostile_ip");
                    selectList += rptSrcAddr;
                    rptSrcName = reportData.getString("rpts_hostile_name_tx");
                    rptProto = reportData.getString("rpts_protocol_tx");
                    rptNotes = reportData.getString("rpts_notes_tx");
                    rptSrcWhois = reportData.getString("rpts_hstl_whois_tx").replaceAll("'", "''");
                    rptSrcCountry = reportData.getString("rpts_hostile_cntry_tx");
                    sqlStatement += "SELECT * FROM incidents WHERE incdnt_reported_fl = 0 AND incdnt_hostile_ip = '" + rptSrcAddr + "'";
                    if ((this.selectCriteria & copyDestAddr) != 0) {
                        sqlStatement += " AND incdnt_target_ip = '" + reportData.getString("rpts_target_ip") + "'";
                        selectList += " -> " + reportData.getString("rpts_target_ip");
                    }
                    if ((this.selectCriteria & copyDestPort) != 0) {
                        sqlStatement += " AND incdnt_target_port_nr = " + reportData.getString("rpts_target_port_nr");
                        selectList += " - " + reportData.getString("rpts_target_port_nr");
                    }
                    if ((this.selectCriteria & copySite) != 0) {
                        sqlStatement += " AND sites_unique_id_nr = " + reportData.getString("sites_unique_id_nr");
                        selectList += ", " + Globals.gDatabase.singleValue("SELECT sites_name_abrv_tx FROM sites WHERE sites_unique_id_nr = " + reportData.getString("sites_unique_id_nr"));
                    }
                }
            } catch (SQLException error) {
                String errorMsg = "Error getting the original report data:\n" + error;
                Globals.gUtility.errorDialog(this.reportCopierWindow, "ERROR", errorMsg, Globals.gDescrDBerr);
                return;
            }
        } else {
            String errorMsg = "Error getting the original report data:\n" + Globals.gDatabase.getLastError();
            Globals.gUtility.errorDialog(this.reportCopierWindow, "ERROR", errorMsg, Globals.gDescrDBerr);
        }
        if (sqlStatement.length() == 0) {
            Globals.gUtility.progressDialog(copyLog, "\nNo Incident Reports were selected to be copied\n\n");
            return;
        }
        verifySelectList = "The selected Incident Report will be copied to all incidents matching the following criteria:\n\n";
        verifySelectList += "  Source IP address\n";
        if ((this.selectCriteria & this.copyDestAddr) != 0) verifySelectList += "  Destination IP address\n";
        if ((this.selectCriteria & this.copyDestPort) != 0) verifySelectList += "  Destination Port\n";
        if ((this.selectCriteria & this.copySite) != 0) verifySelectList += "  Reporting Site\n";
        verifySelectList += "\nIs this correct?";
        if (!Globals.gUtility.confirmClose(this.reportCopierWindow, "COPY REPORT", verifySelectList)) {
            Globals.gUtility.progressDialog(copyLog, "\nNo Incident Reports were copied\n\n");
            return;
        }
        Globals.gUtility.progressDialog(copyLog, "\tCopy incident report for:\n" + selectList + "\n\n");
        Globals.gDatabase.execute("START TRANSACTION");
        reportData = Globals.gDatabase.query(sqlStatement);
        if (reportData != null) {
            try {
                while (reportData.next()) {
                    incID = reportData.getString("incdnt_unique_id_nr");
                    incSite = reportData.getString("sites_unique_id_nr");
                    incDate = reportData.getString("incdnt_date_dt");
                    incSrcPort = reportData.getString("incdnt_hostile_port_nr");
                    incDestAddr = reportData.getString("incdnt_target_ip");
                    incDestPort = reportData.getString("incdnt_target_port_nr");
                    incEvents = this.buildEvents();
                    incDestName = Globals.gUtility.DNSResolve(incDestAddr);
                    incDestWhois = Globals.gUtility.Whois(incDestAddr).replaceAll("'", "''");
                    incDestCountry = Globals.gDatabase.singleValue("SELECT cntry_name_abrv_tx FROM countries LIMIT(1)");
                    if (incDestCountry == null) incDestCountry = "er";
                    sqlStatement = sqlInsertReport + incID + "," + incSite + "," + rptSev + ", '" + incDate + "', CURRENT_DATE, '" + rptSrcAddr + "'," + incSrcPort + ", '" + rptSrcName + "', '" + incDestAddr + "'," + incDestPort + ", '" + incDestName + "', '" + rptProto + "', '" + incEvents + "', '" + rptNotes + "', '" + rptSrcWhois + "', '" + incDestWhois + "', '" + rptSrcCountry + "', '" + incDestCountry + "')";
                    if (Globals.gDatabase.execute(sqlStatement) == -1) {
                        success = false;
                        Globals.gUtility.progressDialog(copyLog, "Insert Incident Report statement:\n" + sqlStatement + "\n");
                        Globals.gUtility.progressDialog(copyLog, "Database error:\n" + Globals.gDatabase.getLastError() + "\n");
                        break;
                    }
                    sqlStatement = "UPDATE incidents SET incdnt_reported_fl = 1, desc_severity_unique_id_nr = " + rptSev + " WHERE incdnt_unique_id_nr = " + incID;
                    if (Globals.gDatabase.execute(sqlStatement) == -1) {
                        success = false;
                        Globals.gUtility.progressDialog(copyLog, "Update Incident statement:\n" + sqlStatement + "\n");
                        Globals.gUtility.progressDialog(copyLog, "Database error:\n" + Globals.gDatabase.getLastError() + "\n");
                        break;
                    }
                    if (this.analyzeTab != null) this.analyzeTab.updateIncidentIcon(Integer.parseInt(incID), Integer.parseInt(rptSev)); else if (this.trendsTab != null) this.trendsTab.updateIncidentIcon(Integer.parseInt(incID), Integer.parseInt(rptSev));
                    iStmts++;
                    if ((iStmts % 40) == 0) Globals.gUtility.progressDialog(copyLog, "\n"); else if ((iStmts % 10) == 0) Globals.gUtility.progressDialog(copyLog, "|"); else if ((iStmts % 5) == 0) Globals.gUtility.progressDialog(copyLog, "+"); else Globals.gUtility.progressDialog(copyLog, ".");
                }
            } catch (SQLException error) {
                String errorMsg = "Error creating incident copies:\n" + error;
                Globals.gUtility.errorDialog(this.reportCopierWindow, "ERROR", errorMsg, Globals.gDescrDBerr);
                success = false;
            }
        } else {
            String errorMsg = "Error getting copied incident data:\n" + Globals.gDatabase.getLastError();
            Globals.gUtility.errorDialog(this.reportCopierWindow, "ERROR", errorMsg, Globals.gDescrDBerr);
        }
        if (success) {
            if (iStmts > 0) {
                if (Globals.gDatabase.execute("COMMIT") != -1) {
                    Globals.gUtility.progressDialog(copyLog, "\nIncident Reports created: " + iStmts + "\n\n");
                } else {
                    Globals.gUtility.progressDialog(copyLog, "Error committing changes:\n" + Globals.gDatabase.getLastError() + "\n");
                }
            } else Globals.gUtility.progressDialog(copyLog, "\nNo Incident Reports were created\n\n");
        } else {
            Globals.gDatabase.execute("ROLLBACK");
            Globals.gUtility.progressDialog(copyLog, "\nNo Incident Reports were stored in the database\n\n");
        }
        return;
    }

    /**
 * Build the Events/Actions/Triggers of an incident for storage in a report.
 */
    private String buildEvents() {
        String eventText = "";
        ResultSet events = null;
        ResultSet actions = null;
        ResultSet triggers = null;
        try {
            events = Globals.gDatabase.query("SELECT * FROM rules_event AS e, incident_event AS ie WHERE e.rules_evt_unique_id_nr = ie.rules_evt_unique_id_nr AND ie.incdnt_unique_id_nr = " + this.incidentSelectedID);
            while (events.next()) {
                eventText += events.getString("rules_evt_desc_unique_tx") + "\tAction Weight: " + events.getString("incdntevt_act_wght_nr") + "\r\n";
                actions = Globals.gDatabase.query("SELECT * FROM rules_action AS a, incident_activity AS ia WHERE a.rules_act_unique_id_nr = ia.rules_act_unique_id_nr AND ia.incdntevt_unique_id_nr = " + events.getInt("incdntevt_unique_id_nr"));
                while (actions.next()) {
                    eventText += "\t" + actions.getString("rules_act_desc_unique_tx") + "\tTrigger Weight: " + actions.getString("incdntevt_trgr_wght_nr") + "\r\n";
                    triggers = Globals.gDatabase.query("SELECT * FROM rules_trigger AS t, incident_trigger AS it WHERE t.rules_trig_unique_id_nr = it.rules_trig_unique_id_nr AND it.incdntact_unique_id_nr = " + actions.getInt("incdntact_unique_id_nr"));
                    while (triggers.next()) {
                        eventText += "\t\t" + triggers.getString("rules_trig_desc_unique_tx") + "\r\n";
                    }
                }
                eventText += "\r\n";
            }
        } catch (SQLException err) {
            String errorMsg = "Error building event data:\n" + err;
            Globals.gUtility.errorDialog(this.reportCopierWindow, " ERROR", errorMsg, Globals.gDescrDBerr);
        }
        return eventText;
    }
}
