//Class for Holding Chooser Data
package service;

import javax.swing.*;

public class GraphSaveComp
{

  static public JButton b_sel_save;
  static public JToggleButton tb_save; 
  static public JTextArea ta_save; 

  public GraphSaveComp(JButton b_sel_save, JToggleButton tb_save, 
		   JTextArea ta_save)
    {     
      this.b_sel_save = b_sel_save;
      this.tb_save = tb_save;
      this.ta_save = ta_save;  
    }	
  public JToggleButton getSaveButton()
    {
      return tb_save;
    }
   public JButton getSelectButton()
    {
      return b_sel_save;
    }
   public JTextArea getTextArea()
    {
      return ta_save;
    }
}
