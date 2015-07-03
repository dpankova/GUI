//
package custom.listeners;

import service.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;


public class MyActionListener_enter_nios implements ActionListener {
  
  private MyProcess myprocess;
  private JTextArea ta_shell;
  private JTextArea ta_main;
  public MyActionListener_enter_nios(MyProcess myprocess,
				     JTextArea ta_shell, JTextArea ta_main) {
    this.myprocess = myprocess;
    this.ta_shell = ta_shell;
    this.ta_main = ta_main;
  }
  
  public void actionPerformed(ActionEvent e_b1)
    {
      String filename;
      filename = ta_shell.getText();
      
      if (!myprocess.isRunning())
      {
	TAWriter.TAWrite_EDT(ta_main,new String("NIOS Shell: Process is dead. Restart it.")); 
      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String("NIOS Shell: Process is ready"));
	try
	{
	  myprocess.processInput.write(filename +" \n");
	  myprocess.processInput.flush(); 
	 
	} 
	catch  (IOException ex_b2) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Enter the NIOS: "+
						  "IOException was caught: "+
						  ex_b2.getMessage()));   
	}
      }
    }
  
}
