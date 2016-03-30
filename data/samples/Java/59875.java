/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import javax.swing.JWindow;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLClassLoader;

/**
 * SplashScree creates a splash screen for the application.
 * It can be run concurrently with the application by starting
 * it using a Thread.
 *
 * @version 1.0
 * @author P.Jaya Krishna
 */
public class SplashScreen extends JWindow implements Runnable {

    /**
	 * Creates a SplashScreen object with necessary initialization.
	 */
    public SplashScreen(boolean isDelayed) {
        ImageIcon image = null;
        JLabel label = new JLabel(image);
        Dimension dim;
        int x, y;
        try {
            image = new ImageIcon("images/splash.png");
            label = new JLabel(image);
        } catch (Exception ex) {
        }
        getContentPane().add(label);
        if (!isDelayed) {
            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent me) {
                    setVisible(false);
                }
            });
        }
        pack();
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) (dim.getWidth() - getWidth()) / 2;
        y = (int) (dim.getHeight() - getHeight()) / 2;
        setLocation(x, y);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    /**
	 * For concurrent execution run() should be invoked through 
	 * start() a thread.
	 */
    public void run() {
        try {
            Thread.currentThread().sleep(3000);
            setVisible(false);
        } catch (InterruptedException ex) {
        }
    }
}
