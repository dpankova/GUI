//Class for Holding Chooser Data
package service;

import javax.swing.*;
import java.awt.*;

public class ChooserDataSet
{
  public static final String home = System.getProperty("user.home"); 
  public Component parent;
  public JTextArea ta_path, ta_main;
  public String dir, dialog_name, extension_name, extension;
  public ChooserDataSet(Component parent, JTextArea ta_path, 
		 JTextArea ta_main, String dir, String dialog_name, 
		 String extension_name, String extension){
   
   this.parent = parent;
   this.ta_path = ta_path;
   this.dir = home+dir;
   this.dialog_name = dialog_name;
   this.extension_name = extension_name;
   this.extension = extension;
   this.ta_main = ta_main;
  }
   public ChooserDataSet(Component parent, JTextArea ta_path, 
		 JTextArea ta_main, String dir, String dialog_name){
   
   this.parent = parent;
   this.ta_path = ta_path;
   this.dir = home+dir;
   this.dialog_name = dialog_name;
   this.extension_name = " ";
   this.extension = " ";
   this.ta_main = ta_main;
  }
  //for selection without corresponding text area in GUI
   public ChooserDataSet(Component parent, 
		 JTextArea ta_main, String dir, String dialog_name){
   
   this.parent = parent;
   this.ta_path = null;
   this.dir = home+dir;
   this.dialog_name = dialog_name;
   this.extension_name = " ";
   this.extension = " ";
   this.ta_main = ta_main;
  }

	
}
