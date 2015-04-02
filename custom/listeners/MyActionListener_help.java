//Custom action listener for i_help menu item (Help/Help)
package custom.listeners;

import javax.swing.*;
import java.awt.event.*; 
import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class MyActionListener_help implements ActionListener {
  
  public static int DISPOSE_ON_CLOSE = 2;
  public JTabbedPane tab_panel = new JTabbedPane();         
  public JPanel p_help = new JPanel(new MigLayout());
  public JPanel p_diag = new JPanel(new MigLayout());
  public JFrame target;
  
  public MyActionListener_help(JFrame target) {
    this.target = target;
  }
  public void actionPerformed(ActionEvent e_b1)
    {      
      p_help.setBorder(BorderFactory.createEtchedBorder());
      p_diag.setBorder(BorderFactory.createEtchedBorder());
      
      tab_panel.add("Help",p_help);
      tab_panel.add("Diagram",p_diag);
      
      JDialog d_help = new JDialog(target, "Help and Diagram");
      d_help.getContentPane().add(tab_panel);
      d_help.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      d_help.setSize(500,500);
      d_help.setLocation(target.getLocation());
      javax.swing.SwingUtilities.invokeLater(new Runnable() 
	{
	  public void run() 
	    {
	       d_help.setVisible(true);
	    }
	});
    }
}  
