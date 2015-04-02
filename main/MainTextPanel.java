//LoadNiosPanel top right, panelc
package main;


import javax.swing.*;        
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;


import service.*;
import custom.listeners.*;

public class MainTextPanel extends JPanel{
  
  public JTextArea ta_main;
  static JButton b_main_tofile = new JButton("<html><center>Save</center></html>");  
  static JButton b_main_clear = new JButton("<html><center>Clear</center></html>");  
  static JLabel l_main = new JLabel("Command output:");   


  public MainTextPanel(JTextArea ta_main)
    {
      this.ta_main = ta_main;
       
      JScrollPane sp_main = new JScrollPane(ta_main);
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new MigLayout()); 
       
      ta_main.setEditable(false);
      ta_main.setFont(TAWriter.myFont);
      ta_main.setTabSize(4);
  
      TAWriter.TAWrite_EDT(ta_main,new String("SELECT SHELL FILE")); 
      TAWriter.TAWrite_EDT(ta_main,new String("SELECT SAVE FILE DIRECTORY")); 
      TAWriter.TAWrite_EDT(ta_main,new String("EXIT USING EXIT BUTTON")); 
 
       
      add(b_main_tofile, "cell 1 0, width 100:100:100, height 25:25:25, align right, grow 0");
      add(b_main_clear, "cell 1 0, width 100:100:100, height 25:25:25, align right, grow 0");
      add(sp_main, "cell 0 1 2 1, width 100:2000:2000, height 100:800:800, aligny top, grow");
      add(l_main, "cell 0 0, width 150:150:150, height 25:25:25, grow 0");
       
      ChooserDataSet set_main = new ChooserDataSet(b_main_tofile, ta_main, 
						   "/pingu","Save the commands into file"); 
       b_main_tofile.addActionListener(new MyActionListener_file_saver(set_main));    
       b_main_clear.addActionListener(new ActionListener()
	 { public void actionPerformed(ActionEvent e) { ta_main.setText(null); } }); 
       
    }
}
