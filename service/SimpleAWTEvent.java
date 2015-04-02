// This service class nedeed to prevent the threads from accessing
// gui components directly (Swing is not thread safe).
// We define this special class as one of the measures 
// for correct dispatching of the tread main text area events.
// for more info: http://www.kauss.org/Stephan/swing/EventDemo.pdf
// credit: Stephan@Krauss.org

package service;

import javax.swing.*;        
import java.awt.*; 

public class SimpleAWTEvent extends AWTEvent 
{
  public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;
  private String str;
  public SimpleAWTEvent( Object target, String str)
    {
      super(target, EVENT_ID);
      this.str = str;
    } 
  
  public String getStr() { return( str ); }

}
