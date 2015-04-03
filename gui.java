
import javax.swing.*;        
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartPanel;
import java.util.concurrent.*;
import java.lang.Thread;
import java.util.*;
import java.util.concurrent.atomic.*;



import service.*;
import custom.filechooser.*;
import custom.listeners.*;
import custom.threads.*;
import main.*;

public class gui extends JFrame 
{
  //------------------------ GUI components ------------------------
  
  static JMenuBar menubar = new JMenuBar();

  static JMenu menu_m = new JMenu("Process");
  static JMenu menu_f = new JMenu("Firmware");
  static JMenu menu_h = new JMenu("Help");

  static JMenuItem i_mRestart = new JMenuItem("Restart");
  static JMenuItem i_fRestart = new JMenuItem("Restart");

  static JPanel panel = new JPanel();
  static JButton b_exit = new JButton("<html><center>EXIT</center></html>"); 
  static JTextArea ta_shell = new JTextArea("select NIOS");
  static JTextArea ta_main = new JTextArea();
  
  static ChartPanel panel_ch = new ChartPanel(null);
  static ChartPanel panel_h = new ChartPanel(null); //add H
   
  static BlockingQueue<DataSet> que = new LinkedBlockingQueue<DataSet>();
 
// ------------------ Main constructor ----------------------------------
  public gui()
    {
      
      redirectSystemStreams();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(200,200,1000,800);
      enableEvents(SimpleAWTEvent.EVENT_ID); 
      b_exit.setBackground(Color.orange);
      
      //--------------------- UI layout ---------------------------------
      menubar.add(menu_m);
      menubar.add(menu_f);
      menubar.add(menu_h);
      
      menu_m.add(i_mRestart);
      menu_f.add(i_fRestart);
      
      panel.setLayout(new MigLayout()); 
      
      panel_ch.setLayout(new MigLayout()); 
      panel_ch.setBackground(Color.WHITE);
      panel_ch.setBorder(BorderFactory.createEtchedBorder());
      
      panel_h.setLayout(new MigLayout()); //add H
      panel_h.setBackground(Color.WHITE); //add H
      panel_h.setBorder(BorderFactory.createEtchedBorder());
      
      
//------------------------  processes start here  --------------------------------
      
      ChooserDataSet set_home = new ChooserDataSet(panel, ta_main, 
      "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw/", 
						   "Chose Process Home Directory"); 	
      
      ChooseFile cdir = new ChooseFile(set_home); 
      cdir.DirChooser();
      if (cdir.getOption() != JFileChooser.APPROVE_OPTION) 
      {
	TAWriter.TAWrite_EDT(ta_main,"Home directory not choosen");        
	System.exit(0);
      }
      
      //create graph panel.it's created separatly since some of it elements 
      // need to be passed to the threads
      GraphSavePanel panel_grsave = new GraphSavePanel(ta_main);
      GraphSaveComp grsvcomp  = new GraphSaveComp(panel_grsave.getSelectButton(), 
						  panel_grsave.getSaveButton(),
						  panel_grsave.getTextArea());

      
      ProcessBuilder processbuilder = new ProcessBuilder("/bin/bash");
      processbuilder.redirectErrorStream(true);
      processbuilder.directory(new File(cdir.getPath()));
    
      cdir = null;//clean up
      //main process
      MyProcess myprocess = new MyProcess(processbuilder,ta_main);
      
      Boolean flag = myprocess.setProcess();
      if (!flag)
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Nios shell: intial process failed"));
      }
      else
      {
	myprocess.setOutput();
	myprocess.setInput();
	ThreadStart reader = new ThreadStart(myprocess,this,que); //start reader thread
	ThreadStart hist_grapher = new ThreadStart(myprocess,panel_h,this,que);
	ThreadStart grapher = new ThreadStart(myprocess,grsvcomp,panel_ch,this,que);
	ThreadStart saver = new ThreadStart(myprocess,grsvcomp,this,que);
      }   
      
      //firmware process
      MyProcess myprocess_firm = new MyProcess(processbuilder,ta_main);
      flag = myprocess_firm.setProcess();
      if (!flag)
      {
	TAWriter.TAWrite_EDT(ta_main,new String("Firmware: initial process failed"));
      }
      else
      {
	myprocess_firm.setOutput();
	myprocess_firm.setInput();
	
	ThreadStart reader_firm = new ThreadStart(myprocess_firm,this);  
      }   
      //make a map of componenets to be disabled until file is chosen
      Map<Integer, Component> mapDisableMain = new HashMap<Integer, Component> (22);
      //create panels
      MainTextPanel panel_main = new MainTextPanel(ta_main);
      LoadNiosPanel panel_loadN = new LoadNiosPanel(ta_main, myprocess);
      FirmwarePanel panel_firm = new FirmwarePanel(this, myprocess_firm, menu_f, ta_shell , ta_main);
      ServicePanel panel_ser = new ServicePanel(this, myprocess, myprocess_firm, 
						menu_h, mapDisableMain);
      
      //add components to the map for disabling
      Component[] com = panel_firm.getComponents();
      int comlen = com.length;
      
      for (int a = 0; a < comlen; a++) 
	mapDisableMain.put(a,com[a]);
      
      com = panel_loadN.getComponents();
      for (int a = 0; a < com.length; a++) 
	mapDisableMain.put(com.length+a,com[a]);

      comlen = comlen + com.length;
      mapDisableMain.put(comlen,menu_f);
      mapDisableMain.put(comlen+1,menu_m);
      //the last panel, since we need to pass it a map what is already filled with elements
      EnterNiosPanel panel_enterN= new EnterNiosPanel(this, myprocess, menu_m, ta_shell, 
						      b_exit, mapDisableMain, ta_main);
     
      //place panels onto the frame
      panel.add(panel_main,"cell 0 2 3 1, width 620:2000:2000, height 100:2000:2000, grow");
      panel.add(panel_loadN,"cell 2 0 2 1, width 200:800:800, height 125:125:125, growx");
      panel.add(panel_enterN,"cell 0 0, width 200:800:800, height 115:115:115, aligny top, growx");
      panel.add(panel_firm,"cell 1 0, width 200:800:800, height 115:115:115, aligny top, growx");
      panel.add(panel_grsave,"cell 0 3 3 1, width 620:2000:2000, height 50:50:50, growx");
      
      panel.add(panel_ch,"cell 0 4 2 1, width 420:1500:2000, height 100:2000:2000, grow"); //change H
      panel.add(panel_h,"cell 2 4 1 1, width 200:800:2000, height 100:2000:2000, grow"); //add H
    
      getContentPane().add(panel, BorderLayout.CENTER);
      setJMenuBar(menubar);
      setVisible(true);
      //some actions what cannot be placed into one panel
      b_exit.addActionListener(new MyActionListener_exit(myprocess, myprocess_firm, ta_main)); 
      i_fRestart.addActionListener(new MyActionListener_restart_firm(processbuilder, myprocess_firm, ta_main, this));
      i_mRestart.addActionListener(new MyActionListener_restart_nios(processbuilder, myprocess, grsvcomp, 
								   panel_ch, ta_main, this, que));
 
    }
 
//---------------------------- METHODS  ---------------------------- 
//------------------- Overvriting Window.processEvent ------------ 
//to make thread safe way to write into ta_main
  protected void processEvent(AWTEvent event)
    {
      if ( event instanceof SimpleAWTEvent )
      {
	SimpleAWTEvent ev = (SimpleAWTEvent) event;
	ta_main.append(ev.getStr());         // access GUI component
      }
      else // other events go to the system default process event handler
      {
	super.processEvent(event);
      }
    } 
// ----------- Redirect console text to main text area (ta_main) -------
  private static void updateTextArea(final String text) {
    SwingUtilities.invokeLater(new Runnable() {
	public void run() {
	  ta_main.append(text);
	}
      });
  }
  
  private static void redirectSystemStreams() {
    OutputStream out = new OutputStream() {
	@Override
	public void write(int b) throws IOException {
	  updateTextArea(String.valueOf((char) b));
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
	  updateTextArea(new String(b, off, len));
	}
	
	@Override
	public void write(byte[] b) throws IOException {
	  write(b, 0, b.length);
	}
      };
    
    System.setOut(new PrintStream(out, true));
    System.setErr(new PrintStream(out, true));
  }
//------------------- Main Method ------------------------------
  static public void CreateShowGui()
    {      
      gui gui_main = new gui();	 
    }
//------------------- Main method ------------------------------
  public static  void main(String[] args) 
    {
      //Schedule a job for the event-dispatching thread:
      //creating and showing this application's GUI.
      javax.swing.SwingUtilities.invokeLater(new Runnable() 
	{
	  public void run() 
	    {
	      CreateShowGui();
	    }
	});
    } 
}








