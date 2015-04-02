//Custom action listener for b_sel_save button (select save directory, over the main plot)
package custom.listeners;

import custom.listeners.*;
import service.*;
import custom.filechooser.*;

import javax.swing.*;
import java.awt.event.*; 

public class MyActionListener_dir_chooser extends MyActionListener_file_chooser {
 
  public MyActionListener_dir_chooser( ChooserDataSet set, FlagWatcher flag) {
    super(set,flag);
  }
  public MyActionListener_dir_chooser(ChooserDataSet set) {   
    super(set);
  }
  public void actionPerformed(ActionEvent e)
    {
      ChooseFile sdir = new ChooseFile (set);
      sdir.DirChooser();
      if (sdir.getOption() == JFileChooser.APPROVE_OPTION) 
      {       
	set.ta_path.setText(sdir.getPath());
	if(set.parent instanceof JToggleButton) 
	((JToggleButton)set.parent).setSelected(false);
	flag.setValue(true);
      }
      else
      {
	if(set.parent instanceof JToggleButton) 
	((JToggleButton)set.parent).setSelected(false);
	flag.setValue(false);
      }
    }
}

