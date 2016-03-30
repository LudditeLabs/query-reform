/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;
import org.jmol.util.Logger;

/**
 * A example of integrating the Jmol viewer into a java application.
 *
 * <p>I compiled/ran this code directly in the examples directory by doing:
 * <pre>
 * javac -classpath ../Jmol.jar Integration.java
 * java -cp .:../Jmol.jar Integration
 * </pre>
 *
 * @author Miguel <miguel@jmol.org>
 */
public class Integration {

    public static void main(String[] argv) {
        JFrame frame = new JFrame("Hello");
        frame.addWindowListener(new ApplicationCloser());
        Container contentPane = frame.getContentPane();
        JmolPanel jmolPanel = new JmolPanel();
        contentPane.add(jmolPanel);
        frame.setSize(300, 300);
        frame.setVisible(true);
        JmolSimpleViewer viewer = jmolPanel.getViewer();
        viewer.openStringInline(strXyzHOH);
        viewer.evalString(strScript);
        String strError = viewer.getOpenFileError();
        if (strError != null) Logger.error(strError);
    }

    static final String strXyzHOH = "3\n" + "water\n" + "O  0.0 0.0 0.0\n" + "H  0.76923955 -0.59357141 0.0\n" + "H -0.76923955 -0.59357141 0.0\n";

    static final String strScript = "delay; move 360 0 0 0 0 0 0 0 4;";

    static class ApplicationCloser extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    static class JmolPanel extends JPanel {

        JmolSimpleViewer viewer;

        JmolAdapter adapter;

        JmolPanel() {
            adapter = new SmarterJmolAdapter();
            viewer = JmolSimpleViewer.allocateSimpleViewer(this, adapter);
        }

        public JmolSimpleViewer getViewer() {
            return viewer;
        }

        final Dimension currentSize = new Dimension();

        final Rectangle rectClip = new Rectangle();

        public void paint(Graphics g) {
            getSize(currentSize);
            g.getClipBounds(rectClip);
            viewer.renderScreenImage(g, currentSize, rectClip);
        }
    }
}
