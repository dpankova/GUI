//Custom action listener for b_main_tofile button (Save button next to the main text area)
package custom.listeners;

import service.*;
import custom.filechooser.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*; 

public class MyActionListener_file_saver implements ActionListener{
  
  protected ChooserDataSet set;
  public MyActionListener_file_saver( ChooserDataSet set) {
    this.set = set;
  }
  public void actionPerformed(ActionEvent e)
    {
      try 
      {
	ChooseFile csave = new ChooseFile(set); 
	csave.FileSaver();
	
	File file = new File(new String (csave.getPath() + "/out_commands.txt"));
	BufferedWriter outfile = new BufferedWriter(new FileWriter(file,true));
	outfile.write(set.ta_main.getText());
	outfile.close();
	 
	TAWriter.TAWrite_EDT(set.ta_main, "Command Saver -  Saved");	
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
