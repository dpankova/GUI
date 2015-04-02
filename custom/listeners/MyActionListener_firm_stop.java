//Custom action listener for b_firm_stop button (Interrupt, top middle panel)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class MyActionListener_firm_stop implements ActionListener {
  
  private MyProcess myprocess;
  private JTextArea ta_main;
  public MyActionListener_firm_stop(MyProcess myprocess, JTextArea ta_main) {
    this.myprocess = myprocess;
    this.ta_main = ta_main;
  }
 
  public void actionPerformed(ActionEvent e_b1)
    {
      boolean flag = true;
      long l =0;
      if (myprocess.isRunning())
      {
	while (!myprocess.queue_kill.isEmpty()) 
	{
	  try
	  {
	    l = myprocess.queue_kill.remove();
	    TAWriter.TAWrite_EDT(ta_main,new String(Long.toString(l)));    
	    ThreadService.InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWriter.TAWrite_EDT(ta_main,new String("Load firmware: "+
						    " IllegalStateException was caught: "+ex_b1.getMessage()));    
	  }
        }
	
	try
	{
	  myprocess.processInput.write( "q \n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Stop firmware: "+
						  "IOException was caught: "+ex_b1.getMessage()));    
	}
	
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware stopped"));
      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String(" Firmware: Process is dead"));
	while (!myprocess.queue_kill.isEmpty()) 
	{
	  try
	  {
	    l = myprocess.queue_kill.remove();
	    TAWriter.TAWrite_EDT(ta_main,new String(Long.toString(l)));
	    ThreadService.InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWriter.TAWrite_EDT(ta_main,new String("Load firmware: "+
						    " IllegalStateException was caught: "+ex_b1.getMessage()));    
	  }
        }
      } 
    }
}
