/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class AdministratorGUI extends JFrame {

    private JPanel pnlMain;

    private CardLayout card;

    private JPanel pnlAdminHome;

    private JLabel lblWelcome;

    private Font largeFont = new Font("Tahoma", Font.PLAIN, 24);

    private JPanel pnlLeft;

    private JPanel pnlRight;

    private JPanel pnlButtons;

    private JButton btnReports;

    private JButton btnSetUpSemester;

    private JButton btnAddStudentFile;

    private JButton btnLogOut;

    private static final int LEFT_PANEL_WIDTH = 175;

    private static File userFile;

    private File studentCourseFile;

    public AdministratorGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        setResizable(false);
        setTitle("BearTrac");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 640, 480);
        WindowListener listener = new WindowAdapter() {

            public void windowClosing(WindowEvent w) {
                System.exit(0);
            }
        };
        addWindowListener(listener);
        card = new CardLayout();
        pnlMain = new JPanel();
        pnlMain.setLayout(card);
        pnlMain.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pnlMain);
        createAdminHomePanel();
        createReportPanel();
    }

    private void closeAndDispose() {
        this.setVisible(false);
        this.dispose();
    }

    /**
	 * Sets up the Administrators home panel
	 */
    private void createAdminHomePanel() {
        pnlAdminHome = new JPanel();
        pnlMain.add(pnlAdminHome, "pnlAdminHome");
        pnlAdminHome.setLayout(null);
        lblWelcome = new JLabel("Welcome Administrator");
        lblWelcome.setBounds(200, 5, 509, 39);
        lblWelcome.setFont(largeFont);
        pnlAdminHome.add(lblWelcome);
        pnlLeft = new JPanel(new GridLayout(14, 1));
        pnlLeft.setBounds(1, 50, LEFT_PANEL_WIDTH, 390);
        pnlLeft.setBorder(new LineBorder(new Color(0, 0, 0)));
        createPermissionButtons();
        pnlAdminHome.add(pnlLeft);
        pnlRight = new JPanel();
        pnlRight.setBounds(180, 50, 615 - LEFT_PANEL_WIDTH, 390);
        pnlRight.setBorder(new LineBorder(new Color(0, 0, 0)));
        pnlAdminHome.add(pnlRight);
    }

    /**
	 * Creates all the buttons that the administrator is allowed
	 */
    private void createPermissionButtons() {
        pnlButtons = new JPanel();
        pnlButtons.setVisible(true);
        btnReports = new JButton("Reports");
        btnSetUpSemester = new JButton("Semester Setup");
        btnAddStudentFile = new JButton("Add Students to Database");
        btnLogOut = new JButton("Log Out");
        btnReports.addActionListener(new GenerateReportsListener());
        btnSetUpSemester.addActionListener(new SetUpSemesterListener());
        btnAddStudentFile.addActionListener(new AddStudentFileListener());
        btnLogOut.addActionListener(new LogOutListener());
        pnlLeft.add(btnReports);
        pnlLeft.add(btnSetUpSemester);
        pnlLeft.add(btnAddStudentFile);
        pnlLeft.add(btnLogOut);
    }

    /**
	 * sets up the reports panel
	 */
    private void createReportPanel() {
    }

    /**
	 * test method
	 * @param args
	 */
    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    AdministratorGUI frame = new AdministratorGUI();
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Dimension screen = tk.getScreenSize();
                    frame.setLocation(screen.width / 2 - LogInGUI.WINDOW_W / 2, screen.height / 2 - LogInGUI.WINDOW_H / 2);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * ActionListener for btnReports
	 * @author Brandon
	 */
    class GenerateReportsListener implements ActionListener {

        /**
		 * Will switch the active card to the generate reports card
		 * 
		 */
        public void actionPerformed(ActionEvent e) {
        }
    }

    /**
	 * Action Listener for btnSetUpSemester
	 * @author Brandon
	 */
    class SetUpSemesterListener implements ActionListener {

        /**
		 * will  open a JFile chooser for the administrator to select the file to upload.
		 */
        public void actionPerformed(ActionEvent e) {
            StudentCourseFile scf = new StudentCourseFile(studentCourseFile);
            try {
                scf.addCourseToDatabase(studentCourseFile);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
	 * ActionListener for btnAddStudentFile
	 * @author Brandon
	 *
	 */
    class AddStudentFileListener implements ActionListener {

        /**
		 * will switch the card to the addStudentFile card and open a
		 * JFile chooers for the administrator to select the file to upload.
		 */
        public void actionPerformed(ActionEvent e) {
            UserFile uf = new UserFile(userFile);
            try {
                uf.addStudentToDatabase(userFile);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
	 * ActionListener for btnLogOut
	 * @author Brandon
	 */
    class LogOutListener implements ActionListener {

        /**
		 * Ends the administrators session and returns to the LogInGUI screen.
		 */
        public void actionPerformed(ActionEvent e) {
            closeAndDispose();
            BearTrac.showLogInGUI();
        }
    }
}
