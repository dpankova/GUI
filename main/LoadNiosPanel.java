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
  
  static JButton b_load_nios = new JButton("<html><center>Load NIOS</center></html>");
  static JButton b_sel_file1 = new JButton("<html><center>...</center></html>"); 
  static JButton b_sel_file2 = new JButton("<html><center>...</center></html>");  
  static JLabel l_files = new JLabel("Configuration files:"); 
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
      
      add(b_sel_file1, "cell 1 1, width 50:50:50, height 25:25:25, align center, grow 0"); 
      add(b_sel_file2, "cell 1 2, width 50:50:50, height 25:25:25, align center, grow 0");
      add(b_load_nios, "cell 1 0, width 150:150:150, height 25:25:25, align, center, grow 0");
      add(sp_file1, "cell 0 1, width 100:800:800, height 35:35:35, growx");
      add(sp_file2, "cell 0 2, width 100:800:800, height 35:35:35, growx");
      add(l_files, "cell 0 0, width 100:140:140, height 25:25:25, grow 0");
      
      ChooserDataSet set_file1 = new ChooserDataSet(b_sel_file1, ta_file1, ta_main,
      "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw", 
						     "Select .elf file","elf files","elf");
      ChooserDataSet set_file2 = new ChooserDataSet(b_sel_file2, ta_file2, ta_main,
       "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw", 
						     "Select .sh file","sh files","sh"); 	
  
   
       
      b_load_nios.addActionListener(new MyActionListener_load_nios(myprocess, ta_file1, 
								   ta_file2, ta_main));
      b_sel_file1.addActionListener(new MyActionListener_file_chooser(set_file1));     
      b_sel_file2.addActionListener(new MyActionListener_file_chooser(set_file2));
       
    }
}
