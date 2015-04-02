//Custom action listener for i_service menu item (Help/Service Console)
package custom.listeners;

import javax.swing.*;
import java.awt.event.*; 

public class MyActionListener_service implements ActionListener {
  
  private static int DISPOSE_ON_CLOSE = 2;
  public java.awt.Component target;
  public JPanel panel_s;
  public MyActionListener_service(java.awt.Component target, JPanel panel_s) {
    this.target = target;
    this.panel_s = panel_s;
  }
  public void actionPerformed(ActionEvent e_b1)
    {
      //create pop up window
      JDialog d_service = new JDialog((JFrame)target, "Service console");
      d_service.getContentPane().add(panel_s);
      d_service.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      d_service.setSize(400,175);
      d_service.setLocation(target.getLocation());
      // make it visible
      javax.swing.SwingUtilities.invokeLater(new Runnable() 
	{
	  public void run() 
	    {
	      d_service.setVisible(true);
	    }
	});
    }
}  
