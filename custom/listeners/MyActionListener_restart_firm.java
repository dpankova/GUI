//
package custom.listeners;

import service.*;
import custom.threads.*;

import javax.swing.*;
import java.util.concurrent.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;


public class MyActionListener_restart_firm implements ActionListener {
  
  public MyProcess myprocess;
  public ProcessBuilder processbuilder;
  public JTextArea ta_main;
  public java.awt.Component target;
  public MyActionListener_restart_firm( ProcessBuilder processbuilder,
					MyProcess myprocess,JTextArea ta_main,
					java.awt.Component target) {
    this.myprocess = myprocess;
    this.target = target;
    this.ta_main = ta_main;
    this.processbuilder=processbuilder;
  }
  
  public void actionPerformed(ActionEvent e_b1)
    {
      boolean flag = true;
      if (!myprocess.isRunning())
      {  
	flag = myprocess.setProcess();
	if (flag)
	{
	myprocess.setOutput();
	myprocess.setInput();
	
	ThreadStart reader_firm = new ThreadStart(myprocess,target);
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware: Process restarted")); 
	}
	else
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Firmware: Process restart failed, restart gui")); 
	}
      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware: Process is running already "+
						"interrunpt it first"));
      }
    }
  
}
