//Custom action listener for b_sel_shell button (select nios shell, top left panel)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_destroy_firm implements ActionListener {
  
  private MyProcess myprocess;
  private JTextArea ta_main;
  public MyActionListener_destroy_firm(MyProcess myprocess, JTextArea ta_main) {
    this.myprocess = myprocess;
    this.ta_main = ta_main;
  }
  public void actionPerformed(ActionEvent e_b1)
    {
      long l =0;
      while (!myprocess.queue_kill.isEmpty()) 
      {
	try
	{
	  l = myprocess.queue_kill.remove();  
	    ThreadService.InterruptThread(l);
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Firmware:"+
						  " IllegalStateException was caught: "+
						  ex_b1.getMessage()));    
	}
      }
      try 
      {  
	myprocess.processInput.write("q\n");
	myprocess.processInput.flush(); 
	myprocess.processInput.write("pkill -9 nios2-terminal\n");
	myprocess.processInput.flush(); 
      } 
      catch  (IOException ex_b2) 
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware : "+
						"IOException was caught: "+
						ex_b2.getMessage()));   
      }
      myprocess.process.destroy();
    }
}
