//
package custom.listeners;

import service.*;
import custom.threads.*;

import javax.swing.*;
import java.util.concurrent.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;


public class MyActionListener_restart_nios implements ActionListener {
  
  public MyProcess myprocess;
  public ProcessBuilder processbuilder;
  public JTextArea ta_main;
  public GraphSaveComp gsc;
  public ChartPanel panel;
  public BlockingQueue<DataSet> queue;
  public java.awt.Component target;
  public MyActionListener_restart_nios( ProcessBuilder processbuilder,
					MyProcess myprocess, GraphSaveComp gsc, 
					ChartPanel panel, JTextArea ta_main,
					java.awt.Component target,
					BlockingQueue<DataSet> queue) {
    this.myprocess = myprocess;
    this.target = target;
    this.queue = queue; 
    this.gsc = gsc;
    this.panel = panel;
    this.ta_main = ta_main;
    this.processbuilder=processbuilder;
  }
  
  public void actionPerformed(ActionEvent e_b1)
    {
      Boolean flag;
      if (!myprocess.isRunning())
      {  
	flag = myprocess.setProcess();
	if (flag)
	{
	myprocess.setOutput();
	myprocess.setInput();
	
	ThreadStart reader = new ThreadStart(myprocess,target,queue);
	ThreadStart grapher = new ThreadStart(myprocess,gsc,panel,target,queue);
	ThreadStart saver = new ThreadStart(myprocess,gsc,target,queue);
	TAWriter.TAWrite_EDT(ta_main,new String("NIOS Shell: Process restarted")); 
	}
	else
	{
	  TAWriter.TAWrite_EDT(ta_main,new String("Nios Shell: Process restart failed, restart gui")); 
	}
       

      }
      else
      {
	TAWriter.TAWrite_EDT(ta_main,new String("NIOS Shell: Process is running already "+
						"interrunpt it first"));
	
      }
    }
  
}
