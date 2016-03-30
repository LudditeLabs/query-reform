/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The interface_MainFrame class implements the main frame of the client. The main
 * aspects of this class are to prepare the user interface for the communication. Here
 * the login/logout menu is created.
 * Also the drawing panel and the adapter class to the communication is created.
 * In this case the frame is only a cover for some complex background applications.
 * This applications has to be embedded in this class.
 * In this case the background application of Liu (2008) is embedded in this class.
 * The "By Sven Weber" to "End Sven Weber" embedded sections are parts of the
 * communication.
 * 
 * @author (c) Copyright 2009 by authors: Xuanjiao Liu, Sven Weber, Dr. The Anh Vuong
 *
 */
public class interface_MainFrame_E extends JFrame implements ActionListener, ItemListener{

	/** The serial version UID. */
	private static final long serialVersionUID = -8063952762260195177L;

	static Color colors[] = { Color.orange, Color.pink, Color.cyan,
	      Color.magenta, Color.blue, Color.lightGray, Color.gray };

	String[] window_e = {
			  "Display",
		      "Reformating",
		      "Montage",
		      "Analogy",
		      "Document",
		      "Help",

		    };
	 
	 


	 String[] menu_e_1= {
		      "File",
		      "New",
		      "Open",
		      "Close",
		      "Save",
		      "Save as",
		      "Print",
		      "End"
		    };

	 String[] menu_e_2 = {
			 "Look and Feel",
		      "GTK LookAndFeel",
		      //"Mac LookAndFeel",
		      "Metal LookAndFeel",
		      "Motif LookAndFeel",
		      "Windows LookAndFeel",
		      //"Pgs LookAndFeel"
		    };
	 
	 	
	 
	 	//By Sven Weber
		String[] menu_e_3 = {
				  "Communication",
			      "Connect",
			      "Disconnet",
			      "Systemmessages",
			      "Chat",
			      "Graphic-Toolbar",
			      "Drawpermission",
			      "Radar view"

		    };
		//End of Sven Weber
	
	
	
		String[] Labeltab1 = {
				"Canal Band: ",
				"Canal: ",
				"Timeintervall: ",
				"Timeframe: ",
				"Horizontal",
				"Verical",
				"Ruler: ",
				"Canal "

		};

	 String[] Buttontab2 = {
				"Load Montag",
				"Load Electrode",
				"Load Canal",
				//"Load Left Viewer",
				//"Load Right Viewer",
				"Reformat",
				"New Montage",				
				//"Reformat from",
				//"Choose Color"
		};
	 
		String[] Buttontab4 = {
				"Load Canalbox",
				"Load Left Viewer",
				"Load Right Viewer",
				"Timeintervall: ",
				"Timeframe: ",
				"Horizontal",
				"Verical",
				"Kanal "
		};

	String[] ButtontabDocument = {
				"Load Patient-Document",
				"Save Patient-Document",
				"Print Patient-Document"
		};
	

	String[] Montage = {
			"Aspect Montage 1",
			"Aspect Montage 2",
			"Parasagittal Row Montage",
			"Coronary Row Montage",
			"Transversal Row Montage",
			"H-Montage"
	};

	String[] MontageImage = {
			"MontageImage1.jpg",
			"MontageImage2.jpg",
			"MontageImage3.jpg",
			"MontageImage4.jpg",
			"MontageImage5.jpg",
			"MontageImage6.jpg"
	};

	String[] canalschema, reformcanal;
 	String[] zeitintervall= {"5ms", "4ms", "3ms", "2ms", "1ms"};
 	String[] zeitfenster={" ", "20s", "10s", "5s", "1s"};
    int[] canalliste;
 	Color blue = new Color(0,128,128);
 	Color black = new Color(0,0,0);
 	Color green = new Color(85,148,75);
 	Color turkis = new Color(129,190,190);
 	Color green1 = new Color(77,148,136);
 
 	Rectangle  frame1 = new Rectangle(0,20,1600,800);


 	//String[] masstab={"Hide", "Show"};

	public JMenuBar menuBar = null;
	
	
	
	public EEG aEEG = null;

	public EEGMontage eegmontage;

	public EEGReformat eegreformat;
	

	public interface_DrawPanel dpanel;

	
	public SPanel spanel1, spanel2;

	public JPanel rpanel, checkboxpanel;

	public JViewport vp;

	public EPanel epanel, epanel1, epanel2;

	public MontagePanel mpanel;

	public JSplitPane splitPane = null;

	public JPanel panel1,panel1_1, panel1_2,
	Panel2, panel2, panel2_1, panel2_2, panel2_3,panel2_4, panel2_5, panel2_3_1, panel2_3_2,
	panel3, panel3_1,
	panel4, panel4_1, panel4_2,panel4_3, panel4_4,
	panel5, panel5_1, panel5_2, panel5_3, panel5_3_1, panel5_3_2,
	panel6, panel6_1, panel6_2,
	panel7;

	public JPanel[] panel;

	public ButtonGroup group;

	public JTable tablecanal;

	public JLabel lb;

	//private JRadioButton rb;

	public static JTabbedPane tab;

	public JInternalFrame jif1, jif2;

	public JTextArea textarea;

	//public JFrame mainframe;
	
	public JCheckBox[] CheckBoxCanal;

    Timer timer;

    
    
    
	//By Sven Weber
	/** The horizontal scrollbar. */
	static JScrollBar sb;
	
	/** The vertical scrollbar. */
	static JScrollBar sv;
    
	/** A value for the addOn_adapterToCom class. */
	addOn_adapterToCom aTC;
	
	/** The different panels. */
	public JPanel panel8, panel8_1, panel8_2;
	
	/** The different CheckBoxMenuItems. */
	static JCheckBoxMenuItem miCheck1;
	static JCheckBoxMenuItem miCheck2;
	static JCheckBoxMenuItem miCheck3;
	static JCheckBoxMenuItem miCheck4;
	static JCheckBoxMenuItem miCheck5;
	
	/** The different RadioButtonMenuItems. */
	JRadioButtonMenuItem miRadio1;
	JRadioButtonMenuItem miRadio2;
	
	/** The maximum value of the panels vertical slider. */
	int verticalSliderMax = 0;
	
	/** The maximum value of the panels horizontal slider. */
	int horizontalSliderMax = 0;
	//End Sven Weber
    
    
	
	
    
    public interface_MainFrame_E() {
    	
      	//By Sven Weber
      	//A instance of the class. 
      	aTC = new addOn_adapterToCom();
      	verticalSliderMax = simpleApplicationInterface.getVerticalSliderMax();
      	horizontalSliderMax = simpleApplicationInterface.getHorizontalSliderMax();
      	//End Sven Weber
    	
      	
      	
	    aEEG = new EEG();
    
	    eegmontage = new EEGMontage(aEEG);
	    eegreformat = new EEGReformat();
	    canalschema = new String [32];
	    canalliste = new int[32];

        tab = new JTabbedPane();
      	addTabViewer(window_e[0],Labeltab1);
      	addReformViewer(window_e[1], Buttontab2);
      	addMontageViewer(window_e[2]);
      	addVisualisationViewer(window_e[3], Buttontab4);
      	addDocumentViewer(window_e[4], ButtontabDocument);
      	//addHelpViewer(window_g[5]);
      	
      	

      	getContentPane().add(tab, "Center");

      	menuBar = createMenus(menu_e_1, menu_e_2, menu_e_3);
    	setJMenuBar(menuBar);


     	 ChangeListener changeListener = new ChangeListener() {
  	      public void stateChanged(ChangeEvent changeEvent) {
  	        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent
  	            .getSource();
  	        int index = sourceTabbedPane.getSelectedIndex();
  	        System.out.println("Tab changed to: "
  	            + sourceTabbedPane.getTitleAt(index));
  	        
  	      }
  	     };

  	     tab.addChangeListener(changeListener);

	 }
    
    
    
    
    
    
    
    
    
    //By Sven Weber
    /**
     * Sets the horizontal slider position.
     * 
     * @param pos	The horizontal slider position.
     */
    public static void setHorizontalSlider(int pos) {
    	sb.setValue(pos);
    }
    
    
    /**
     * Sets the vertical slider position.
     * 
     * @param pos	The horizontal slider position.
     */
    public static void setVerticalSlider(int pos) {
    	sv.setValue(pos);
    }
    //End Sven Weber
    

    
    
    
    
    
    
    
    public void init_e()
    {
       	 setTitle(simpleApplicationInterface.getHeader());
       	 setBounds(0,0,(getToolkit().getScreenSize().width)-250, Math.round(getToolkit().getScreenSize().height/4*3));
         setVisible(true);
    }

	public void addHelpViewer(String label) {
		 panel7 = new JPanel();
	     panel7.setLayout(null);
	     tab.addTab(label, null, panel7, label);
	}
		
	
	
	

	public void addDocumentViewer(String label, String[] buttontabdoc) {
		 panel6 = new JPanel();
	     panel6.setLayout(new BorderLayout());

	     panel6_1 = new JPanel();
	     panel6_1.setBackground(turkis);
	     panel6_1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
	     for(int i=0; i<3; i++){
	    	 JButton button = new JButton(buttontabdoc[i]);
			 button.setBackground(new Color(129,190,190));
			 button.addActionListener(this);
			 panel6_1.add(button);
	     }

	     panel6.add("North", panel6_1);
	     tab.addTab(label, null,panel6, label);
	}

	public void addVisualisationViewer(String label, String[] labeltext) {
		 panel5 = new JPanel();
         panel5.setBackground(turkis);
		 panel5.setLayout(null);
		 
		 epanel2 = new EPanel(aEEG);
	     epanel2.setBounds(10,20,320,400);
	     epanel2.setBackground(black);
	     epanel2.setVisible(false);

	     //Montagepanel:
		 panel5_1 = new JPanel();
		 panel5_1.setBounds(0,40, 320,320);
		 panel5_1.setBackground(black);

		 //KanalPanel:
		 panel5_2 = new JPanel();
		 panel5_2.setBounds(0,40, 320,320);
		 panel5_2.setBackground(turkis);
	     panel5_2.setLayout(new GridLayout(16, 2));
	     
         spanel1= new SPanel(aEEG);
         spanel1.setBackground(black);
         spanel2= new SPanel(aEEG);
         spanel2.setBackground(black);
	     
	     checkboxpanel = new JPanel();		
	     checkboxpanel.setBackground(turkis);
	     checkboxpanel.setBounds(0,360, 320,320);
	     checkboxpanel.setLayout(new GridLayout(16,2));	     
	     CheckBoxCanal = new JCheckBox[32];	
	     
  	     for (int i=0;i<32;i++)
		    {
			 CheckBoxCanal[i] = new JCheckBox(Buttontab4[7]+(i+1));		
			 CheckBoxCanal[i].setBackground(turkis);
		     //CheckBoxCanal.setFont(new Font("Dialog", Font.BOLD, 11));
			 CheckBoxCanal[i].addActionListener(this);
		     checkboxpanel.add(CheckBoxCanal[i]);			    	
		    }
	     JPanel buttonpanel1 = new JPanel();
		 buttonpanel1.setBackground(turkis);
		 buttonpanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
		 buttonpanel1.setBounds(0,0,1580,30);
		 
		 for (int i =0; i<3; i++){
			 JButton button = new JButton(labeltext[i]);
			 button.setBackground(turkis);
			 button.addActionListener(this);
			 buttonpanel1.add(button);
		 }
		 
	     //JComboBox Zeitintervall=new JComboBox(zeitintervall);
	     //Zeitintervall.addItemListener(this);
		 JComboBox Zeitfenster=new JComboBox(zeitfenster);
	     Zeitfenster.addItemListener(this);


	     	final JSlider hslider = createSlider(JSlider.HORIZONTAL, 1,10,1, 5,1,129, 190, 190);
	     	hslider.addChangeListener(new ChangeListener()
	           { public void stateChanged(ChangeEvent e)
	           { System.out.println("Wert"+hslider.getValue());
	             spanel1.Ma�stab(false);
	             spanel1.HorizontalVergr(hslider.getValue());
	             spanel2.Ma�stab(false);
	             spanel2.HorizontalVergr(hslider.getValue());
	             //repaint();
	            }
	        });
	     	final JSlider vslider = createSlider(JSlider.HORIZONTAL, 1,5,1, 5,1,129, 190, 190);
	     	vslider.addChangeListener(new ChangeListener()
	           { public void stateChanged(ChangeEvent e)
	           { System.out.println("Wert"+vslider.getValue());
	             //hpanel.Ma�stab(false);
	           spanel1.VerticalVergr(vslider.getValue());
	           spanel2.VerticalVergr(vslider.getValue());
	             //repaint();
	            }
	        });
	     	
		    final JScrollBar sb1 = new JScrollBar(JScrollBar.HORIZONTAL, 0,20 , 0, 20000);
		     sb1.addAdjustmentListener(new AdjustmentListener(){
				public void adjustmentValueChanged(AdjustmentEvent event) {
	            	spanel1.setXWert(event.getValue());
	                //repaint();
				}
		     });
		    final JScrollBar sv1 = new JScrollBar(JScrollBar.VERTICAL, 0,20 , 0, 1000);
		     sv1.addAdjustmentListener(new AdjustmentListener(){
					public void adjustmentValueChanged(AdjustmentEvent event) {
		            	spanel1.setYWert(event.getValue());
		                //repaint();
					}
			 });
		     
			    final JScrollBar sb2 = new JScrollBar(JScrollBar.HORIZONTAL, 0,20 , 0, 20000);
			     sb2.addAdjustmentListener(new AdjustmentListener(){
					public void adjustmentValueChanged(AdjustmentEvent event) {
		            	spanel2.setXWert(event.getValue());
		                //repaint();
					}
			     });
			    final JScrollBar sv2 = new JScrollBar(JScrollBar.VERTICAL, 0,20 , 0, 1000);
			     sv2.addAdjustmentListener(new AdjustmentListener(){
						public void adjustmentValueChanged(AdjustmentEvent event) {
			            	spanel2.setYWert(event.getValue());
			                //repaint();
						}
				 });
	     	
	     	//buttonpanel.add(new JLabel(labeltext[2]));
	     	//buttonpanel.add(Zeitintervall);
	     	buttonpanel1.add(new JLabel(labeltext[4]));
	     	buttonpanel1.add(Zeitfenster);
	     	buttonpanel1.add(new JLabel(labeltext[5]));
	     	buttonpanel1.add(hslider);
	     	buttonpanel1.add(new JLabel(labeltext[6]));
	     	buttonpanel1.add(vslider);
		 
		 group = new ButtonGroup();		 		 
	     JRadioButton button1 = new JRadioButton("Vertically Split");
	     button1.setBackground(turkis);
	     button1.addActionListener(this);
	     group.add(button1); 
	     JRadioButton button2 = new JRadioButton("Horizontally Split");
	     button2.setBackground(turkis);
	     button2.setSelected(true);
	     button2.addActionListener(this);
	     group.add(button2);	  
	     
	     buttonpanel1.add(button1);
	     buttonpanel1.add(button2);
	     
	     
	     panel5_3_1 = new JPanel();
	     panel5_3_1.setBackground(green);
	     panel5_3_1.setLayout(new BorderLayout());
	     panel5_3_2 = new JPanel();
	     panel5_3_2.setBackground(green1);
	     panel5_3_2.setLayout(new BorderLayout());
         splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel5_3_1, panel5_3_2); 
         splitPane.setContinuousLayout(true); 
 	     splitPane.setOneTouchExpandable(true); 
         splitPane.setDividerLocation(200); 
         splitPane.setBounds(350,40, 1230, 640);
         
		 panel5_3_1.add("South", sb1);
		 panel5_3_1.add("East", sv1);
		 panel5_3_2.add("South", sb2);
	     panel5_3_2.add("East", sv2);
	     
	     //panel5.add(panel5_1);
	     panel5.add(panel5_2);
	     //panel5.add(buttonpanel2);
	     //panel5.add(epanel2);
	     panel5.add(panel5_2);
	     panel5.add(buttonpanel1);
	     panel5.add(splitPane);
	     panel5.add(checkboxpanel);

	     tab.addTab(label, null,panel5, label);
	}

	public void addMontageViewer(String label) {
		 panel4 = new JPanel();
         panel4.setBackground(turkis);
		 panel4.setLayout(null);
		 
		 epanel1 = new EPanel(aEEG);
	     epanel1.setBounds(10,-10,320,400);
	     epanel1.setBackground(black);
	     epanel1.setVisible(false);

	     //Montagepanel:
		 panel4_1 = new JPanel();
		 panel4_1.setBounds(0,0, 320,400);
		 panel4_1.setBackground(black);

		 //KanalPanel:
		 panel4_2 = new JPanel();
		 panel4_2.setBounds(0,420, 320,240);
		 panel4_2.setBackground(turkis);
	     panel4_2.setLayout(new GridLayout(16, 2));

		 panel4_3 = new JPanel();
		 panel4_3.setBackground(turkis);
		 panel4_3.setLayout(new GridLayout(1,6));
		 //panel4_1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
		 //panel4_1.setBounds(0,0,1248,200);

		 /*JPanel p2 = new JPanel();
		 p2.setBounds(0,0,210,200);
		 p2.setLayout(new BorderLayout());
	     p2.setBackground(turkis);
	     p2.add("Center",new JLabel(new ImageIcon("EEG_MontageM.jpg")));
	     p2.add("South", new JLabel("Aufnahmemontage"));
	     panel4.add(p2);*/

		 JRadioButton[] button = new JRadioButton[6];
		 group = new ButtonGroup();
		 for (int i=0;i<6; i++){
			 JPanel p1 = new JPanel();
		     p1.setLayout(new BorderLayout());
		     p1.setBackground(turkis);

		     button[i] = new JRadioButton(Montage[i]);
		     button[i].setBackground(turkis);
		     button[i].addActionListener(this);
		     group.add(button[i]);
		     
		     p1.add("Center", new JLabel(new ImageIcon(interface_MainFrame_E.class.getResource(MontageImage[i]))));
		     
		     //p1.add("Center", new JLabel(new ImageIcon(MontageImage[i])));
		     p1.add("South", button[i]);
		     panel4_3.add(p1);

		 }

		 JScrollPane sr = new JScrollPane();
		 sr.setBounds(320,0,1260,200);
		 JViewport vp = sr.getViewport();
		 vp.setLayout(new BorderLayout());
		 vp.add("Center", panel4_3);

		 panel4_4 = new JPanel();
		 panel4_4.setLayout(new BorderLayout());
		 panel4_4.setBounds(320,200, 1260, 500);
		 mpanel = new MontagePanel(aEEG);
		 mpanel.setBackground(black);

		 final JSlider hslider = createSlider(JSlider.HORIZONTAL, 1,20,1, 1,1,129, 190, 190);
	     	hslider.addChangeListener(new ChangeListener()
	           { public void stateChanged(ChangeEvent e)
	           { System.out.println("Wert"+hslider.getValue());
	             mpanel.HorizontalVergr(hslider.getValue());
	             repaint();
	            }
	        });
	     	final JSlider vslider = createSlider(JSlider.VERTICAL, 1,10,1, 1,1,129, 190, 190);
	     	vslider.addChangeListener(new ChangeListener()
	           { public void stateChanged(ChangeEvent e)
	           { System.out.println("Wert"+vslider.getValue());
	             mpanel.VerticalVergr(vslider.getValue());
	             repaint();
	            }
	        });
		    final JScrollBar sb = new JScrollBar(JScrollBar.HORIZONTAL, 0,20 , 0, 20000);
		     sb.addAdjustmentListener(new AdjustmentListener(){
				public void adjustmentValueChanged(AdjustmentEvent event) {
	            	mpanel.setXWert(event.getValue());
	                repaint();
				}
		     });
		    final JScrollBar sv = new JScrollBar(JScrollBar.VERTICAL, 0,20 , 0, 1000);
		     sv.addAdjustmentListener(new AdjustmentListener(){
					public void adjustmentValueChanged(AdjustmentEvent event) {
		            	mpanel.setYWert(event.getValue());
		                repaint();
					}
			 });

		 panel4_4.add("Center",mpanel);
	     panel4_4.add("South",sb);
		 panel4_4.add("East",sv);
		 panel4_4.add("North",hslider);
		 panel4_4.add("West",vslider);
		 panel4.add(sr);
		 panel4.add(epanel1);
		 panel4.add(panel4_1);
		 panel4.add(panel4_2);
		 panel4.add(panel4_4);
	     tab.addTab(label, null,panel4, label);
	}



	public void addReformViewer(String label, String[] buttontabcanal2) {
		 Panel2 = new JPanel();
		 Panel2.setBackground(turkis);
		 Panel2.setLayout(null);

		 JPanel buttonpanel = new JPanel();
		 buttonpanel.setBackground(turkis);
		 buttonpanel.setLayout(new GridLayout(1,5));
		 buttonpanel.setBounds(0,0,1580,30);

		 for (int i =0; i<buttontabcanal2.length; i++){
			 JButton button = new JButton(buttontabcanal2[i]);
			 button.setBackground(turkis);
			 button.addActionListener(this);
			 buttonpanel.add(button);
		 }
		 /*
		 group = new ButtonGroup();
	     JRadioButton button1 = new JRadioButton("Vertically Split");
	     button1.setBackground(new Color(85,148,75));
	     button1.addActionListener(this);
	     group.add(button1);
	     JRadioButton button2 = new JRadioButton("Horizontally Split");
	     button2.setBackground(new Color(85,148,75));
	     button2.setSelected(true);
	     button2.addActionListener(this);
	     group.add(button2);
	     buttonpanel.add(button1);
	     buttonpanel.add(button2);
	     */
	     epanel = new EPanel(aEEG);
	     epanel.setBounds(10,20,320,410);
	     epanel.setBackground(black);
	     epanel.setVisible(false);

		 //panel2 = new JPanel();
	     //Montagepanel:
		 panel2_1 = new JPanel();
		 panel2_1.setBounds(0,30, 320,410);
		 panel2_1.setBackground(black);

		 //KanalPanel:
		 panel2_2 = new JPanel();
		 panel2_2.setBounds(0,450, 320,240);
		 panel2_2.setBackground(turkis);
	     panel2_2.setLayout(new GridLayout(16, 2));

	     //DarstellungPanel:
	     panel2_3 = new JPanel();
	     panel2_3.setBounds(350,30, 1230, 640);
	     panel2_3.setLayout(new BorderLayout());

	     rpanel = new JPanel();
         rpanel.setBackground(black);
		 rpanel.setLayout(new BorderLayout());


         /*
		 panel2_3 = new JPanel();
		 panel2_3.setBounds(350,60, 320,400);
		 panel2_3.setBackground(new Color(0,0,0));
		 panel2_4 = new JPanel();
		 panel2_4.setBounds(350,460, 320,240);
		 panel2_4.setBackground(new Color(0,0,0));
	     panel2_4.setLayout(new GridLayout(16, 2));

	     panel2_5 = new JPanel();
	     panel2_5.setBounds(350,60, 900,700);
		 panel2_5.setBackground(new Color(85,148,75));
		 panel2_5.setLayout(new GridLayout(32,10));

	     panel2_3_1 = new JPanel();
	     panel2_3_1.setBackground(new Color(85,148,75));
	     panel2_3_1.setLayout(null);
	     panel2_3_2 = new JPanel();
	     panel2_3_2.setBackground(new Color(77,148,136));
	     panel2_3_2.setLayout(null);
         splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel2_3_1, panel2_3_2);
         splitPane.setContinuousLayout(true);
 	     splitPane.setOneTouchExpandable(true);
         splitPane.setDividerLocation(200);
         splitPane.setBounds(300,60, 1000,800);
         */
         addCSInternalFrame(Panel2,10,100,500,300);
         //addRCSInternalFrame(Panel2,10,100,500,300);
	     Panel2.add( buttonpanel);
	     Panel2.add(epanel);
	     Panel2.add(panel2_1);
	     Panel2.add(panel2_2);
	     Panel2.add(panel2_3);
	     //panel2_3.add("South",sb);
	     //panel2_3.add("East",sv);
	     //panel2_3.add("North",hslider);
	     //panel2_3.add("West",vslider);
	     panel2_3.add("Center",rpanel);

	     //Panel2.add(panel2_3);
	     //Panel2.add(panel2_4);
	     //Panel2.add(panel2_5);
	     //Panel2.add(splitPane);

	     tab.addTab(label, null,Panel2, label);
	}

	public void addTabViewer(String label, String[] labeltext) {

		    panel1 = new JPanel();
		    //panel1.setBackground(new Color(0,0,0));
	     	panel1.setLayout(new BorderLayout());

	     	panel1_1 = new JPanel();
	     	//panel1_1.setBounds(10,10,960,20);
	     	panel1_1.setBackground(turkis);
	     	panel1_1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
	     	//panel1_1.setLayout(new FlowLayout());


	     	String[] canals= {labeltext[7]+"1-8",labeltext[7]+"9-16",labeltext[7]+"17-24",labeltext[7]+"25-32"};
	     	String[] canal = new String[32];
	     	for (int i=0;i<32;i++) {
	     		canal[i]=labeltext[7]+(i+1);
	     	}

	     	JComboBox CanalRange=new JComboBox(canals);
	     	//CanalRange.setBackground(new Color(109,171,16));
	     	CanalRange.addItemListener(this);

	     	JComboBox CanalSingle=new JComboBox(canal);
	     	CanalSingle.addItemListener(this);

	     	JComboBox Zeitintervall=new JComboBox(zeitintervall);
	     	Zeitintervall.addItemListener(this);
	     	JComboBox Zeitfenster=new JComboBox(zeitfenster);
	     	Zeitfenster.addItemListener(this);
	     	//JComboBox Masstab=new JComboBox(masstab);
	     	//Masstab.addItemListener(this);

	     	/*JButton button = new JButton("Load Viewer");
			button.setBackground(new Color(129,190,190));
			button.addActionListener(this);*/
	     	
	     	
	     	
	     	
	     	

	     	
	     	
	     	final JSlider hslider = createSlider(JSlider.HORIZONTAL, 1,10,1, 5,1,129, 190, 190);
	     	hslider.addChangeListener(new ChangeListener()
	           { public void stateChanged(ChangeEvent e)
	           { System.out.println("Wert"+hslider.getValue());
	             dpanel.Ma�stab(false);
	             dpanel.HorizontalVergr(hslider.getValue());
	             //repaint();
	            }
	        });

	     	
	     	
	     	final JSlider vslider = createSlider(JSlider.HORIZONTAL, 1,5,1, 5,1,129, 190, 190);
	     	vslider.addChangeListener(new ChangeListener()
	           { public void stateChanged(ChangeEvent e)
	           { System.out.println("Wert"+vslider.getValue());
	             //hpanel.Ma�stab(false);
	             dpanel.VerticalVergr(vslider.getValue());
	             //repaint();
	            }
	        });
	     	
	     	

	     	
	     	
			panel1_1.add(new JLabel(labeltext[0]));
			panel1_1.add(CanalRange);
			panel1_1.add(new JLabel(labeltext[1]));
			panel1_1.add(CanalSingle);
			panel1_1.add(new JLabel(labeltext[2]));
			panel1_1.add(Zeitintervall);
			panel1_1.add(new JLabel(labeltext[3]));
			panel1_1.add(Zeitfenster);
			//panel1_1.add(new JLabel(labeltext[6]));
			//panel1_1.add(Masstab);
			
			panel1_1.add(new JLabel(labeltext[4]));
			panel1_1.add(hslider);
			panel1_1.add(new JLabel(labeltext[5]));
			panel1_1.add(vslider);

	     	panel1_2 = new JPanel();
	     	panel1_2.setBackground(turkis);
	     	panel1_2.setLayout(new BorderLayout(10,10));

	     	dpanel = new interface_DrawPanel(aEEG);
	     	
	     	
	     	
	     	//By Sven Weber
	     	dpanel.initCom();
	     	dpanel.setBackground(simpleApplicationInterface.getBackgroundColor());
	     	//End Sven Weber
	     	
	     	
	     	
	     	
	     	

	     	
		      sb = new JScrollBar(JScrollBar.HORIZONTAL, 0,20 , 0, horizontalSliderMax);
		    sb.addAdjustmentListener(new AdjustmentListener(){
				public void adjustmentValueChanged(AdjustmentEvent event) {
	            	dpanel.setXWert(event.getValue());
	            	
	            	
	            	//By Sven Weber
	            	//Sets the horizontal slider position.
	            	addOn_radarViewPanel.MainInstance.setMyHorizontalPosition(event.getValue());
	            	//End Sven Weber
	            	
	            	
	      
	                //repaint();
				}
		     });
		    sv = new JScrollBar(JScrollBar.VERTICAL, 0,20 , 0, verticalSliderMax); 
		    sv.addAdjustmentListener(new AdjustmentListener(){
					public void adjustmentValueChanged(AdjustmentEvent event) {
		            	dpanel.setYWert(event.getValue());


		            	
		            	//By Sven Weber
		            	//Sets the vertical slider position.
		            	addOn_radarViewPanel.MainInstance.setMyVerticalPosition(event.getValue());
		            	//End Sven Weber
		            	
		            	
		            	
		            	
		                //repaint();
					}
			 });

			panel1.add("North",panel1_1);
			panel1_2.add("Center",dpanel);
			panel1_2.add("South",sb);
			panel1_2.add("East",sv);
			//panel1_2.add("North",hslider);
			//panel1_2.add("West",vslider);
			panel1.add("Center",panel1_2);

		    tab.addTab(label,null,panel1, label);
	}
	
	
	
	
	

	public GridBagConstraints makegbc(int x, int y, int width, int height)
	  {
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = x;
	    gbc.gridy = y;
	    gbc.gridwidth = width;
	    gbc.gridheight = height;
	    gbc.insets = new Insets(1, 1, 1, 1);
	    return gbc;
	  }

	public JMenuBar createMenus(String[] menu1, String[] menu2, String[] menu3) {
    	JMenuItem mi;
    	JMenuBar menuBar = new JMenuBar();

    	JMenu fileMenu_e = menuBar.add(new JMenu(menu1[0]));
    	for (int i = 1; i < menu1.length; ++i) {
    		mi = new JMenuItem(menu1[i]);
         	fileMenu_e.add(mi);
         	mi.addActionListener(this);
    	}

     	JMenu LF_e = menuBar.add(new JMenu(menu2[0]));
     	for (int i = 1; i < menu2.length; ++i) {
    		mi = new JMenuItem(menu2[i]);
         	LF_e.add(mi);
         	mi.addActionListener(this);
    	}
     	
     	
     	
     	
     	
     	
     	
     	//By Sven Weber
     	//The new communication menu.
     	JMenu Com = menuBar.add(new JMenu(menu3[0]));
     	
    	miRadio1 = new JRadioButtonMenuItem(menu3[1]);
     	miRadio1.setSelected(false);
     	Com.add(miRadio1);
 		miRadio1.addActionListener(this);
 		
 		miRadio2 = new JRadioButtonMenuItem(menu3[2]);
 		miRadio2.setSelected(true);
     	Com.add(miRadio2);
 		miRadio2.addActionListener(this);
     	
     	Com.addSeparator();
     	
     	miCheck1 = new JCheckBoxMenuItem(menu3[3]);
     	miCheck1.setSelected(true);
 		Com.add(miCheck1);
 		miCheck1.addActionListener(this);
 		
 		miCheck2 = new JCheckBoxMenuItem(menu3[4]);
     	miCheck2.setSelected(true);
 		Com.add(miCheck2);
 		miCheck2.addActionListener(this);
 		enableMenuCheckBox(2,false);
 		
 		miCheck3 = new JCheckBoxMenuItem(menu3[5]);
     	miCheck3.setSelected(true);
 		Com.add(miCheck3);
 		miCheck3.addActionListener(this);
 		
 		miCheck4 = new JCheckBoxMenuItem(menu3[6]);
     	miCheck4.setSelected(true);
 		Com.add(miCheck4);
 		miCheck4.addActionListener(this);
 		enableMenuCheckBox(4,false);
 		
 		miCheck5 = new JCheckBoxMenuItem(menu3[7]);
 		miCheck5.setSelected(true);
 		Com.add(miCheck5);
 		miCheck5.addActionListener(this);
     	//End Sven Weber
 		
 		

		return menuBar;

     }
	

	
	
	
	//By Sven Weber
	/**
	 * Sets the different checkboxes. 
	 * 
	 * @param check	The selected CheckBox.
	 * @param value	The value if the Checkbox should be selected or not.
	 */
	 public static void setMenuCheckBox(int check, boolean value) {
		 if (check == 1) {
			 miCheck1.setSelected(value);
		 }
		 if (check == 2) {
			 miCheck2.setSelected(value);
		 }
		 if (check == 3) {
			 miCheck3.setSelected(value);
		 }
		 if (check == 4) {
			 miCheck4.setSelected(value);
		 }
		 if (check == 5) {
			 miCheck5.setSelected(value);
		 }
	 }
	 
	 
	 /**
	  * Enables the different checkboxes. 
	  * 
	  * @param check	The selected CheckBox.
	  * @param value	The value if the Checkbox should be enabled or not.
	  */
	 public static void enableMenuCheckBox(int check, boolean value) {
		 boolean selected;
		 if (value == false) {
			 selected = false;
		 }
		 else {
			 selected = true;
		 }
		 
		 if (check == 1) {
			 miCheck1.setSelected(selected);
			 miCheck1.setEnabled(value);
		 }
		 if (check == 2) {
			 miCheck2.setSelected(selected);
			 miCheck2.setEnabled(value);
		 }
		 if (check == 3) {
			 miCheck3.setSelected(selected);
			 miCheck3.setEnabled(value);
		 }
		 if (check == 4) {
			 miCheck4.setSelected(selected);
			 miCheck4.setEnabled(value);
		 }
		 if (check == 5) {
			 miCheck5.setSelected(selected);
			 miCheck5.setEnabled(value);
		 }
	 }
	 //End Sven Weber

	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
     public JSlider createSlider(int orientation, int min, int max, int value, int majortsp, int minortsp, int c1,int c2,int c3 ) {
    	 JSlider slider = new JSlider(orientation, min,max,value);
         slider.setPaintTicks(true);
         slider.setBackground(new Color(c1, c2, c3));
 	     slider.setMajorTickSpacing(majortsp);
 	     slider.setMinorTickSpacing(minortsp);
 	     //slider.setPaintLabels( true );
 	     slider.setSnapToTicks( true );
 	     //slider.addChangeListener(this);
 	     return slider;
     }

     public void showInfo(){
    	 JLabel Name = new JLabel("Patient Name: "+aEEG.getFirstName()+" "+aEEG.getName());
	     JLabel SampleRate = new JLabel("Sample rate: "+" "+aEEG.getRate());
	     panel1_1.add(Name);
	     panel1_1.add(SampleRate);
     }

     public void loadMontageFile(){
  	   FileDialog filedia = new FileDialog(this, "Montage", FileDialog.LOAD);
	   filedia.setFile("*.jpg");
	   filedia.setLocation(60,95);
	   filedia.setVisible(true);
	   String filename = filedia.getFile();
	   if (filename != null) {
		   panel2_1.add(new JLabel(new ImageIcon(interface_MainFrame_E.class.getResource(filename))));
  	       panel4_1.add(new JLabel(new ImageIcon(interface_MainFrame_E.class.getResource(filename))));
  	       panel5_1.add(new JLabel(new ImageIcon(interface_MainFrame_E.class.getResource(filename))));
		   
	
		   
  	       /*panel2_1.add(new JLabel(new ImageIcon(filename)));
  	       panel4_1.add(new JLabel(new ImageIcon(filename)));
  	       panel5_1.add(new JLabel(new ImageIcon(filename)));*/
  	       //panel2_3.add(new JLabel(new ImageIcon(filename)));
	   }
	   filedia.dispose();
     }

     public void loadEEGFile(){	  
		       FileDialog filedia = new FileDialog(this, "�ffnen", FileDialog.LOAD);
			   filedia.setLocation(60,95);
			   filedia.setVisible(true);
			   String filename = filedia.getFile();
			   if (filename != null) {
				 aEEG.lesebDatei(filedia.getDirectory()+filedia.getFile());
				 panel2_2.removeAll();
					 mpanel.removeAll();
					 panel2_1.remove(panel2_2);
					 dpanel.setEinzelKanal(1);
					 dpanel.setMehrKanal(1);
					 
					 
					 
					 //addOn_radarView.setDPanel(dpanel);

					 
					 
			  	     textarea = new JTextArea();
			  	     panel6.add("Center", textarea);
			  	     
			  	     
			  	     //darstellung des EEG im TestTab, ist gekoppelt an tabViewer!
					 /*dpanel2.setEinzelKanal(1);
					 dpanel2.setMehrKanal(1);*/

			   }	    	   
			   filedia.dispose();   
			     
     
      }
     
     

     public void loadDokument(){
	       textarea = new JTextArea();
	       textarea.setBackground(Color.white);
	       textarea.setLineWrap(true);
	       textarea.setWrapStyleWord(true);
	       textarea.setFont(new Font("Dialog",Font.BOLD,12));
	       textarea.setTabSize(5);
	       textarea.setText("Patient Document: \n\n");
	       textarea.append("Patient Name: "+aEEG.firstName+" "+aEEG.name+"\n\n"+
	    		   "Rate: "+aEEG.getRate()+"\n"+"Bank: "+aEEG.getBankAnz()+"\n\n");
	       for (int i=0; i<32; i++){
	    	   textarea.append("Canal "+(i+1)+":  " +aEEG.getKanalNamen(i)+"\n");
	       }
	       //textarea.append(aEEG.loadKDatei());
	       //textarea.replaceRange(aEEG.loadKDatei(), 1, 100);
  	       panel6.add("Center", textarea);
     }

     public void saveDokument(){
	  FileDialog filedia = new FileDialog(this,"�ffnen", FileDialog.LOAD);
	  filedia.setMode(FileDialog.SAVE);
	  filedia.setFile("*.txt");
	  filedia.setVisible(true);
	  String filename = filedia.getFile();
	  if (filename != null) {
		  writeFile(filename, textarea.getText());
	  }
     }

     public void printDokument(){
    	 try {
		      PrinterJob pjob = PrinterJob.getPrinterJob();
		      pjob.setJobName("Patient Document");
		      pjob.setCopies(1);
		      pjob.setPrintable(new Printable() {
		        public int print(Graphics pg, PageFormat pf, int pageNum) {
		          if (pageNum > 0)
		            return Printable.NO_SUCH_PAGE;
		          pg.drawString(aEEG.loadKDatei(), 50, 50);
		          return Printable.PAGE_EXISTS;
		        }
		      });

		      if (pjob.printDialog() == false)
		        return;
		      pjob.print();
		    } catch (PrinterException pe) {
		      pe.printStackTrace();
		    }
        }


     public void addCSInternalFrame(JPanel panelx, int x, int y, int w, int h)
     {
         jif1 = new JInternalFrame();
	     jif1.setTitle("Please choose a Canal Schema:");
	     jif1.setBackground(new Color(0,128,128));
	     jif1.setBounds(x,y,w,h);
	     jif1.setClosable(true);
	     jif1.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
	     JPanel p1 = new JPanel();
	     p1.setLayout(new BorderLayout());
	     JPanel p2 = new JPanel();
	     p2.setLayout(new BorderLayout());
		 group = new ButtonGroup();
	     JRadioButton button1 = new JRadioButton("23 Electrode");
	     button1.setBackground(new Color(85,148,75));
	     button1.addActionListener(this);
	     group.add(button1);
	     JRadioButton button2 = new JRadioButton("21 Electrode");
	     button2.setBackground(new Color(85,148,75));
	     button2.addActionListener(this);
	     group.add(button2);
	     
	     p1.add("Center", new JLabel(new ImageIcon(interface_MainFrame_E.class.getResource("23 Electrode.jpg"))));
	     
	     //p1.add("Center", new JLabel(new ImageIcon("23 Electrode.jpg")));
	     p1.add("South", button1);
	     
	     p2.add("Center", new JLabel(new ImageIcon(interface_MainFrame_E.class.getResource("21 Electrode.jpg"))));
	     
	     //p2.add("Center", new JLabel(new ImageIcon("21 Electrode.jpg")));
	     p2.add("South", button2);
         jif1.add(p1);
         jif1.add(p2);
	     panelx.add(jif1);

     }

     private boolean writeFile(String name, String buf)
 	 {
    	  boolean ret = false;
     	  try {
     	    BufferedWriter writer = new BufferedWriter(
                                     new FileWriter(name));
     		writer.write(buf);
     		writer.close();
     		ret = true;
     	  } catch (FileNotFoundException e) {
     		System.out.println(e);
     	  } catch (IOException e) {
     		System.out.println(e);
     	  }
     	  return ret;
 	 }


	 public void actionPerformed(ActionEvent event)
     {
       String cmd = event.getActionCommand();

       try {
         //PLAF-Klasse auswaehlen
         String plaf = "unknown";
         if (cmd.equals("Metal LookAndFeel")) {
           plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
         } else if (cmd.equals("Motif LookAndFeel")) {
           plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
         } else if (cmd.equals("Windows LookAndFeel")) {
        	 plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
         } else if (cmd.equals("GTK LookAndFeel")) {
             plaf = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
         } else if (cmd.equals("Mac LookAndFeel")) {
             plaf = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
         } else if (cmd.equals("Pgs LookAndFeel")) {
             plaf = "com.pagosoft.plaf.PgsLookAndFeel";
         }
         //LAF umschalten
         UIManager.setLookAndFeel(plaf);
         SwingUtilities.updateComponentTreeUI(this);
       } catch (UnsupportedLookAndFeelException e) {
         System.err.println(e.toString());
       } catch (ClassNotFoundException e) {
         System.err.println(e.toString());
       } catch (InstantiationException e) {
         System.err.println(e.toString());
       } catch (IllegalAccessException e) {
         System.err.println(e.toString());
       }
       
       
       
       
       //By Sven Weber
       if (cmd.equals("Verbindung herstellen") | cmd.equals("Connect")) {
    	   addOn_menu.connect();
    	   miRadio1.setSelected(true);
    	   miRadio2.setSelected(false);
       }
       else if (cmd.equals("Verbindung trennen") | cmd.equals("Disconnet")) {
    	   addOn_menu.disconnect();
    	   miRadio1.setSelected(false);
    	   miRadio2.setSelected(true);
       }
       else if (cmd.equals("Systemmeldungen") | cmd.equals("Systemmessages")) {
    	   addOn_menu.systemMessages(miCheck1.isSelected());
       }
       else if (cmd.equals("Chat")) {
    	   addOn_menu.chat(miCheck2.isSelected());
       }
       else if (cmd.equals("Grafik-Toolbar") | cmd.equals("Graphic-Toolbar")) {
    	   addOn_menu.graphicToolbar(miCheck3.isSelected());
       }
       else if (cmd.equals("Zeichenberechtigung") | cmd.equals("Drawpermission")) {
    	   addOn_menu.drawPermission(miCheck4.isSelected());
       }
       else if (cmd.equals("Radar View")) {
    	   addOn_menu.radarView(miCheck5.isSelected());
       }
       //End Sven Weber
       
       
       
       
	    else if (cmd.equals("New") | cmd.equals("Neu")) {
			 System.out.println(event.getActionCommand());
			 dpanel.setMehrKanal(-1);
			 loadEEGFile();

		}
		else if (cmd.equals("Open") | cmd.equals("�ffnen")) {
			 System.out.println(event.getActionCommand());
			 loadEEGFile();
			 
			 
			   //By Sven Weber
			   addOn_radarView.MainInstance.backgroundAvailable(true);
			   //End Sven Weber
			 
			 
		}
		else if (cmd.equals("Close") | cmd.equals("Schlie�en")) {
			 System.out.println(event.getActionCommand());
			 dpanel.setMehrKanal(-1);
			 
			 
			   //By Sven Weber
			   addOn_radarView.MainInstance.backgroundAvailable(false);
			   //End Sven Weber
			 
			 
		}
		else if (cmd.equals("Save") | cmd.equals("Speichern")) {
			 System.out.println(event.getActionCommand());
		}
		else if (cmd.equals("Save as") | cmd.equals("Speichern unter")) {
			 System.out.println(event.getActionCommand());
		}
		else if (cmd.equals("Print") | cmd.equals("Drucken")) {
			 System.out.println(event.getActionCommand());
		}
		else if (cmd.equals("End") | cmd.equals("Beenden")) {
			 System.out.println(event.getActionCommand());
			 System.exit(0);
		}
		/*else if (cmd.equals("Reformat vom")| cmd.equals("Reformat from")) {
    	      ButtonModel selected = group.getSelection();
    	      System.out.print("Selektiert: ");
    	      if (selected != null) {
    	        System.out.println(selected.getActionCommand());
    	      }
			//String fromknot = JOptionPane.showInputDialog(Panel2, "Reformatingskanale vom Knot:");
    	    //eegreformat.BuildReformSchema(aEEG);
       }*/

       else if (cmd.equals("New Montage")| cmd.equals("Neue Montage")) {
    	   panel2_1.removeAll();
    	   panel2_2.removeAll();
    	   //panel2_3.removeAll();
    	   epanel.setVisible(false);
    	   loadMontageFile();
       }

       else if (cmd.equals("Reformat")) {
    	   rpanel.removeAll();
    	   String knot1 = JOptionPane.showInputDialog(Panel2, "Start Knot:");
    	   String knot2 = JOptionPane.showInputDialog(Panel2, "End Knot:");    	   
    	   eegreformat.Such_Algo_R(aEEG, knot1, knot2, Panel2, rpanel,epanel);
       }

       else if (cmd.equals("Load Canal")| cmd.equals("Kanal Laden")) {
    	   for (int i = 0; i < 32; ++i) {
				 canalschema[i]= aEEG.getKanalNamen(i);
				 JLabel lb = new JLabel(i+1 + ": "+ canalschema[i]);
			     panel2_2.add(lb);
			     JLabel lb1 = new JLabel(i+1 + ": "+ canalschema[i]);
			     panel4_2.add(lb1);
			     JLabel lb2 = new JLabel(i+1 + ": "+ canalschema[i]);
			     panel5_2.add(lb2);
		   }
       }

       else if (cmd.equals("Load Electrode")| cmd.equals("Electroden Laden")) {
    	    jif1.setVisible(true);
       }
       else if (cmd.equals("Load Left Viewer")| cmd.equals("Linker Anzeiger laden")) {
    	   spanel1.getKanalListe(canalliste);
    	   spanel1.setPanel("spanel1");
    	   panel5_3_1.add("Center",spanel1);
    	   canalliste = new int[32];
       }
       else if (cmd.equals("Load Right Viewer")| cmd.equals("Rechter Anzeiger laden")) {
    	   spanel2.getKanalListe(canalliste);
    	   spanel2.setPanel("spanel2");
    	   panel5_3_2.add("Center",spanel2);
    	   canalliste = new int[32];
       }

       else if (cmd.equals("Load Montag") | cmd.equals("Montage Laden")) {
    	   loadMontageFile();
       }
       
       else if (cmd.equals("Load Canalbox") | cmd.equals("Kanalbox Laden")) {
    	   checkboxpanel.removeAll();
    	   for (int i=0;i<32;i++)
 		    {
 			 CheckBoxCanal[i] = new JCheckBox(Buttontab4[7]+(i+1));		
 			 CheckBoxCanal[i].setBackground(turkis);
 		     //CheckBoxCanal.setFont(new Font("Dialog", Font.BOLD, 11));
 			 CheckBoxCanal[i].addActionListener(this);
 		     checkboxpanel.add(CheckBoxCanal[i]);	
 		    }
       }

       else if (cmd.equals("23 Electrode")) {
    	  jif1.setVisible(false);
    	  epanel.setcanalsystem01(1);
    	  epanel.setVisible(true);
	      epanel.setOpaque(false);
    	  epanel1.setcanalsystem01(1);
    	  epanel1.setVisible(true);
	      epanel1.setOpaque(false);
    	  epanel2.setcanalsystem01(1);
    	  epanel2.setVisible(true);
	      epanel2.setOpaque(false);
       }

       else if (cmd.equals("21 Electrode")) {
    	  jif1.setVisible(false);
    	  epanel.setcanalsystem02(1);
    	  epanel.setVisible(true);
	      epanel.setOpaque(false);
    	  epanel1.setcanalsystem02(1);
    	  epanel1.setVisible(true);
	      epanel1.setOpaque(false);
    	  epanel2.setcanalsystem02(1);
    	  epanel2.setVisible(true);
	      epanel2.setOpaque(false);
       }
       else if (cmd.equals("Bezugsmontage 1")) {
    	   eegmontage.Montage1(aEEG, mpanel);
       }

       else if (cmd.equals("Bezugsmontage 2")) {
    	   eegmontage.Montage2(aEEG, mpanel);
       }

       else if (cmd.equals("Parasagittale Reihenmontage")) {
    	   eegmontage.Montage3(aEEG, mpanel);
       }

       else if (cmd.equals("Koronare Reihenmontage")) {
    	   eegmontage.Montage4(aEEG, mpanel);
       }

       else if (cmd.equals("Transversale Reihenmontage")) {
    	   eegmontage.Montage5(aEEG, mpanel);
       }

       else if (cmd.equals("H-Montage")) {
    	   eegmontage.Montage6(aEEG, mpanel);
       }

       else if (cmd.equals("Horizontally Split")) {
    	   splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
       }

       else if (cmd.equals("Vertically Split")) {
    	   splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
       }


       else if (cmd.equals("Patient-Document Laden")|cmd.equals("Load Patient-Document")) {
    	   loadDokument();
       }

       else if (cmd.equals("Patient-Document Speichern")|cmd.equals("Save Patient-Document")) {
    	   saveDokument();
       }

       else if (cmd.equals("Patient-Document Drucken")|cmd.equals("Print Patient-Document")) {
    	   printDokument();
       }
       
       for (int i=0;i<32;i++) {
    	   if (cmd.equals("Canal "+(i+1)) | cmd.equals("Kanal "+(i+1)))
			{ 
    		   canalliste[i] = i+1;  
    		   System.out.println(i+1);
			}
       }
       
       
	      
	      
       repaint();
     }


	public void itemStateChanged(ItemEvent event) {
		JComboBox selectedChoice = (JComboBox)event.getSource();

		for (int i=0;i<=32;i++)
		{

			if (selectedChoice.getSelectedItem().equals("Canal "+((i*8)+1)+"-"+(((i+1)*8))) |
				selectedChoice.getSelectedItem().equals("Kanal "+((i*8)+1)+"-"+(((i+1)*8))))
			{
				dpanel.setEinzelKanal(i*8+1);
				dpanel.setMehrKanal(1);
				//aEEG.getKanalNamen(i*8+1);
				//System.out.println("Canal "+(i*8+1)+"-"+((i+1)*8));
			}

			if (selectedChoice.getSelectedItem().equals("Canal "+(i+1)) |
				selectedChoice.getSelectedItem().equals("Kanal "+(i+1)))
			{
				dpanel.setEinzelKanal(i+1);
	 	  		dpanel.setMehrKanal(0);
	 	  		//aEEG.getKanalNamen(i+1);
	 	  		//System.out.println("Canal "+(i+1));
			}
		}
		for (int i=1;i<8;i++)
		{
			if (selectedChoice.getSelectedItem().equals(i+"ms"))
			{
				dpanel.setZeitintervall(i);
				spanel1.setZeitintervall(i);
				spanel2.setZeitintervall(i);
				//hpanel.HorizontalVergr(i);
			}
		}

		if (selectedChoice.getSelectedItem().equals("20s"))
		{
			dpanel.Ma�stab(true);
			dpanel.setZeitfenster(3);
			dpanel.HorizontalVergr(1);
			spanel1.Ma�stab(true);
			spanel1.setZeitfenster(3);
			spanel1.HorizontalVergr(1);
			spanel2.Ma�stab(true);
			spanel2.setZeitfenster(3);
			spanel2.HorizontalVergr(1);
		}

		if (selectedChoice.getSelectedItem().equals("10s"))
		{
			dpanel.Ma�stab(true);
			dpanel.setZeitfenster(3);
			dpanel.HorizontalVergr(2);
			spanel1.Ma�stab(true);
			spanel1.setZeitfenster(3);
			spanel1.HorizontalVergr(2);
			spanel2.Ma�stab(true);
			spanel2.setZeitfenster(3);
			spanel2.HorizontalVergr(2);
		}
		else if (selectedChoice.getSelectedItem().equals("5s"))
		{
			dpanel.Ma�stab(true);
			dpanel.setZeitfenster(3);
			dpanel.HorizontalVergr(4);
			spanel1.Ma�stab(true);
			spanel1.setZeitfenster(3);
			spanel1.HorizontalVergr(4);
			spanel2.Ma�stab(true);
			spanel2.setZeitfenster(3);
			spanel2.HorizontalVergr(4);
		}
		else if (selectedChoice.getSelectedItem().equals("1s"))
		{
			dpanel.Ma�stab(true);
			dpanel.setZeitfenster(3);
			dpanel.HorizontalVergr(20);
			spanel1.Ma�stab(true);
			spanel1.setZeitfenster(3);
			spanel1.HorizontalVergr(20);
			spanel2.Ma�stab(true);
			spanel2.setZeitfenster(3);
			spanel2.HorizontalVergr(20);
		}
		/*
		else if (selectedChoice.getSelectedItem().equals("0.5s"))
		{
			hpanel.Ma�stab(true);
			hpanel.setZeitfenster(3);
			hpanel.HorizontalVergr(40);
		}*/

		else if (selectedChoice.getSelectedItem().equals("Show"))
		{
			dpanel.Ma�stab(true);
		}
		else if (selectedChoice.getSelectedItem().equals("Hide"))
		{
			dpanel.Ma�stab(false);
		}
        repaint();
	}

	/*
	public void adjustmentValueChanged(AdjustmentEvent event) {
		{
           Adjustable ae = event.getAdjustable();
           if(ae.getOrientation()==Scrollbar.HORIZONTAL)
           {
        	   //System.out.println("Horizontal");
        	   dpanel.setXWert(event.getValue());
           }
           if(ae.getOrientation()==Scrollbar.VERTICAL)
           {
        	   //System.out.println("VERTICAL");
        	   dpanel.setYWert(event.getValue());
           }
           repaint();
        }
	}*/
}
