//Custom action listener for b_exit button (orange Exit button, top left panel)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_exit implements ActionListener {
  
  private MyProcess myprocess;
  private MyProcess myprocess2;
  private JTextArea ta_main;
  public MyActionListener_exit(MyProcess myprocess,MyProcess myprocess2,
			       JTextArea ta_main) {
    this.myprocess = myprocess;
    this.myprocess2 = myprocess2;
    this.ta_main = ta_main;
  }
  public void actionPerformed(ActionEvent e_b1)
    {//destroy threads created by main process
      while (!myprocess.queue_kill.isEmpty())//while they exist 
      {
	try
	{
	  long l = myprocess.queue_kill.remove(); //get one
	  ThreadService.InterruptThread(l);//interrrupt it
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Exit: "+
						  " IllegalStateException was caught: "+
						  ex_b1.getMessage()));    
	}
      }
       try 
      {  
	
	myprocess2.processInput.write("q \n"); //stop the firmware
	myprocess2.processInput.flush(); 
	myprocess2.processInput.write("pkill -9 nios2-terminal \n");
	myprocess2.processInput.flush(); 
	
      } 
      catch  (IOException ex_b2) 
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Exit: "+
						"IOException was caught: "+
						ex_b2.getMessage()));   
      }
     
      while (!myprocess2.queue_kill.isEmpty()) 
      {	
	try
	{
	  long l = myprocess2.queue_kill.remove();  
	  ThreadService.InterruptThread(l);
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Exit: "+
						  " IllegalStateException was caught: "+
						  ex_b1.getMessage()));    
	}
      }
      myprocess.process.destroy();
      myprocess2.process.destroy();
      System.exit(0);
    }
}
