//Class for reacting to a boolean value change 
// thread safe
package service;

import java.util.*;
import java.beans.*;

public class FlagWatcher {
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private Boolean value = true;

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    this.pcs.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.pcs.removePropertyChangeListener(listener);
  }
  
  public Boolean getValue() {
    return this.value;
  }
  
  public void setValue(Boolean value) {
        Boolean oldvalue = this.value;
        this.value = value;
        this.pcs.firePropertyChange("FlagWatcher",oldvalue, value);
  }
  
}



