//Custom action listenre for b_load_nios button (load nios, top right panel)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class MyActionListener_load_nios implements ActionListener {
  
  public MyProcess myprocess;
  public JTextArea ta_file1, ta_file2, ta_main;
  public MyActionListener_load_nios(MyProcess myprocess, JTextArea ta_file1,
				    JTextArea ta_file2, JTextArea ta_main) {
    this.myprocess = myprocess;
    this.ta_main = ta_main;
    this.ta_file1 = ta_file1;
    this.ta_file2 = ta_file2;
  }
  
  public void actionPerformed(ActionEvent e_b2)
    {
      String filename1, filename2;
      filename1 = ta_file1.getText();
      filename2 = ta_file2.getText();
      
      try
      {
	if (myprocess.isRunning())
	{
	  myprocess.processInput.write("nios2-download -g "+ filename1 +" && "+ 
				       filename2 +" | nios2-terminal \n");
	  myprocess.processInput.flush(); 	  
	}
	else 
	  TAWriter.TAWrite_EDT(ta_main,new String("Loading NIOS Shell: Process is dead")); 
      } 
      catch  (IOException ex_b2) 
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Load the NIOS: "+
						"IOException was caught: "+ex_b2.getMessage()));   
      }
      
    }
  
}
