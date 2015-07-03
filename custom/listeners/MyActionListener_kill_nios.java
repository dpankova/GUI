//Custom action listenre for b_load_nios button (load nios, top right panel)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class MyActionListener_kill_nios implements ActionListener {
  
  public MyProcess myprocess;
  public JTextArea ta_main;
  public MyActionListener_kill_nios(MyProcess myprocess, JTextArea ta_file1) {
    this.myprocess = myprocess;
    this.ta_main = ta_main;
  }
  
  public void actionPerformed(ActionEvent e_b2)
    {
      
      try
      {
	if (myprocess.isRunning())
	{
	  myprocess.processInput.write("pkill -2 nios2-terminal\n");
	  myprocess.processInput.flush(); 	  
	}
	else 
	  TAWriter.TAWrite_EDT(ta_main,new String("NIOS2 process is stopped")); 
      } 
      catch  (IOException ex_b2) 
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Kill the NIOS2: "+
						"IOException was caught: "+ex_b2.getMessage()));   
      }
      
    }
  
}
