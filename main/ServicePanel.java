//LoadNiosPanel top right, panelc
package main;

import javax.swing.*;        
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import java.util.*;

import service.*;
import custom.listeners.*;

public class ServicePanel extends JPanel{
  
  static JMenuItem i_service = new JMenuItem("Service Input");
  static JTextField field_s = new JTextField();
  static JScrollPane sp_field = new JScrollPane(field_s);
  static JButton b_run = new JButton("<html><center>Run</center></html>");
  static JRadioButton br_m = new JRadioButton("Main");
  static JRadioButton br_f = new JRadioButton("Firmware");
  static ButtonGroup gr_br = new ButtonGroup();
  static JLabel l_s1 = new JLabel("Chose process:");
  static JLabel l_s2 = new JLabel("Enter the command:");
  static JMenuItem i_help = new JMenuItem("Help");
 
  public MyProcess myprocess;
  public MyProcess myprocess_firm;
  public JMenu menu;
  public java.awt.Component target;
  public ServicePanel(java.awt.Component target, MyProcess myprocess, MyProcess myprocess_firm, 
		      JMenu menu, Map<Integer, Component> map)
    {

      this.myprocess = myprocess;
      this.myprocess_firm = myprocess_firm;
      this.menu = menu;
      this.target = target;
    
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new MigLayout()); 
      
      field_s.setFont(TAWriter.myFont);
      
      gr_br.add(br_m);
      gr_br.add(br_f);
      
      menu.add(i_service); 
      menu.add(i_help);
  
      map.put(99,i_service);
      
      add(br_m, "cell 0 1 , width 100:100:100, height 25:25:25, align center, grow 0");
      add(br_f, "cell 1 1, width 100:100:100, height 25:25:25, align center, grow 0");
      add(sp_field, "cell 0 3 2 1 , width 100:1000:1000, height 35:35:35, aligny top, growx");
      add(b_run, "cell 1 4, width 100:100:100, height 25:25:25, align right, grow 0");
      add(l_s1, "cell 0 0, width 150:150:150, height 25:25:25, grow 0");
      add(l_s2, "cell 0 2, width 150:150:150, height 25:25:25, grow 0");
      
      i_service.addActionListener(new MyActionListener_service(target, this));
      b_run.addActionListener(new MyActionListener_run(myprocess, myprocess_firm, field_s, br_m, target));
      i_help.addActionListener(new MyActionListener_help((JFrame)target));
          
    }
}
