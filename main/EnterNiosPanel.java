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

public class EnterNiosPanel extends JPanel{
  
  static JButton b_enter_nios = new JButton("<html><center>Enter NIOS2</center></html>");
  static JButton b_sel_shell = new JButton("<html><center>...</center></html>"); 
  static JLabel l_shell = new JLabel("Select NIOS2 shell file:");
  static JMenuItem i_mInterrupt = new JMenuItem("Interrupt");
  public Map <Integer, Component> map;
 
  public MyProcess myprocess;
  public JTextArea ta_main;
  public JTextArea ta_shell;
  public JFrame gui;
  public JButton b_exit;
  public EnterNiosPanel(JFrame gui, MyProcess myprocess, JMenu menu,  
			JTextArea ta_shell, JButton b_exit,
			Map<Integer, Component> map, JTextArea ta_main)
    {

      this.myprocess = myprocess;
      this.gui = gui;
      this.ta_main = ta_main;
      this.map = map;
      this.ta_shell = ta_shell;
      this.b_exit = b_exit;
      
      menu.add(i_mInterrupt);
      JScrollPane sp_shell = new JScrollPane(ta_shell);  
      
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new MigLayout()); 
      
      ta_shell.setEditable(false);
      ta_shell.setFont(TAWriter.myFont);

      menu.add(i_mInterrupt);
      
      add(l_shell,"cell 0 0, span 6, width 200:200:200, height 10:25:25, grow 0");
      add(b_sel_shell, "cell 5 1, width 50:50:50, height 25:25:25, grow 0");
      add(sp_shell,"cell 0 1 5 1, width 150:800:800, height 35:35:35, growx");
      add(b_enter_nios,"cell 0 2, span,  width 150:150:150, height 25:25:25,  grow 0");
      add(b_exit, "cell 0 2, span, width 150:150:150, height 25:25:25, grow 0");
      
      map.put(199,b_enter_nios); //add components to disable, big IDs choosen so they are original
     
      FlagWatcher flagDisable = new FlagWatcher();
      FlagListener flagListener  = new FlagListener(map);
      flagDisable.addPropertyChangeListener(flagListener);
      flagDisable.setValue(false);
     
      
      ChooserDataSet set_shell = new ChooserDataSet(b_sel_shell, ta_shell, ta_main,
    "/altera/13.1/nios2eds/", "NIOS command shell script","shell files","sh");  
    
      b_enter_nios.addActionListener(new MyActionListener_enter_nios(myprocess, ta_shell, ta_main));
      b_sel_shell.addActionListener(new MyActionListener_file_chooser(set_shell, flagDisable)); 
      i_mInterrupt.addActionListener(new MyActionListener_stop_nios(myprocess, gui, ta_main)); 
       
    }
 
}
