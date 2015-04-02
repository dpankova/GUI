// MAkes histogram
package custom.threads;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import org.jfree.chart.axis.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.renderer.category.*;

import service.*; 
import custom.threads.*; 

public class HistGrapherThread extends Thread 
  {    
    public BlockingQueue<DataSet> queue;
    public ChartPanel panel;
    public java.awt.Component target;
  
    public HistGrapherThread(ChartPanel panel,
			     java.awt.Component target, BlockingQueue<DataSet> queue) 
      {
	this.panel = panel;
	this.queue = queue;
	this.target = target;
      }
    
    @Override
    public void run() 
      {
	String last =" ";
	//DataSet set; //data queue element
	// ------------------- Create Graph ---------------------------
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Series 1");
	
	final JFreeChart chart = ChartFactory.createHistogram(null,null,null,dataset,
							      PlotOrientation.VERTICAL,
							      false,false,false);
	XYPlot plot = (XYPlot) chart.getPlot();   
        plot.setForegroundAlpha(0.85f); 
	plot.setBackgroundPaint(new Color(0xffb347));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();   
	renderer.setBarPainter(new StandardXYBarPainter()); //removes color gradient from bars
	renderer.setSeriesPaint(0, Color.black);
	renderer.setDrawBarOutline(false); 

	panel.setChart(chart); //add graph to chart panel
	//--------------------- /Create graph ----------------------------------------
	Thread thisThread = Thread.currentThread();
	while (!thisThread.isInterrupted()) 
	{
	           while (true) 
		   {	 
		     DataSet set = queue.peek(); //check the data in the queue
		     if (set == null) break; //  HistGrapherThread got it
		          
		     if (set.histgraphed) //check the flag so we don't use same data more than once
		       continue;//if saver is too slow. i.e HistGrapherThread: NOT MY TURN 
		
		     set = queue.poll();//remove the data, i.e HistGrapherThread: MY TURN
		     if (set == null) 
		     {
		       TAWriter.TAWrite(target, new String("HistGraphThread error: queue changed too fast or unpredictably "));
		       break;   
		     }
		     int length = set.getLength();		     
		     set.histgraphed = true; //set the flag so it's not used again
		     try
		     {
		       queue.put(set);//put it back to the que for grapher to take
		     }	
		     catch(InterruptedException ex1) 
		     {
		       TAWriter.TAWrite(target, new String("HistThread error: " + ex1.getMessage())); 
		     }
		 
		     int value = 0;
		     for (int i=0; i<length; i++)
		       value = value+set.adc[i]; 
		     value = value-(set.adc[0]+set.adc[length-1])/2;
		     try
		     {
		       dataset.addObservation(value);//try to add value to the hist
		     }
		     catch(RuntimeException ex) //if failed because there is no bin
		     {//make new bin
		       SimpleHistogramBin bin = new SimpleHistogramBin(value, value+1, true, false);
		       try
		       { 
			 dataset.addBin(bin);//add new bin
		       }
		       catch(RuntimeException ex2)
		       { 
			 TAWriter.TAWrite(target,new String("HistGrapherThread error: " + ex2.getMessage()));
		       }
		       dataset.addObservation(value);//try to add data again
		      
		     }
		   }
		   if(thisThread.isInterrupted())
		     break;
	} 
      }
}    



