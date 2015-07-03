//Custom action listener for b_main_tofile button (Save button next to the main text area)
package custom.listeners;

import service.*;
import custom.filechooser.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar; 

public class MyActionListener_file_saver implements ActionListener{
  
  public ChooserDataSet set;
  static JTextField tf_name;
  public MyActionListener_file_saver( ChooserDataSet set, JTextField tf_name) {
    this.tf_name = tf_name;
    this.set = set;
  }
  public void actionPerformed(ActionEvent e)
    {
      try 
      {
	ChooseFile csave = new ChooseFile(set); 
	csave.FileSaver();
	
	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	String str = " ";
	if (tf_name.getText()==null)
	  str = new String (csave.getPath() +"/output_commands"+"_"+timeStamp+".txt");
	else
	  str = new String (csave.getPath() +"/"+ tf_name.getText()+"_"+timeStamp+".txt");
	
	File file = new File(str);
	BufferedWriter outfile = new BufferedWriter(new FileWriter(file,true));
	outfile.write(set.ta_main.getText());
	outfile.close();
	 
	TAWriter.TAWrite_EDT(set.ta_main, "Command Saver -  Saved "+str);	
      }
      catch(FileNotFoundException l) 
      {
	TAWriter.TAWrite_EDT(set.ta_main, "Command Saver -  File not found (or canceled)");
      }
       catch(NullPointerException j)
      {
	TAWriter.TAWrite_EDT(set.ta_main, "Command Saver -  Null pointer");
      }
      catch(IOException k)
      {
	TAWriter.TAWrite_EDT(set.ta_main, "Command Saver -  IO exeption");
      }
    }
}
