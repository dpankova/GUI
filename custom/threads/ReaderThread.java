// Thread that reads the output of the main process, selects the data and puts it into the 
// blocking queue
// NOTE unlike listeners it uses different thread safe writer
package custom.threads;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.regex.*;
import java.util.concurrent.*;

import service.*;  

public class ReaderThread extends Thread 
  {    
    public static Pattern pattern_data = Pattern.compile("([0-9]{1,10},[ ]{1,10}){5}");
    public static Pattern pattern_time = Pattern.compile("start timestamp");
    public static Pattern pattern_header = Pattern.compile("isamp   adc   time  tot  eoe");
    public static Pattern pattern_nsamp = Pattern.compile("Nsamples");
    public Matcher matcher_data = pattern_data.matcher(" ");
    public Matcher matcher_time = pattern_time.matcher(" ");
    public Matcher matcher_header = pattern_header.matcher(" ");
    public Matcher matcher_nsamp = pattern_nsamp.matcher(" ");
    
    public static final int N_const = 256;
    static DateFormat df = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss");

    public BufferedReader reader = null;
    public BlockingQueue<DataSet> queue;
    public java.awt.Component  target = null;     // The component which will receive the event.
    public ReaderThread(BufferedReader reader, java.awt.Component target, 
			BlockingQueue<DataSet> queue) 
      {
	this.reader = reader;
	this.queue = queue;
	this.target = target;
      }

    @Override
    public void run() 
      {
	int N_samp = N_const;
	int count = 0;
	DataSet set = new DataSet(N_samp);
	try 
	{
	  Thread thisThread = Thread.currentThread();
	  while (!thisThread.isInterrupted()) 
	  {
	    while (true) 
	    {
	      final String line = reader.readLine();
	      if (line == null) break; 
	      String[] parts;
	     
	          matcher_data.reset(line); //recognize and get gata
		  if (matcher_data.find())
		  {
		    parts = line.split(",[ ]{0,10}");	      
		    set.isamp[count] = Integer.parseInt(parts[0].trim());
		    set.adc[count] = Integer.parseInt(parts[1].trim());
		    set.time[count] = Integer.parseInt(parts[2].trim());
		    set.tot[count] = Integer.parseInt(parts[3].trim());
		    set.eoe[count] = Integer.parseInt(parts[4].trim());
		    count++;
		    if (count == N_samp) //if we got as much data as we expected from firmware
		    {
		      if (set.eoe[count-1] == 1) // the last data line in the run has eoe=1 
		      {
			set.graphed = false;
			set.histgraphed = false;
			queue.put(set);
			count = 0;
		      } 
		      else 
			TAWriter.TAWrite(target, new String("This data run is bad"));
		    }
		    continue;
		  }
		  
		  
		  matcher_header.reset(line);//remove data header from display
		  if (matcher_header.find())
		    continue;

		  
		  matcher_nsamp.reset(line); //get number of samples from data
		  if (matcher_nsamp.find())
		  {
		    parts = line.split("=");
		    N_samp = Integer.parseInt(parts[1].trim());
		    set.UpdateSize(N_samp);
		  }

		  
		  matcher_time.reset(line); //get timestamp
		  if (matcher_time.find())
		  {
		    parts = line.split("[,]{0,1}[ ]{1,10}");
		    Timestamp stamp = new Timestamp(Long.parseLong(parts[3].trim(),10));
		    java.sql.Date date = new java.sql.Date(stamp.getTime());
		    set.name = df.format(date); 
		  }
		  TAWriter.TAWrite(target, line);
	      
	     if(thisThread.isInterrupted())
	       break;
	      
	    }
	  } 
	}
	catch(IOException exception)   
	{
	  TAWriter.TAWrite(target, new String("Reader Thread error: " +
					      exception.getMessage())); 
	}	
	catch(InterruptedException exception2) 
	{
	  TAWriter.TAWrite(target, new String("Reader Thread error: " + 
					      exception2.getMessage())); 
	}
      }    
 } 
