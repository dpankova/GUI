//Custom action listener for b_sel_shell button (select nios shell, top left panel)
package service;

import service.*;

import java.lang.*;

public class ThreadService
{
  static public void InterruptThread(long id_g) {
    Thread currentThread = Thread.currentThread();
    ThreadGroup threadGroup = getRootThreadGroup(currentThread);
    int allActiveThreads = threadGroup.activeCount();
    Thread[] allThreads = new Thread[allActiveThreads];
    threadGroup.enumerate(allThreads);
    
    for (int i = 0; i < allThreads.length; i++) 
    {
      if( allThreads[i] != null) //Thread interrupted previously
      { // might die after it was gathered by this call to getRootThreadGroup
	Thread thread = allThreads[i];
	long id = thread.getId();
	if (id == id_g)
	{
	  thread.interrupt();
	}
      }
    }
  }
  
  private static ThreadGroup getRootThreadGroup(Thread thread) {
    ThreadGroup rootGroup = thread.getThreadGroup();
    while (true) {
      ThreadGroup parentGroup = rootGroup.getParent();
        if (parentGroup == null) {
	  break;
        }
        rootGroup = parentGroup;
    }
    return rootGroup;
  }
}
