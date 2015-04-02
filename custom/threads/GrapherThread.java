// MAkes graphs 
package custom.threads;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import org.jfree.chart.axis.*;
import org.jfree.data.statistics.HistogramDataset;

import service.*; 
import custom.threads.*; 

public class GrapherThread extends Thread 
  {    
    public BlockingQueue<DataSet> queue;
    public GraphSaveComp gr_el;
    public ChartPanel panel;
    public java.awt.Component target;
  
    public GrapherThread(GraphSaveComp gr_el,  ChartPanel panel, 
			 java.awt.Component target,  BlockingQueue<DataSet> queue) 
      {
	this.panel = panel;
	this.queue = queue;
	this.target = target;
	this.gr_el = gr_el;
      }
    
    @Override
    public void run() 
      {
	String last =" ";
	//DataSet set; //data queue element
	// ------------------- Create Graph ---------------------------
	XYSeries data = new XYSeries("data");
	XYSeriesCollection dataset = new XYSeriesCollection(data);
	
	final JFreeChart chart = ChartFactory.createScatterPlot(null,null,null,dataset,
								PlotOrientation.VERTICAL,
								false,false,false);
	XYPlot plot = (XYPlot) chart.getPlot();

	NumberAxis range = (NumberAxis) plot.getRangeAxis();
	range.setAutoRangeIncludesZero(false);
	range.setAutoRange(true);
	NumberAxis domain = (NumberAxis) plot.getDomainAxis();
	domain.setAutoRange(true);

	XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	renderer.setSeriesLinesVisible(0, false);//no lines
	renderer.setUseFillPaint(true);//fill shapes with paint
	
	plot.setRenderer(renderer);
	plot.setBackgroundPaint(Color.white);
        
	panel.setChart(chart); //add graph to chart panel
	 
	//--------------------- /Create graph ----------------------------------------
	Thread thisThread = Thread.currentThread();
	while (!thisThread.isInterrupted()) 
	{
	  while(gr_el.tb_save.isSelected()) //if user wants to save data
	  { 
	           while (true) 
		   {	 
		     DataSet set = queue.peek(); //check the data in the queue
		     if (set == null) break; 
		     
		     if (set.graphed || !set.histgraphed) //so we don't use same data more than once
		       continue;//or take data before histogram, if saver is too slow.
		     set = queue.poll(); //remove the data
		     set.graphed = true; //set the flag so it's not used again
		     try
		     {
		       queue.put(set);//put it back to the que for the saver thread to take
		     }	
		     catch(InterruptedException ex1) 
		     {
		       TAWriter.TAWrite(target, new String("GraphThread error: " + ex1.getMessage())); 
		     }
		     
		     data.clear(); // clear previous run
		     for (int i = 0; i<set.getLength(); i++)     
		       data.add(set.isamp[i],set.adc[i]); //add data to chart
		   }
		   if(thisThread.isInterrupted())
		     break;
	  }
	  while(!gr_el.tb_save.isSelected())//if user doesn't want it saved 
	  {
	           while (true) 
		   {	 
		     DataSet set = queue.peek(); //check the data in queue
		     if (set == null) break; 	 //GrapherThread: got it
	
		     if (!set.histgraphed) //so we don't take data before histogram
		       continue;//GrapherThread: NOT my turn 
		     
		     set = queue.poll(); //remove the data - GrapherThread: my turn 
		    
		     data.clear(); // clear previous run
		     for (int i = 0; i<set.getLength(); i++)     
		       data.add(set.isamp[i],set.adc[i]); //add data to chart
		   }
		   if(thisThread.isInterrupted())
		     break;
	  
	  }  
	}
      }    
}


