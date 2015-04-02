// Class for keeping the process and things realted to it
package service; 

import javax.swing.*;  
import java.io.*;      
import java.util.*;
import service.*;


public class MyProcess
{
  public ProcessBuilder processBuilder;
  public JTextArea ta_main;
  public Process process;
  public BufferedReader processOutput;
  public BufferedWriter processInput;
  public Queue<Long> queue_kill = new LinkedList<Long>();
  
  public MyProcess(ProcessBuilder processBuilder, JTextArea ta_main) 
    {
      this.processBuilder = processBuilder;
      this.ta_main = ta_main;
    }
  
  public boolean setProcess()
    {
      boolean flag;
      try
      {
	process = processBuilder.start();
	return flag = true;
      }	 
      catch  (IOException ex_b2) 
      {
	TAWriter.TAWrite_EDT(ta_main,new String("MyProcess NIOS: "+
						"IOException was caught: "+
						ex_b2.getMessage()));  
	return flag = false;
      }
    }
  
  public void setOutput()
    {
      processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
  public void setInput()
    {
      processInput = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }  
 
  public boolean isRunning() {
    try 
    {
      process.exitValue();
      return false;
    } 
    catch (Exception e) 
    {
      return true;
    }
  }
 
}
