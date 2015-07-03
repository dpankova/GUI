//LoadNiosPanel top right, panelc
package main;


import javax.swing.*;        
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;


import service.*;
import custom.listeners.*;

public class LoadNiosPanel extends JPanel{
  
  static JButton b_load_nios = new JButton("<html><center>Load NIOS2</center></html>");
  static JButton b_kill_nios = new JButton("<html><center>Free JTAG UART</center></html>");
  static JButton b_sel_file1 = new JButton("<html><center>...</center></html>"); 
  static JButton b_sel_file2 = new JButton("<html><center>...</center></html>");  
  static JLabel l_file1 = new JLabel("Select Configuration file .sh:"); 
  static JLabel l_file2 = new JLabel("Select Configuration file .elf:"); 
  static JTextArea ta_file1 = new JTextArea("select file 1");
  static JScrollPane sp_file1 = new JScrollPane(ta_file1);
  static JTextArea ta_file2 = new JTextArea("select file 2");
  static JScrollPane sp_file2 = new JScrollPane(ta_file2);
  static JMenuItem i_mInterrupt = new JMenuItem("Interrupt");

  public MyProcess myprocess;
  public JTextArea ta_main;
  public LoadNiosPanel(JTextArea ta_main, MyProcess myprocess)
    {

      this.myprocess = myprocess;
      this.ta_main = ta_main;
           
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new MigLayout()); 
      
       Font myFont = TAWriter.myFont;
      ta_file1.setEditable(false);
      ta_file2.setEditable(false);
      ta_file1.setFont(myFont);
      ta_file2.setFont(myFont);
      
      add(b_sel_file1, "cell 5 1, width 50:50:50, height 25:25:25, grow 0"); 
      add(b_sel_file2, "cell 5 3, width 50:50:50, height 25:25:25 , grow 0");
      add(b_load_nios, "cell 0 5, span, width 150:150:150, height 25:25:25, grow 0");
      add(b_kill_nios,"cell 0 5, span, width 150:150:150, height 25:25:25,  grow 0");
      add(sp_file1, "cell 0 1 5 1, width 150:800:800, height 35:35:35, growx");
      add(sp_file2, "cell 0 3 5 1, width 150:800:800, height 35:35:35, growx");
      add(l_file1, "cell 0 0, span, width 200:250:250, height 25:25:25, grow 0");
      add(l_file2, "cell 0 2, span, width 200:250:250, height 25:25:25, grow 0");
      
      ChooserDataSet set_file1 = new ChooserDataSet(b_sel_file1, ta_file1, ta_main,
      "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw", 
						     "Select .elf file","elf files","elf");
      ChooserDataSet set_file2 = new ChooserDataSet(b_sel_file2, ta_file2, ta_main,
       "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw", 
						     "Select .sh file","sh files","sh"); 	
  
      
       
      b_load_nios.addActionListener(new MyActionListener_load_nios(myprocess, ta_file1, 
								   ta_file2, ta_main));
      b_kill_nios.addActionListener(new MyActionListener_kill_nios(myprocess, ta_main));
      b_sel_file1.addActionListener(new MyActionListener_file_chooser(set_file1));     
      b_sel_file2.addActionListener(new MyActionListener_file_chooser(set_file2));
       
    }
}
