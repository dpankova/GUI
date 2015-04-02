//Custom action listener for i_mInterrupt menu item (Process/Interrupt)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_stop_nios implements ActionListener {
  
  public MyProcess myprocess;
  public JTextArea ta_main;
  public java.awt.Component target = null;
  public MyActionListener_stop_nios( MyProcess myprocess, java.awt.Component target, 
				     JTextArea ta_main ) {
    this.myprocess = myprocess;
    this.target = target;
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
	    ThreadService.InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWriter.TAWrite_EDT(ta_main,new String("ENter NIOS: "+
						    " IllegalStateException was caught: "
						    +ex_b1.getMessage()));    
	  }
        }
	
	myprocess.process.destroy();
	TAWriter.TAWrite_EDT(ta_main,new String("NIOS stopped"));
      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String("NIOS: Process is dead"));
	while (!myprocess.queue_kill.isEmpty()) 
	{
	  try
	  {
	    l = myprocess.queue_kill.remove();
	    ThreadService.InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWriter.TAWrite_EDT(ta_main,new String("Load firmware: "+
						    " IllegalStateException was caught: "+
						    ex_b1.getMessage()));    
	  }
        }
      } 
    }
}
