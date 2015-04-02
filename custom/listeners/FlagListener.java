//Class actiong on value change of boolean variable
package custom.listeners;

import java.awt.*; 
import java.util.*;
import java.beans.*;
import java.util.Map.Entry;

public class FlagListener implements PropertyChangeListener {
  private Map<Integer, Component> map;
  public FlagListener (Map<Integer, Component> map){
    this.map = map; 
  }
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("FlagWatcher")) {
	  boolean setTo = event.getNewValue().equals(true);
	  for (Map.Entry<Integer, Component> entry : map.entrySet())
	  {
	    entry.getValue().setEnabled(setTo);
	  }
	}
    }
}




