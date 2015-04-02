//Custom action listener for b_run button (Run button in a service panel)
package custom.listeners;

import service.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_run implements ActionListener {
 
  public MyProcess myprocess;
  public MyProcess myprocess2;
  public java.awt.Component target;
  public JTextField field_s;
  public JRadioButton br_m;
  public MyActionListener_run(MyProcess myprocess, MyProcess myprocess2, JTextField field_s, 
			      JRadioButton br_m, java.awt.Component target) {
    this.myprocess = myprocess;
    this.myprocess2 = myprocess2;
    this.br_m = br_m;
    this.field_s = field_s;
    this.target = target;
  }
  public void actionPerformed(ActionEvent e_b1)
    {
      if(br_m.isSelected())
      {
	if (myprocess.isRunning())
	{
	  try
	  {
	    myprocess.processInput.write(new String(field_s.getText() +" \n"));
	    myprocess.processInput.flush(); 
	  } 
	  catch  (IOException ex_b2) 
	  {
	    TAWriter.TAWrite(target,new String("Service console: "+
						    "IOException was caught: "+
						    ex_b2.getMessage()));   
	  }
	  TAWriter.TAWrite(target,new String("Service console: main input")); 
	}
	else
	{
	  TAWriter.TAWrite(target,new String("Service console: main process is dead")); 
	} 
      }
      else
      {
	if (myprocess2.isRunning())
	{
	  try
	  {
	    myprocess2.processInput.write(new String(field_s.getText() +" \n"));
	    myprocess2.processInput.flush(); 
	  } 
	  catch  (IOException ex_b2) 
	  {
	    TAWriter.TAWrite(target,new String("Service console: "+
						    "IOException was caught: "+
						    ex_b2.getMessage()));   
	  }
	  TAWriter.TAWrite(target,new String("Service console: firmware input ")); 
	}
	else
	{
	  TAWriter.TAWrite(target,new String("Service console: firmware  process is dead")); 
	} 
      }
    }
}  
