//Firmware panel
package main;

import javax.swing.*;        
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartPanel;
import java.util.concurrent.*;
import java.lang.*;

import service.*;
import custom.listeners.*;
import custom.threads.*;

public class FirmwarePanel extends JPanel{


  static JMenuItem i_fDestroy = new JMenuItem("Terminate");
  static JMenuItem i_fInfo = new JMenuItem("Info");
  static JButton b_load_firm = new JButton("<html><center>Load Firmware</center></html>");
  static JButton b_sel_firm = new JButton("<html><center>...</center></html>");  
  static JButton b_firm_stop= new JButton("<html><center>Interrupt</center></html>");
  static JLabel l_firm = new JLabel("Select Firmware file:");  
  static JTextArea ta_firm = new JTextArea("select .sof file");
  static JScrollPane sp_firm = new JScrollPane(ta_firm);

  public JMenu menu;
  public ProcessBuilder pb;
  public JTextArea ta_shell, ta_main;
  public JFrame gui;
  public FirmwarePanel(JFrame gui, MyProcess myprocess_firm, JMenu menu,  
		      JTextArea ta_shell, JTextArea ta_main)
    {
      this.menu  = menu; 
      this.pb = pb;
      this.gui = gui;
      this.ta_main = ta_main;
      this.ta_shell = ta_shell;
      
      ta_firm.setEditable(false);
      ta_firm.setFont(TAWriter.myFont);
      
      menu.add(i_fDestroy);
      menu.add(i_fInfo);
      
      setLayout(new MigLayout()); 
      setBorder(BorderFactory.createEtchedBorder());
      add(b_load_firm, "cell 0 2, span, width 150:150:150, height 25:25:25, align, center, grow 0");
      add(b_sel_firm, "cell 5 1, width 50:50:50, height 25:25:25, align center, grow 0");
      add(sp_firm, "cell 0 1, span 5, width 100:800:800, height 35:35:35, growx");
      add(l_firm, "cell 0 0, span, width 200:200:200, height 10:25:25, grow 0");
      add(b_firm_stop, "cell 0 2, span, width 150:150:150, height 25:25:25,  align, center, grow 0");
      
      ChooserDataSet set_firm = new ChooserDataSet(b_sel_firm, ta_firm, ta_main,
      "/pingu/gen2dom-fw/ddc2_sockit_nios2/quartus_project/output_files/", "Select .sof file","sof files","sof");
      
      b_load_firm.addActionListener(new MyActionListener_load_firm(myprocess_firm, ta_firm, ta_shell, ta_main));     
      b_sel_firm.addActionListener(new MyActionListener_file_chooser(set_firm));    
      b_firm_stop.addActionListener(new MyActionListener_firm_stop(myprocess_firm, ta_main));    
      i_fDestroy.addActionListener(new MyActionListener_destroy_firm(myprocess_firm,ta_main));  
      i_fInfo.addActionListener(new MyActionListener_firm_info(myprocess_firm, ta_main));   
           
    }
  
}
