//Custom action listener for b_load_firm button (loas firmware, top middle panel)
package custom.listeners;

import service.*;
import custom.threads.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_load_firm implements ActionListener {
  
  public MyProcess myprocess;
  public JTextArea ta_firm, ta_shell, ta_main;
  public java.awt.Component target = null;
  public MyActionListener_load_firm( MyProcess myprocess, JTextArea ta_firm, 
				     JTextArea ta_shell, JTextArea ta_main) {
    this.myprocess = myprocess;
    this.target =target;
    this.ta_main = ta_main;
    this.ta_firm = ta_firm;
    this.ta_shell = ta_shell;
  }
 
  public void actionPerformed(ActionEvent e_b1)
    {
      String filename, filename_sh;
      filename = ta_firm.getText();
      filename_sh = ta_shell.getText();
      
      ReaderThread_firm reader_firm;
      
      if (!myprocess.isRunning())
      {	
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware: Process is dead"));
      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware: Process is ready"));
	try
	{
	  myprocess.processInput.write(filename_sh +" \n");
	  myprocess.processInput.flush();
	  myprocess.processInput.write("nios2-configure-sof "+filename +"\n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Load firmware: "+
						  "IOException was caught: "+ex_b1.getMessage()));    
	}
      } 
    }
}
