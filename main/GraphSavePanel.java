//Class for Holding graph elements
package main;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.util.*;
import java.awt.*;

import service.*;
import custom.filechooser.*;
import custom.listeners.*;

public class GraphSavePanel extends JPanel
{
 
  public JToggleButton tb_save = new JToggleButton("<html><center>Save Data</center></html>");
  public JButton b_sel_save= new JButton("<html><center>...</center></html>");
  public JTextArea ta_save = new JTextArea("select where to save");
  public JScrollPane sp_save = new JScrollPane(ta_save); 
  public JTextArea ta_main;

  public GraphSavePanel(JTextArea ta_main) 
    { 
      this.ta_main = ta_main;
   
      ta_save.setEditable(false);
      ta_save.setFont(TAWriter.myFont);
      
      setLayout(new MigLayout()); 
      setBorder(BorderFactory.createEtchedBorder());  
      add(tb_save, "cell 2 0, width 150:150:150, height 25:25:25, align right, grow 0");
      add(sp_save, "cell 0 0 , width 100:1000:1000, height 35:35:35, aligny top, grow");
      add(b_sel_save, "cell 1 0, width 50:50:50, height 25:25:25, align center, grow 0");  

      Map<Integer, Component> mapDisableSave= new HashMap<Integer, Component> (2);
      mapDisableSave.put(0,tb_save);
      
      FlagWatcher flagDisableSave = new FlagWatcher();
      FlagListener flagListenerSave  = new FlagListener(mapDisableSave);
      flagDisableSave.addPropertyChangeListener(flagListenerSave);
      flagDisableSave.setValue(false);
      
      ChooserDataSet set_save = new ChooserDataSet(tb_save, ta_save, ta_main,
						   "/pingu/data/", 
						   "Chose File Save Directory"); 

      b_sel_save.addActionListener(new MyActionListener_dir_chooser(set_save,flagDisableSave));       

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
