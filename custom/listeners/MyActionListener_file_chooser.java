//Custom action listener for b_sel_shell button (select nios shell, top left panel)
package custom.listeners;

import service.*;
import custom.filechooser.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*; 
import java.util.*;

public class MyActionListener_file_chooser implements ActionListener {
  public FlagWatcher flag;
  public ChooserDataSet set;
  
  public MyActionListener_file_chooser( ChooserDataSet set, FlagWatcher flag) {
    this.flag = flag;
    this.set = set;
  }

  public MyActionListener_file_chooser(ChooserDataSet set) {   
    this.set = set;
    this.flag = new FlagWatcher();
  }
    public void actionPerformed(ActionEvent e_b2)
      {
	ChooseFile cfile = new ChooseFile(set);
	cfile.FileChooser();
	if (cfile.getOption() == JFileChooser.APPROVE_OPTION) 
	{	 
	  flag.setValue(true); 
	  set.ta_path.setText(cfile.getPath());
	}
	else 
	{
	  flag.setValue(false); 
	  set.ta_path.setText("No Selection");
	}       
      }
  }
