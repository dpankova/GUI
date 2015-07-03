// Class for starting the treads in organized manner
package service; 

import javax.swing.*;
import java.util.concurrent.*; 
import org.jfree.chart.ChartPanel;
import java.io.*; 

import service.*;
import custom.threads.*;

public class ThreadStart
{
  public GraphSaveComp gsc; 
  public java.awt.Component target;
  public MyProcess myprocess;
  public ChartPanel panel;
  public BlockingQueue<DataSet> queue;
  private Thread thread = null; 

  public ThreadStart(MyProcess myprocess,
		     GraphSaveComp gsc,
		     ChartPanel panel,
		     java.awt.Component target,
		     BlockingQueue<DataSet> queue ) 
    {
      this.gsc = gsc;
      this.target= target;
      this.panel = panel;
      this.queue = queue;
      this.myprocess=myprocess;
      start("grapher");
    }
  public ThreadStart(MyProcess myprocess,
		     ChartPanel panel,
		     java.awt.Component target,
		     BlockingQueue<DataSet> queue ) 
    {
      this.target= target;
      this.panel = panel;
      this.queue = queue;
      this.myprocess=myprocess;
      start("hist_grapher");
    }
  
  public ThreadStart(MyProcess myprocess,
		     GraphSaveComp gsc,
		     java.awt.Component target,
		     BlockingQueue<DataSet> queue ) 
    {
      this.gsc = gsc;
      this.target= target;
      this.queue = queue;
      this.myprocess=myprocess;
      
      start("saver");
    }
  public ThreadStart(MyProcess myprocess, 
		     java.awt.Component target,
		     BlockingQueue<DataSet> queue ) 
    {
      this.queue = queue;
      this.target= target;
      this.myprocess=myprocess;

      start("reader");
    }
   public ThreadStart(MyProcess myprocess, 
		      java.awt.Component target) 
    {
      this.target= target;
      this.myprocess=myprocess;
   
      start("reader_firm");
    }
  
  private void start(String type)
    {
      switch(type)
      {
      case "reader":
	thread = new ReaderThread(myprocess.processOutput,target,queue);
	break;
      case "reader_firm":
	thread = new ReaderThread_firm(myprocess.processOutput,target);
	break;
      case "grapher":
	thread = new GrapherThread(gsc,panel,target,queue);
	break;
      case "hist_grapher":
	thread = new HistGrapherThread(panel,target,queue);
	break;
      case "saver":
	thread = new SaverThread(gsc,target,queue);
	break;
      default: 
	TAWriter.TAWrite(target, new String("Start Tread Type Error "+type));
	break;
      }
      thread.start(); 
      try
      {
	myprocess.queue_kill.add(thread.getId());
      }
      catch  (IllegalStateException ex_b1) 
      { 
	TAWriter.TAWrite(target,new String("Start Tread Error"+type+
						ex_b1.getMessage()));
      }
    }
}
