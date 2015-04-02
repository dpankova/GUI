//Custom action listener for firmware/info menu item 
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_firm_info implements ActionListener {
  
  private MyProcess myprocess;
  private JTextArea ta_main;
  public MyActionListener_firm_info(MyProcess myprocess, JTextArea ta_main ) {
    this.myprocess = myprocess;
    this.ta_main = ta_main;
  }
 
  public void actionPerformed(ActionEvent e_b1)
    {
      boolean flag = true;
      long l =0;
      if (myprocess.isRunning())
      {
	
	try
	{
	  myprocess.processInput.write( "i \n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Info firmware: "+
						  "IOException was caught: "+ex_b1.getMessage()));    
	}	
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware info requested"));
      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String(" Firmware: Process is dead"));
      } 
    }
}
