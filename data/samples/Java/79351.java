/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import struct.Projet;

public class CondDecl extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JButton Bt_Ok, Bt_Annuler;

    private JTextField Ed_Resultat, Ed_NomVariable;

    JPanel panevariable;

    JComboBox CB_ChoixTypeDecl, CB_VarTypeDecl, CB_Operateur;

    JComboBox CB_Objet;

    public int status;

    public String Commande;

    class Contenu extends JPanel {

        private static final long serialVersionUID = 1L;

        CondDecl parent;

        Projet projet;

        public Contenu(Projet prj, CondDecl p) {
            parent = p;
            projet = prj;
            setLayout(null);
            Font font = new Font("MS Sans Serif", Font.PLAIN, 10);
            CB_ChoixTypeDecl = new JComboBox(new String[] { "Appuie sur bouton", "En contact", "Attaque", "Automatique", "Auto une seul fois", "Variable..." });
            CB_ChoixTypeDecl.setFont(font);
            CB_ChoixTypeDecl.setBounds(new Rectangle(105, 5, 155, 20));
            CB_ChoixTypeDecl.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (CB_ChoixTypeDecl.getSelectedIndex() == 5) panevariable.setVisible(true); else panevariable.setVisible(false);
                }
            });
            add(CB_ChoixTypeDecl);
            panevariable = new JPanel();
            panevariable.setBounds(new Rectangle(5, 30, 325, 100));
            panevariable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            panevariable.setLayout(null);
            add(panevariable);
            panevariable.setVisible(false);
            int statsize = projet.getStatsBase().size();
            String[] values = new String[31 + statsize];
            values[0] = "%Name%";
            values[1] = "%UpperName%";
            values[2] = "%Classe%";
            values[3] = "%Skin%";
            values[4] = "%Vie%";
            values[5] = "%VieMax%";
            values[6] = "%CurrentMag%";
            values[7] = "%MagMax%";
            values[8] = "%Gold%";
            values[9] = "%Lvl%";
            values[10] = "%LvlPoint%";
            values[11] = "%CurrentXP%";
            values[12] = "%NextXP%";
            values[13] = "%Timer%";
            values[14] = "%Timer2%";
            values[15] = "%Timer3%";
            values[16] = "%Effect%";
            values[17] = "%CaseX%";
            values[18] = "%CaseY%";
            values[19] = "%EvCaseX%";
            values[20] = "%EvCaseY%";
            values[21] = "%BloqueChangeSkin%";
            values[22] = "%Direction%";
            values[23] = "%Inventaire%";
            values[24] = "%NbObjetInventaire%";
            values[25] = "%Arme%";
            values[26] = "%Bouclier%";
            values[27] = "%Casque%";
            values[28] = "%Armure%";
            for (int i = 0; i < statsize; i++) values[29 + i] = "%" + projet.getStatsBase().get(i) + "%";
            values[29 + statsize] = "%rand(...)%";
            values[30 + statsize] = "Variable...";
            CB_VarTypeDecl = new JComboBox(values);
            CB_VarTypeDecl.setBounds(new Rectangle(5, 5, 120, 20));
            CB_VarTypeDecl.setFont(font);
            CB_VarTypeDecl.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if ((CB_VarTypeDecl.getSelectedIndex() == InCombo(CB_VarTypeDecl, "Variable...")) || (CB_VarTypeDecl.getSelectedIndex() == InCombo(CB_VarTypeDecl, "%rand(...)%"))) Ed_NomVariable.setVisible(true); else Ed_NomVariable.setVisible(false);
                    if (CB_VarTypeDecl.getSelectedIndex() == InCombo(CB_VarTypeDecl, "%Inventaire%")) {
                        Ed_Resultat.setVisible(false);
                        CB_Objet.setVisible(true);
                    } else {
                        Ed_Resultat.setVisible(true);
                        CB_Objet.setVisible(false);
                    }
                }
            });
            panevariable.add(CB_VarTypeDecl);
            CB_Operateur = new JComboBox(new String[] { ">", "=", "!=", "<", ">=", "<=" });
            CB_Operateur.setBounds(new Rectangle(130, 5, 60, 20));
            CB_Operateur.setFont(font);
            CB_Operateur.setSelectedIndex(1);
            panevariable.add(CB_Operateur);
            Ed_Resultat = new JTextField();
            Ed_Resultat.setBounds(new Rectangle(195, 5, 120, 20));
            Ed_Resultat.setFont(font);
            panevariable.add(Ed_Resultat);
            values = new String[projet.getObjets().size()];
            for (int i = 0; i < projet.getObjets().size(); i++) values[i] = projet.getObjetByIndex(i).Name;
            CB_Objet = new JComboBox(values);
            CB_Objet.setBounds(new Rectangle(195, 5, 120, 20));
            CB_Objet.setFont(font);
            CB_Objet.setVisible(false);
            panevariable.add(CB_Objet);
            Ed_NomVariable = new JTextField();
            Ed_NomVariable.setBounds(new Rectangle(5, 30, 120, 20));
            Ed_NomVariable.setFont(font);
            Ed_NomVariable.setVisible(false);
            panevariable.add(Ed_NomVariable);
            JLabel Lbl_Texte = new JLabel("Dans le cas de l'inventaire, choisissez");
            Lbl_Texte.setBounds(new Rectangle(130, 30, 200, 15));
            Lbl_Texte.setFont(font);
            panevariable.add(Lbl_Texte);
            Lbl_Texte = new JLabel("toujours le signe '=' ou '!='");
            Lbl_Texte.setBounds(new Rectangle(130, 45, 200, 15));
            Lbl_Texte.setFont(font);
            panevariable.add(Lbl_Texte);
            Bt_Ok = new JButton("Ok");
            Bt_Ok.setBounds(new Rectangle(81, 140, 90, 20));
            add(Bt_Ok);
            Bt_Annuler = new JButton("Annuler");
            Bt_Annuler.setBounds(new Rectangle(185, 140, 90, 20));
            add(Bt_Annuler);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("Déclenchement : ", 6, 20);
        }
    }

    public CondDecl(Projet projet, String commande, Dialog owner, boolean modal) {
        super(owner, "Condition de déclenchement", modal);
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension tailleEcran = k.getScreenSize();
        int largeurEcran = tailleEcran.width;
        int hauteurEcran = tailleEcran.height;
        setSize(350, 200);
        setLocation((largeurEcran / 2) - 175, (hauteurEcran / 2) - 100);
        setLayout(new BorderLayout());
        Contenu monContenu = new Contenu(projet, this);
        add(monContenu);
        Bt_Ok.addActionListener(this);
        Bt_Annuler.addActionListener(this);
        Commande = commande;
        LoadCommande();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bt_Ok) {
            status = 1;
            SaveCommande();
        } else status = 0;
        setVisible(false);
    }

    private int InCombo(JComboBox cb, String chaine) {
        for (int i = 0; i < cb.getItemCount(); i++) if (cb.getItemAt(i).toString().compareTo(chaine) == 0) return i;
        return -1;
    }

    public void LoadCommande() {
        int posop;
        String variable, op;
        if (Commande.compareTo("") != 0) {
            posop = InCombo(CB_ChoixTypeDecl, Commande);
            if (posop >= 0) CB_ChoixTypeDecl.setSelectedIndex(posop); else {
                CB_ChoixTypeDecl.setSelectedIndex(5);
                posop = Commande.indexOf("!=");
                op = "!=";
                if (posop == -1) {
                    posop = Commande.indexOf(">=");
                    op = ">=";
                }
                if (posop == -1) {
                    posop = Commande.indexOf("<=");
                    op = "<=";
                }
                if (posop == -1) {
                    posop = Commande.indexOf(">");
                    op = ">";
                }
                if (posop == -1) {
                    posop = Commande.indexOf("<");
                    op = "<";
                }
                if (posop == -1) {
                    posop = Commande.indexOf("=");
                    op = "=";
                }
                if (posop != -1) {
                    variable = Commande.substring(0, posop);
                    CB_Operateur.setSelectedIndex(InCombo(CB_Operateur, op));
                    Ed_Resultat.setText(Commande.substring(posop + op.length()));
                    posop = InCombo(CB_VarTypeDecl, variable);
                    if (posop >= 0) {
                        CB_VarTypeDecl.setSelectedIndex(posop);
                        if (CB_VarTypeDecl.getItemAt(CB_VarTypeDecl.getSelectedIndex()).toString().compareTo("%Inventaire%") == 0) {
                            posop = InCombo(CB_Objet, Ed_Resultat.getText());
                            if (posop >= 0) CB_Objet.setSelectedIndex(posop);
                        }
                    } else {
                        if (variable.startsWith("%rand(")) {
                            CB_VarTypeDecl.setSelectedIndex(InCombo(CB_VarTypeDecl, "%rand(...)%"));
                            variable = variable.substring(variable.indexOf("(") + 1);
                            if (variable.indexOf(")%") >= 0) variable = variable.substring(0, variable.lastIndexOf(")%"));
                            Ed_NomVariable.setText(variable);
                        } else if (variable.startsWith("Variable")) {
                            CB_VarTypeDecl.setSelectedIndex(InCombo(CB_VarTypeDecl, "Variable..."));
                            variable = variable.substring(variable.indexOf("[") + 1);
                            if (variable.indexOf("]") >= 0) variable = variable.substring(0, variable.lastIndexOf("]"));
                            Ed_NomVariable.setText(variable);
                        }
                    }
                }
            }
        }
    }

    public void SaveCommande() {
        if (CB_ChoixTypeDecl.getItemAt(CB_ChoixTypeDecl.getSelectedIndex()).toString().compareTo("Variable...") == 0) {
            if (CB_VarTypeDecl.getItemAt(CB_VarTypeDecl.getSelectedIndex()).toString().compareTo("%Inventaire%") == 0) Commande = "%Inventaire%" + CB_Operateur.getItemAt(CB_Operateur.getSelectedIndex()).toString() + CB_Objet.getItemAt(CB_Objet.getSelectedIndex()).toString(); else if (CB_VarTypeDecl.getItemAt(CB_VarTypeDecl.getSelectedIndex()).toString().compareTo("%rand(...)%") == 0) Commande = "%rand(" + Ed_NomVariable.getText() + ")%" + CB_Operateur.getItemAt(CB_Operateur.getSelectedIndex()).toString() + Ed_Resultat.getText(); else if (CB_VarTypeDecl.getItemAt(CB_VarTypeDecl.getSelectedIndex()).toString().compareTo("Variable...") == 0) Commande = "Variable[" + Ed_NomVariable.getText() + "]" + CB_Operateur.getItemAt(CB_Operateur.getSelectedIndex()).toString() + Ed_Resultat.getText(); else Commande = CB_VarTypeDecl.getItemAt(CB_VarTypeDecl.getSelectedIndex()).toString() + CB_Operateur.getItemAt(CB_Operateur.getSelectedIndex()).toString() + Ed_Resultat.getText();
        } else Commande = CB_ChoixTypeDecl.getItemAt(CB_ChoixTypeDecl.getSelectedIndex()).toString();
    }
}
