// Custom class to manage selection of files
// Three different methods for choosing file, saving file and choosing directory
// and two types of constructor
// path - contains full path to the choosen file/directory
// option - tell which dialog button was pressed (OK, CANCEL) of if there is an error

package custom.filechooser;  

import javax.swing.JFileChooser;
import java.awt.Component;
import javax.swing.filechooser.*;
import javax.swing.*;

import service.*;

public class ChooseFile
{
  private String path;
  private int option;
  private ChooserDataSet set;  
  public ChooseFile (ChooserDataSet set)
    {
      this.set = set;
    }
  public String getPath(){return path;}
  public int getOption(){return option;}
 
  public void FileChooser()
    {
      JFileChooser chooser = new JFileChooser(); 
      FileNameExtensionFilter filter = new FileNameExtensionFilter(set.extension_name, set.extension);
      chooser.setCurrentDirectory(new java.io.File(set.dir));
      chooser.setDialogTitle(set.dialog_name);
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      chooser.setFileFilter(filter);                       //filter extentions
      // chooser.setAcceptAllFileFilterUsed(false); 	   // disable the "All files" option.
      
      option = chooser.showOpenDialog(set.parent); 
      if (option == JFileChooser.APPROVE_OPTION) 
      {
        path = chooser.getSelectedFile().getAbsolutePath();
	TAWriter.TAWrite_EDT(set.ta_main, new String(set.dialog_name + ": file saved -"+path)); 
      }
      else
      {
	TAWriter.TAWrite_EDT(set.ta_main, new String(set.dialog_name + ": not saved")); 
      }
    }

  public void FileSaver()
    {
      JFileChooser chooser = new JFileChooser(); 
      chooser.setCurrentDirectory(new java.io.File(set.dir));
      chooser.setDialogTitle(set.dialog_name);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      option = chooser.showSaveDialog(set.parent); 
      if (option == JFileChooser.APPROVE_OPTION) 
      {
        path = chooser.getSelectedFile().getAbsolutePath();
	TAWriter.TAWrite_EDT(set.ta_main, new String(set.dialog_name + ": file saved -"+path)); 
      }
      else
      {
	TAWriter.TAWrite_EDT(set.ta_main, new String(set.dialog_name + ": not saved")); 
      }
    }
  
  public void DirChooser()
    {
      JFileChooser chooser = new JFileChooser();    
      chooser.setCurrentDirectory(new java.io.File(set.dir));
      chooser.setDialogTitle(set.dialog_name);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	
      option = chooser.showOpenDialog(set.parent); 
      if (option == JFileChooser.APPROVE_OPTION) 
      {
        path = new String(chooser.getSelectedFile().getAbsolutePath()+"/");
	TAWriter.TAWrite_EDT(set.ta_main, new String(set.dialog_name + ": file saved -"+path)); 
      }
      else
      {
	TAWriter.TAWrite_EDT(set.ta_main, new String(set.dialog_name + ": not saved")); 
      }
    }
}
