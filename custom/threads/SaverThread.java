// Writes dataset into file

package custom.threads;

import javax.swing.*;
import java.io.*;
import java.util.concurrent.*;

import service.*;  

public class SaverThread extends Thread {    
  public Writer writer =null;
  public BlockingQueue<DataSet> queue;
  public String name =null;
  public GraphSaveComp gr_el;
  public java.awt.Component target;
  public SaverThread(GraphSaveComp gr_el, java.awt.Component target,  
		     BlockingQueue<DataSet> queue) {
    this.queue = queue;
    this.gr_el = gr_el;
    this.target = target;
  }
    
  @Override
  public void run() 
    {	
      //DataSet set; data queue element
      Thread thisThread = Thread.currentThread();
      while (!thisThread.isInterrupted()) 
      {
   	  while (gr_el.tb_save.isSelected()) //if user wants data saved
	  {     
	      while (true)
	      {
		DataSet set = queue.peek();//take data from queue
		if (set == null) break;//since grapher thread is not gonna take it		
		if (!set.graphed) //so we don't use data before grapher 
		  continue;
		if (!set.histgraphed) //so we don't use data before hist grapher 
		  continue;
	        set = queue.poll();
		try
		{
		  //full file name
		  name = new String(gr_el.ta_save.getText() + set.name + ".txt");	     
		  
		  //write into file -----
		  writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(name), "utf-8"));
		  writer.write(new String ("isamp"+"\t"+ "adc"+"\t"+ "time"+
					   "\t"+ "tot"+"\t"+"eoe"+"\n"));
		  
		  for (int i=0; i<set.getLength(); i++)		       
		    writer.write(new String(set.isamp[i]+"\t"+set.adc[i]+"\t"+set.time[i]+
					    "\t"+set.tot[i]+"\t"+set.eoe[i]+"\n"));
		  //----------------------
		  TAWriter.TAWrite(target,new String("File saved"+ name)); 
		}
		catch (IOException ex) 
		{
		  TAWriter.TAWrite(target,new String("SaveThread1 error: " +
						     ex.getMessage()));
		}
		finally 
		{
		  try
		  { 		       
		    writer.close();//no matter what happens try to close file
		  }
		  catch (Exception ex)
		  {
		    TAWriter.TAWrite(target,new String("SaverThread error: " + 
						       ex.getMessage())); 
		  }
		}
	      } 
	      if(thisThread.isInterrupted())
		break;
	  }
      }  
    } 
} 

