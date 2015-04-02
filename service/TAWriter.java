//Writes into text area queue 
//First method to be used for EDT only
//Second to be used in threads
package service;

import service.SimpleAWTEvent;
import javax.swing.*;        
import java.awt.*;

public class TAWriter
{ 
  public static final Font myFont = new Font("Courier",Font.PLAIN, 12);
  public static void TAWrite_EDT(JTextArea area , String message ) 
    {
      area.append(message +"\n");
      area.setCaretPosition(area.getDocument().getLength());  
    }
  public static void TAWrite( java.awt.Component target , String message ) //using gui event queue
    { 
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue(); 
      eventQueue.postEvent( new SimpleAWTEvent(target, new String(message+ "\n") ));
    }
}
