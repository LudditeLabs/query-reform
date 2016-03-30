/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorAplicacionComprador implements ActionListener {

    AplicacionComprador ac = null;

    public ControladorAplicacionComprador(AplicacionComprador aplicacionComprador) {
        ac = aplicacionComprador;
    }

    public void actionPerformed(ActionEvent evt) {
        System.out.println("ControladorAplicacionComprador.actionPerformed()");
        String action = evt.getActionCommand();
        System.out.println("ActionCommand=" + action);
        if (action.equals("IS")) {
            System.out.println("Iniciar Sesi�n");
            PantallaLogin login = new PantallaLogin(ac);
            login.setVisible(true);
        } else if (action.equals("CS")) {
            System.out.println("Cerrar sesi�n");
            ac.setMenuNoRegistrado();
        } else if (action.equals("AR")) {
            System.out.println("Alta registro");
            PantallaDatosPersonales pdp = new PantallaDatosPersonales(ac, false);
            pdp.setVisible(true);
        } else if (action.equals("CDP")) {
            PantallaDatosPersonales pdp = new PantallaDatosPersonales(ac, false);
        } else if (action.equals("MDP")) {
            PantallaDatosPersonales pdp = new PantallaDatosPersonales(ac, true);
        } else if (action.equals("PPa")) {
        } else if (action.equals("PPu")) {
        } else if (action.equals("PS")) {
        } else if (action.equals("RP")) {
            new PantallaHacerPedido(ac).setVisible(true);
        }
    }
}
