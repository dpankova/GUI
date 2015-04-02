// Thread that reads the output of the Firmware Process (don't touch it!!!)
package custom.threads;

import javax.swing.*;
import java.io.*;

import service.*;     

public class ReaderThread_firm extends Thread 
  {       
    public BufferedReader reader = null;
    public java.awt.Component target =null;
    public ReaderThread_firm(BufferedReader reader, java.awt.Component target ) 
      {
	this.reader = reader;
	this.target = target;
      }
    
    @Override
    public void run() 
      {
	Thread thisThread = Thread.currentThread();
	while (!thisThread.isInterrupted()) 
	{
	     try 
	     {
	       while (true)
	       {
		 String line = reader.readLine(); //read the line
		 if(line == null) break; 
		 
		 TAWriter.TAWrite(target, line); //send it to text area	    
	       }
	     } 
	     catch(IOException exception) 
	     {
	       TAWriter.TAWrite(target,new String("Reader_firm Thread error: " + exception.getMessage())); 
	     }
	}
      } 
} 
