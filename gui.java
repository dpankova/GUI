
import javax.swing.*;        
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import java.util.regex.*;
import java.util.concurrent.*;
import java.lang.Thread;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import org.jfree.chart.axis.*;
import org.jfree.data.statistics.HistogramDataset;
import java.text.*;   
import java.sql.*;

public class gui extends JFrame 
{
  //------------------------ GUI components ------------------------
  
  static JMenuBar menubar = new JMenuBar();
  static JMenu menu_f = new JMenu("Firmware");
  static JMenu menu_m = new JMenu("Process");
  static JMenu menu_h = new JMenu("Help");
  static JPanel panel = new JPanel();
  static JPanel panelf = new JPanel();
  static JPanel panelc = new JPanel();
  static JPanel panels = new JPanel();
  static JPanel panelm = new JPanel();
  static JPanel panelg = new JPanel();
  static ChartPanel panel_ch = new ChartPanel(null);
  static ChartPanel panel_h = new ChartPanel(null); //add H
  
  static JMenuItem i_fDestroy = new JMenuItem("Terminate");
  static JMenuItem i_fInfo = new JMenuItem("Info");
  static JMenuItem i_mInterrupt = new JMenuItem("Interrupt");
  static  JMenuItem i_help = new JMenuItem("Help");
  static  JMenuItem i_diagram = new JMenuItem("Diagram");
  static  JMenuItem i_service = new JMenuItem("Service Input");

  
  static JButton b_enter_nios = new JButton("<html><center>Enter NIOS</center></html>");
  static JButton b_load_firm = new JButton("<html><center>Load Firmware</center></html>");  
  static JButton b_sel_file1 = new JButton("<html><center>...</center></html>"); 
  static JButton b_sel_firm = new JButton("<html><center>...</center></html>");  
  static JButton b_sel_shell = new JButton("<html><center>...</center></html>");  
  static JButton b_sel_file2 = new JButton("<html><center>...</center></html>");  
  static JButton b_load_nios = new JButton("<html><center>Load NIOS</center></html>");
  static JButton b_main_tofile = new JButton("<html><center>Save</center></html>");  
  static JButton b_main_clear = new JButton("<html><center>Clear</center></html>");  
  static JButton b_stop = new JButton("<html><center>Stop</center></html>");
  static JButton b_firm_stop= new JButton("<html><center>Interrupt</center></html>");
  static JToggleButton tb_save = new JToggleButton("<html><center>Save Data</center></html>");
  static JButton b_sel_save= new JButton("<html><center>...</center></html>");
  static JButton b_exit = new JButton("<html><center>EXIT</center></html>");
 
  static JLabel l_files = new JLabel("Configuration files:");       
  static JLabel l_main = new JLabel("Command output:");       
  static JLabel l_shell = new JLabel("NIOS shell file:");       
  static JLabel l_firm = new JLabel("Firmware:");           

  static JTextArea ta_file1 = new JTextArea("select file 1");
  static JScrollPane sp_file1 = new JScrollPane(ta_file1);

  static JTextArea ta_file2 = new JTextArea("select file 2");
  static JScrollPane sp_file2 = new JScrollPane(ta_file2);
  
  static JTextArea ta_firm = new JTextArea("select firmware");
  static JScrollPane sp_firm = new JScrollPane(ta_firm);  
  
  static JTextArea ta_shell = new JTextArea("select NIOS");
  static JScrollPane sp_shell = new JScrollPane(ta_shell);  

  static JTextArea ta_save = new JTextArea("select where to save");
  static JScrollPane sp_save = new JScrollPane(ta_save); 
  
  static JTextArea ta_main = new JTextArea();
  static JScrollPane sp_main = new JScrollPane(ta_main);
  
  static Pattern pattern = Pattern.compile("([0-9]{1,10},[ ]{1,10}){5}");
  static Pattern pattern2 = Pattern.compile("start timestamp");
  static Pattern pattern3 = Pattern.compile("isamp   adc   time  tot  eoe");
  
  static BlockingQueue<String> que = new LinkedBlockingQueue<String>();
  static Queue<Long> q_kf = new LinkedList<Long>();
  static Queue<Long> q_km = new LinkedList<Long>();
  static DateFormat df = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss");
  static String home = System.getProperty("user.home");

//-------------------- CONSTANTS -----------------------------------
public static final int N = 256;
// ------------------ Main method ----------------------------------
  public gui()
{
  ta_file1.setEditable(false);
  ta_file2.setEditable(false);
  ta_main.setEditable(false);
  redirectSystemStreams();
  
  //--------------------- UI layout ---------------------------------
  menubar.add(menu_m);
  menubar.add(menu_f);
  menubar.add(menu_h);
  menu_f.add(i_fDestroy);
  menu_f.add(i_fInfo);
  menu_m.add(i_mInterrupt);
  menu_h.add(i_help);
  menu_h.add(i_diagram);
  menu_h.add(i_service); 

  panel.setLayout(new MigLayout()); 
  panelc.setLayout(new MigLayout()); 
  panelf.setLayout(new MigLayout()); 
  panels.setLayout(new MigLayout()); 
  panelm.setLayout(new MigLayout()); 
  panelg.setLayout(new MigLayout()); 
  panel_ch.setLayout(new MigLayout()); 
  panel_ch.setBackground(Color.WHITE);
  panel_h.setLayout(new MigLayout()); //add H
  panel_h.setBackground(Color.WHITE); //add H
  
  panel.add(panels,"cell 0 0, width 200:800:800, height 115:115:115, aligny top, growx");
  panel.add(panelf,"cell 1 0, width 200:800:800, height 115:115:115, aligny top, growx");
  panel.add(panelc,"cell 2 0 2 1, width 200:800:800, height 125:125:125, growx");
  panel.add(panelm,"cell 0 2 3 1, width 620:2000:2000, height 100:2000:2000, grow");
  panel.add(panelg,"cell 0 3 3 1, width 620:2000:2000, height 50:50:50, growx");
  panel.add(panel_ch,"cell 0 4 2 1, width 420:1500:2000, height 100:2000:2000, grow"); //change H
  panel.add(panel_h,"cell 2 4 1 1, width 200:800:2000, height 100:2000:2000, grow"); //add H
   
  
  panels.add(b_enter_nios,"cell 1 0, width 130:130:130, height 25:25:25, align, center, grow 0");
  panels.add(b_sel_shell, "cell 1 1, width 50:50:50, height 25:25:25, align center, grow 0");
  panels.add(sp_shell,"cell 0 1, width 100:800:800, height 35:35:35, growx");
  panels.add(l_shell,"cell 0 0, width 100:130:130, height 10:25:25, grow 0");
  panels.add(b_exit, "cell 0 2 2 1 , width 150:150:150, height 25:25:25,  align, center, grow 0");
  
  panelf.add(b_load_firm, "cell 1 0, width 150:150:150, height 25:25:25, align, center, grow 0");
  panelf.add(b_sel_firm, "cell 1 1, width 50:50:50, height 25:25:25, align center, grow 0");
  panelf.add(sp_firm, "cell 0 1, width 100:800:800, height 35:35:35, growx");
  panelf.add(l_firm, "cell 0 0, width 100:130:130, height 10:25:25, grow 0");
  panelf.add(b_firm_stop, "cell 0 2 2 1, width 150:150:150, height 25:25:25,  align, center, grow 0");
   
  panelc.add(b_sel_file1, "cell 1 1, width 50:50:50, height 25:25:25, align center, grow 0"); 
  panelc.add(b_sel_file2, "cell 1 2, width 50:50:50, height 25:25:25, align center, grow 0");
  panelc.add(b_load_nios, "cell 1 0, width 150:150:150, height 25:25:25, align, center, grow 0");
  panelc.add(sp_file1, "cell 0 1, width 100:800:800, height 35:35:35, growx");
  panelc.add(sp_file2, "cell 0 2, width 100:800:800, height 35:35:35, growx");
  panelc.add(l_files, "cell 0 0, width 100:140:140, height 25:25:25, grow 0");
  
  panelm.add(tb_save, "cell 1 0, width 150:150:150, height 25:25:25, align right, grow 0");
  panelm.add(b_main_tofile, "cell 1 0, width 100:100:100, height 25:25:25, align right, grow 0");
  panelm.add(b_main_clear, "cell 1 0, width 100:100:100, height 25:25:25, align right, grow 0");
  panelm.add(sp_main, "cell 0 1 2 1, width 100:2000:2000, height 100:800:800, aligny top, grow");
  panelm.add(l_main, "cell 0 0, width 150:150:150, height 25:25:25, grow 0");

  panelg.add(tb_save, "cell 2 0, width 150:150:150, height 25:25:25, align right, grow 0");
  panelg.add(sp_save, "cell 0 0 , width 100:1000:1000, height 35:35:35, aligny top, grow");
  panelg.add(b_sel_save, "cell 1 0, width 50:50:50, height 25:25:25, align center, grow 0");
  // panelg.add(panel_ch, "cell 0 1 3 1, width 620:2000:2000, height 100:2000:2000, grow");
  
  panelc.setBorder(BorderFactory.createEtchedBorder());
  panelf.setBorder(BorderFactory.createEtchedBorder());
  panels.setBorder(BorderFactory.createEtchedBorder());
  panelm.setBorder(BorderFactory.createEtchedBorder());
  panelg.setBorder(BorderFactory.createEtchedBorder());
  panel_ch.setBorder(BorderFactory.createEtchedBorder());
  
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setBounds(200,200,1000,800);
  enableEvents( SimpleAWTEvent.EVENT_ID); 
  getContentPane().add(panel, BorderLayout.CENTER);
  setVisible(true);
  setJMenuBar(menubar);
  b_exit.setBackground(Color.orange);
  
  Component[] com = panelf.getComponents();
  for (int a = 0; a < com.length; a++) 
    com[a].setEnabled(false);
  
  menu_f.setEnabled(false);
  b_enter_nios.setEnabled(false);
  menu_m.setEnabled(false);
  tb_save.setEnabled(false);
  i_service.setEnabled(true);
 
  com = panelc.getComponents();
  for (int a = 0; a < com.length; a++) 
    com[a].setEnabled(false);  
  


  try
  {
//------------------------  processes start here  --------------------------------
    String homedir_default = (home +"/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw");
    ChosenFile cdir = DirChooser(panel,"Chose Process Home Directory", homedir_default);
    
    if (cdir.option != JFileChooser.APPROVE_OPTION) 
    {
      TAWrite_EDT(ta_main,"Home directory not choosen");        
      System.exit(0);
    }
    TAWrite_EDT(ta_main,new String("SELECT SHELL FILE")); 
    TAWrite_EDT(ta_main,new String("SELECT SAVE FILE DIRECTORY")); 
    TAWrite_EDT(ta_main,new String("EXIT USING EXIT BUTTON")); 
   
    
    ProcessBuilder processbuilder = new ProcessBuilder("/bin/bash");
    processbuilder.redirectErrorStream(true);
    processbuilder.directory(new File(cdir.path));
    
    
    
    
    
    MyProcess myprocess = new MyProcess(processbuilder);
    Boolean flag = myprocess.setProcess();
     if (!flag)
    {
      TAWrite_EDT(ta_main,new String("First process failed"));
      throw new IOException();
    }
    myprocess.setOutput();
    myprocess.setInput();
      
    ReaderThread reader = new ReaderThread(myprocess.processOutput,this,que);
    WorkerThread worker = new WorkerThread(que,this,panel_ch,panel_h,tb_save,ta_save); //change H
    
    reader.start();
    worker.start();
    
    try
    {
      q_km.add(reader.getId());
      q_km.add(worker.getId());
    }
    catch  (IllegalStateException ex_b1) 
    {
      TAWrite_EDT(ta_main,new String("Load firmware: "+" IllegalStateException was caught: "+ex_b1.getMessage()));
    }
    
       
    MyProcess myprocess_firm = new MyProcess(processbuilder);
    Boolean flagf = myprocess_firm.setProcess();
    if (!flagf)
    {
      TAWrite_EDT(ta_main,new String("Second process failed"));
      throw new IOException();
    }
    myprocess_firm.setOutput();
    myprocess_firm.setInput();
    
      ReaderThread_firm reader_firm = new ReaderThread_firm(myprocess_firm.processOutput,this);
    reader_firm.start();
    
    try
    {
      q_kf.add(reader_firm.getId());
    }
    catch  (IllegalStateException ex_b1) 
    {
      TAWrite_EDT(ta_main,new String("Second process: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
    }
    
    
    
    
    b_enter_nios.addActionListener(new MyActionListener_enter_nios(myprocess,this,q_km,tb_save)); 
    b_load_firm.addActionListener(new MyActionListener_load_firm(myprocess_firm,this,q_kf));  // !!!!!!!!!!!JTAG CABLE SPECIFY
    i_fDestroy.addActionListener(new MyActionListener_destroy_firm(myprocess_firm,q_kf));
    b_load_nios.addActionListener(new MyActionListener_load_nios(myprocess)); 
    i_mInterrupt.addActionListener(new MyActionListener_stop_nios(myprocess,this,q_km)); 
    b_exit.addActionListener(new MyActionListener_exit(myprocess,myprocess_firm,q_km,q_kf)); 

    b_sel_file1.addActionListener(new MyActionListener_file_chooser(b_sel_file1, ta_file1,
    "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw", "Select .elf file","elf files","elf")); 
    b_sel_firm.addActionListener(new MyActionListener_file_chooser(b_sel_firm, ta_firm,
    "/pingu/gen2dom-fw/ddc2_sockit_nios2/quartus_project/output_files/", "Select .sof file","sof files","sof")); 
    b_sel_shell.addActionListener(new MyActionListener_file_chooser_main(b_sel_shell, ta_shell,
    "/altera/13.1/nios2eds/" , "NIOS command shell script","shell files","sh")); 
    b_sel_file2.addActionListener(new MyActionListener_file_chooser(b_sel_file2, ta_file2,
    "/pingu/gen2dom-fw/ddc2_sockit_nios2/qsys_nios2/software/ddc2_nios2_sw", "Select .sh file","sh files","sh")); 
	
    b_firm_stop.addActionListener(new MyActionListener_firm_stop(myprocess_firm, q_kf));
    i_fInfo.addActionListener(new MyActionListener_firm_info(myprocess_firm));
    //------------------------------------------------------------------------------   
   
 //------------------------------------------------------------------------------   
    b_sel_save.addActionListener(new ActionListener()
      {
	public void actionPerformed(ActionEvent e)
	  {
	    String homedir = (home +"/pingu/data/");
	    ChosenFile sdir = DirChooser(panel,"Chose File Save Directory", homedir);
	    
	    if (sdir.option != JFileChooser.APPROVE_OPTION) 
	    {
	      TAWrite_EDT(ta_main,"Save directory not choosen");        
	      tb_save.setEnabled(false);
	      tb_save.setSelected(false);
	    }
	    else
	    {
	      ta_save.setText(sdir.path);
	      tb_save.setSelected(false);
	      tb_save.setEnabled(true);

	    }
	  }
      }); 
    
//---------------------------- save main file  ---------------------------------- HOOOOOOOOOOOOOOOOW file NAMES!!!!!!!!!	      	  
	b_main_tofile.addActionListener(new ActionListener()
	  {
	    public void actionPerformed(ActionEvent e)
	      {
		try 
		{
		  ChosenFile csave = FileSaver(b_main_tofile, "Save the commands into file", new String (home+"/pingu"));
		  File file = new File(new String (csave.path + "/out_commands.txt"));
		  BufferedWriter outfile = new BufferedWriter(new FileWriter(file,true));
		  outfile.write(ta_main.getText());
		  outfile.close();	    
		}
		catch(FileNotFoundException l) 
		{
		  TAWrite_EDT(ta_main, "Command Saver -  File not found");
		}
		catch(NullPointerException j)
		{
		  TAWrite_EDT(ta_main, "Command Saver -  Null pointer");
		}
		catch(IOException k)
		{
		  TAWrite_EDT(ta_main, "Command Saver -  IO exeption");
		}
		TAWrite_EDT(ta_main, "Command Saver -  Saved");
	      }
	  }); 
	
//---------------------------- clear main field  ---------------------------------- 	      	  
	b_main_clear.addActionListener(new ActionListener()
	  {
	    public void actionPerformed(ActionEvent e)
	      {
		ta_main.setText(null);
	      }
	  }); 
  }
  catch (IOException ex) 
  {
    TAWrite_EDT(ta_main,new String("IOException was caught"+ex.getMessage())); 
  }
}
 static public void CreateShowGui()
    {
      
      gui gui_main = new gui();	
      
    }
//---------------------------- CLASSES ----------------------------
  public static class ChosenFile
  {
    String path;
    int option;
    
    public ChosenFile (String fpath)
      {
	path= fpath;
	option = JFileChooser.APPROVE_OPTION;
      }
    public ChosenFile (String fpath, int opt)
      {
	path = fpath;
	option = opt;
      }
  }
  
//---------------------------- METHODS and Classes ----------------------------

  static public class SimpleAWTEvent extends AWTEvent 
  {
    public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;
    private String str;
    
    SimpleAWTEvent( Object target, String str)
      {
	super( target, EVENT_ID);
	this.str = str;
      } 
    
    public String getStr() { return( str ); }
  } 
  
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
   
//---------------------  write to text area -------------------
  public static void TAWrite( java.awt.Component target , String message ) 
    { 
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue(); 
      eventQueue.postEvent( new SimpleAWTEvent(target, new String(message+ "\n") )); 
    }
public static void TAWrite_EDT(JTextArea area , String message ) 
    { 
      area.append(message +"\n");
      area.setCaretPosition(area.getDocument().getLength());
      
    }
//-------------------- File Chooser---------------------------------
  public static ChosenFile FileChooser(Component parent, String choosertitle, String currentdir, String filtername, String filterext)
    {
      JFileChooser chooser = new JFileChooser(); 
      ChosenFile cfile;
      String selection;
      chooser.setCurrentDirectory(new java.io.File(currentdir));
      chooser.setDialogTitle(choosertitle);
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      // chooser.setAcceptAllFileFilterUsed(false); 	   // disable the "All files" option.
      FileNameExtensionFilter filter = new FileNameExtensionFilter(filtername, filterext);
      chooser.setFileFilter(filter); //filter extentions
      int opendialog = chooser.showOpenDialog(parent); 
      if (opendialog == JFileChooser.APPROVE_OPTION) 
      {
        selection = chooser.getSelectedFile().getAbsolutePath();
	cfile = new ChosenFile(selection);
	TAWrite_EDT(ta_main, new String(choosertitle + ": file selected -"+selection)); 
      }
      else
      {
	cfile = new ChosenFile(" ", opendialog);
	TAWrite_EDT(ta_main,new String(choosertitle + ": no file selected")); 
      }
      return cfile;
    }

//-------------------- File Saver --------------------------------- 
  public static ChosenFile FileSaver(Component parent, String choosertitle, String currentdir)
    {
      JFileChooser chooser = new JFileChooser(); 
      ChosenFile cfile;
      String selection;
      chooser.setCurrentDirectory(new java.io.File(currentdir));
      chooser.setDialogTitle(choosertitle);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int opendialog = chooser.showSaveDialog(parent); 
      if (opendialog == JFileChooser.APPROVE_OPTION) 
      {
        selection = chooser.getSelectedFile().getAbsolutePath();
	cfile = new ChosenFile(selection);
	TAWrite_EDT(ta_main, new String(choosertitle + ": file saved -"+selection)); 
      }
      else
      {
	cfile = new ChosenFile(" ", opendialog);
	TAWrite_EDT(ta_main,new String(choosertitle + ": not saved")); 
      }
      return cfile;
    }

//-------------------- Directory Chooser---------------------------------
  public static ChosenFile DirChooser(Component parent, String choosertitle, String homedir)
    {
      JFileChooser chooser = new JFileChooser(); 
      ChosenFile cfile;
      String selection;
      chooser.setCurrentDirectory(new java.io.File(homedir));
      chooser.setDialogTitle(choosertitle);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      // chooser.setAcceptAllFileFilterUsed(false); 	
      int opendialog = chooser.showOpenDialog(parent); 
      if (opendialog == JFileChooser.APPROVE_OPTION) 
      {
        selection = chooser.getSelectedFile().getAbsolutePath();
	cfile = new ChosenFile(new String(selection+"/"));
	TAWrite_EDT(ta_main,new String(choosertitle + ": file selected -"+selection)); 
      }
      else
      {
	cfile = new ChosenFile(" ", opendialog);
	TAWrite_EDT(ta_main,new String(choosertitle + ": no file selected")); 
      }
      return cfile;
    }

//-------------------- Reads Output in a new thread-----------------  
  static  public class ReaderThread extends Thread 
  {    
    private BufferedReader reader = null;
    private final BlockingQueue<String> queue;
    // The component which will receive the event.
    private java.awt.Component  target = null;
    public ReaderThread(BufferedReader reader,java.awt.Component target , BlockingQueue<String> queue) 
      {
	this.reader = reader;
	this.queue = queue;
	this.target = target;
      }
    @Override
    public void run() 
      {
	String line = null;
	try 
	{
	  Thread thisThread = Thread.currentThread();
	  while (!thisThread.isInterrupted()) 
	  {
	    while ((line = reader.readLine()) != null) 
	    {
	      
	      Matcher matcher = pattern.matcher(line);
	      Matcher matcher2 = pattern2.matcher(line);
	      Matcher matcher3 = pattern3.matcher(line);
	      if (matcher.find())
	      {
		queue.put(line);
		continue;
	      }
	      if (matcher2.find())
		queue.put(line);
	      if (matcher3.find())
		continue;
	      TAWrite(target, line);
	    }
	  } 
	}
	catch(IOException exception)   
	{
	  TAWrite(target,new String("Thread error: " + exception.getMessage())); 
	}	
	catch(InterruptedException exception2) 
	{
	  TAWrite(target,new String("Thread error: " + exception2.getMessage())); 
	}
      }    
  } 
// --------------------------- gets data---------------------------  
  static  public class WorkerThread extends Thread //main change H
  {    
    private final BlockingQueue<String> queue;
    private final ChartPanel panel;
    private final ChartPanel panel2;
    private java.awt.Component target = null;
    private final JToggleButton b_save;
    private final JTextArea field;
  
    public WorkerThread(BlockingQueue<String> queue, java.awt.Component target, ChartPanel panel,ChartPanel panel2, JToggleButton b_save, JTextArea field) 
      {
	this.queue = queue;
	this.panel = panel;
	this.panel2 = panel2;
	this.target = target;
	this.b_save = b_save;
	this.field = field;
      }
    
    @Override
    public void run() 
      {
	String line = null;
	String input = null;
	String[] parts2 =null;
	String[] parts =null;
	java.sql.Date date =null;
	Timestamp stamp =null;
	int count =0;
	int pulses = 0;
	Boolean flag = false; //clear data
	WriterThread writer;
	
	DataSet set = new DataSet();

	XYSeries data = new XYSeries("data");
	XYSeriesCollection dataset = new XYSeriesCollection(data);
	final JFreeChart chart = ChartFactory.createScatterPlot(null,null,null,dataset,PlotOrientation.VERTICAL,false,false,false);
	XYPlot plot = (XYPlot) chart.getPlot();
	NumberAxis domain = (NumberAxis) plot.getDomainAxis();
	NumberAxis range = (NumberAxis) plot.getRangeAxis();
	range.setAutoRangeIncludesZero(false);
	domain.setAutoRange(true);
        range.setAutoRange(true);
	XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	renderer.setSeriesLinesVisible(0, true);
	renderer.setUseFillPaint(true);
	plot.setRenderer(renderer);
	plot.setBackgroundPaint(Color.white);
        panel.setChart(chart);
	
//-----------------------------------H		

	double[] values = new double[100];
        HistogramDataset dataset2 = new HistogramDataset();
	final JFreeChart chart2 = ChartFactory.createHistogram(
            null,
            null, 
            null, 
            dataset2,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );

        panel2.setChart(chart2);
	
//----------------------------------H
	try 
	{
	  Thread thisThread = Thread.currentThread();
	  while (!thisThread.isInterrupted()) 
	  {
	    while ((line = queue.take())!=null)
	    {
	      if (flag)
	      {
		flag = false;
		data.clear();
		count = 0;
	      }
	      
	      Matcher matcher2 = pattern2.matcher(line);
	      if (matcher2.find())
	      {
	        parts2 = line.split("[,]{0,1}[ ]{1,10}");
		stamp = new Timestamp(Long.parseLong(parts2[3].trim(),10));
	        date = new java.sql.Date(stamp.getTime());
		set.name = df.format(date);   
		continue;
	      }  

	      parts = line.split(",[ ]{0,10}");	      
	      set.isamp[count] = Integer.parseInt(parts[0].trim());
	      set.adc[count] = Integer.parseInt(parts[1].trim());
	      set.time[count] = Integer.parseInt(parts[2].trim());
	      set.tot[count] = Integer.parseInt(parts[3].trim());
	      set.eoe[count] = Integer.parseInt(parts[4].trim());
	      
	      data.add(set.isamp[count],set.adc[count]);
	      count++;
	      if (count == N)
	      {
	
		for (int i=0; i<N; i++)
		  values[pulses] = values[pulses]+set.adc[i]; 
		values[pulses] =  values[pulses]-(set.adc[0]+set.adc[255])/2;
		TAWrite(target,Double.toString(values[pulses])); 
		//dataset2.fireDatasetChanged();
		if (pulses ==0)
		  dataset2.addSeries("h",values,100);
		pulses++;
		if(b_save.isSelected())
		{
		  writer = new WriterThread(set,target,field);
		  writer.start();
		}
		flag = true;
	      }
	    }
	  } 
	}
	catch(InterruptedException exception) 
	{
	  TAWrite(target,new String("WorkerThread error: " + exception.getMessage())); 
	}	
      }    
  } 
//--------------------writes data into file-----------------  
  static  public class WriterThread extends Thread 
  {    

    private Writer writer = null;
    private DataSet set;
    private JTextArea field;
    private String name =null;
    private java.awt.Component target = null;
    public WriterThread(DataSet set, java.awt.Component target,  JTextArea field) 
      {
	this.set = set;
	this.field = field;
	this.target = target;
      }
    
    @Override
    public void run() 
      {
	
	try 
	{
	  name = new String(field.getText() + set.name + ".txt");
	  TAWrite(target,new String("File saved"+ name)); 
	  writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name), "utf-8"));
	  writer.write(new String ("isamp"+"\t"+ "adc"+"\t"+ "time"+"\t"+ "tot"+"\t"+"eoe"+"\n"));
	  for (int i=0; i<N; i++)
	  {
	    writer.write(new String(set.isamp[i]+"\t"+set.adc[i]+"\t"+set.time[i]+"\t"+set.tot[i]+"\t"+set.eoe[i]+"\n"));
	  }
	} 
	catch (IOException ex) 
	{
	  TAWrite(target,new String("Thread error: " + ex.getMessage())); 
	} 
	finally 
	{
	  try 
	  {
	    writer.close();
	  }
	  catch (Exception ex) 
	  {
	    TAWrite(target,new String("Thread error: " + ex.getMessage())); 
	  }
	}
      } 
  } 
//-------------------- Reads Output in a new thread (for firmware- don't touch it!!!)-----------------  
  static  public class ReaderThread_firm extends Thread 
  {    
    
    private BufferedReader reader = null;
    private java.awt.Component target =null;
    public ReaderThread_firm(BufferedReader reader, java.awt.Component target ) 
      {
	this.reader = reader;
	this.target = target;
      }
 
    @Override
    public void run() 
      {
	Thread thisThread = Thread.currentThread();
	String line = null;
	while (!thisThread.isInterrupted()) {
	  try 
	  {
	    while ((line = reader.readLine()) != null) 
	    {
	      TAWrite(target, line);	    
	    }
	  } 
	  catch(IOException exception) 
	  {
	    TAWrite(target,new String("Thread error: " + exception.getMessage())); 
	  }
	}
      } 
  } 
//--------------------Check process ----------------------------
public static boolean isRunning(Process process) {
    try {
        process.exitValue();
        return false;
    } catch (Exception e) {
        return true;
    }
}
//------------------------------------------------
 public static class DataSet
  {
    public String name = null;
    public int[] isamp = new int[N];
    public int[] adc = new int[N];
    public int[] time = new int[N];
    public int[] tot = new int[N];
    public int[] eoe = new int[N];
    
    public DataSet()
      {
      	Arrays.fill(isamp,0);
	Arrays.fill(adc,0);
	Arrays.fill(time,0);
	Arrays.fill(tot,0);
	Arrays.fill(eoe,0);
      }
  }

//------------------ Custom action Listeners-------------------
private static class MyActionListener_enter_nios implements ActionListener {
  
  private MyProcess myprocess;
  private Queue<Long> queue;
//  private String filedir;
  private JToggleButton b_save;
  private java.awt.Component target = null;
  public MyActionListener_enter_nios(MyProcess myprocess, java.awt.Component target, Queue<Long> queue,JToggleButton b_save ) {
    this.myprocess = myprocess;
    this.target = target;
    this.queue = queue;
//    this.filedir = filedir;
    this.b_save = b_save;
  }
  
  public void actionPerformed(ActionEvent e_b1)
    {
      String filename;
      filename = ta_shell.getText();
      
      ReaderThread reader;
      WorkerThread  worker;
      boolean flag = true;
      
      if (!isRunning(myprocess.process))
      {
	flag = myprocess.setProcess();
	myprocess.setOutput();
	myprocess.setInput();
	
	reader = new ReaderThread(myprocess.processOutput,target,que);
	worker = new WorkerThread(que,target,panel_ch,panel_h,b_save,ta_save);

        reader.start();
        worker.start();
	try
	{
	  queue.add(reader.getId());
	  queue.add(worker.getId());
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("Load firmware: "+" IllegalStateException was caught: "+ex_b1.getMessage()));
	}
	
	try
	{
	  myprocess.processInput.write(filename +" \n");
	  myprocess.processInput.flush(); 
	} 
	catch  (IOException ex_b2) 
	{
	  TAWrite_EDT(ta_main,new String("Enter the NIOS restart: "+"IOException was caught: "+ex_b2.getMessage()));   
	}
	TAWrite_EDT(ta_main,new String("NIOS Shell: Process restarted")); 
      }
      else
      {
	TAWrite_EDT(ta_main,new String("NIOS Shell: Process is ready"));
     	if (flag)
	{  
	  try
	  {
	    myprocess.processInput.write(filename +" \n");
	    myprocess.processInput.flush(); 
	  } 
	  catch  (IOException ex_b2) 
	  {
	    TAWrite_EDT(ta_main,new String("Enter the NIOS: "+"IOException was caught: "+ex_b2.getMessage()));   
	  }
	}
	else 
	  TAWrite_EDT(ta_main,new String("enter NIOS faliure")); 
      }
    }
  
}
//------------------ Custom action Listeners-------------------
private static class MyActionListener_destroy_firm implements ActionListener {
  
  private MyProcess myprocess;
  private Queue<Long> queue;
  public MyActionListener_destroy_firm(MyProcess myprocess, Queue<Long> queue) {
    this.myprocess = myprocess;
    this.queue = queue;
  }
  public void actionPerformed(ActionEvent e_b1)
    {
      long l =0;
      while (!queue.isEmpty()) 
      {
	try
	{
	  l = queue.remove();  
	  InterruptThread(l);
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("ENter NIOS: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	}
      }
      try 
      {  
	myprocess.processInput.write("pkill -9 -f nios2-gdb-server" +" \n");
	myprocess.processInput.flush(); 
	myprocess.process.destroy();
      } 
      catch  (IOException ex_b2) 
      {
	TAWrite_EDT(ta_main,new String("Enter the NIOS: "+"IOException was caught: "+ex_b2.getMessage()));   
      }
 
    }
}
//------------------ -------------------
private static class MyActionListener_exit implements ActionListener {
  
  private MyProcess myprocess;
  private MyProcess myprocess2;
  private Queue<Long> queue;
  private Queue<Long> queue2;
  public MyActionListener_exit(MyProcess myprocess,MyProcess myprocess2, Queue<Long> queue, Queue<Long> queue2) {
    this.myprocess = myprocess;
    this.myprocess2 = myprocess2;
    this.queue = queue;
    this.queue2 = queue2;
  }
  public void actionPerformed(ActionEvent e_b1)
    {
      long l =0;
      while (!queue.isEmpty()) 
      {
	try
	{
	  l = queue.remove();  
	  InterruptThread(l);
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("ENter NIOS: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	}
      }
      while (!queue2.isEmpty()) 
      {
	try
	{
	  l = queue2.remove();  
	  InterruptThread(l);
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("ENter NIOS: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	}
      }
      myprocess.process.destroy();
      try 
      {  
	myprocess2.processInput.write("pkill -9 -f nios2-gdb-server" +" \n");
	myprocess2.processInput.flush(); 
	myprocess2.process.destroy();
      } 
      catch  (IOException ex_b2) 
      {
	TAWrite_EDT(ta_main,new String("Enter the NIOS: "+"IOException was caught: "+ex_b2.getMessage()));   
      }
      System.exit(0);
    }
}
//----------------------------------------------------------------------
  private static class MyActionListener_stop_nios implements ActionListener {
  
  private MyProcess myprocess;
  private Queue<Long> queue;
  private java.awt.Component target = null;
  public MyActionListener_stop_nios(MyProcess myprocess, java.awt.Component target,  Queue<Long> queue ) {
    this.myprocess = myprocess;
    this.target = target;
    this.queue = queue;
  }
  
  public void actionPerformed(ActionEvent e_b1)
    {
      boolean flag = true;
      long l =0;
      if (isRunning(myprocess.process))
      {
	while (!queue.isEmpty()) 
	{
	  try
	  {
	    l = queue.remove();
	    // TAWrite_EDT(ta_main,new String(Long.toString(l)));    
	    InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWrite_EDT(ta_main,new String("ENter NIOS: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	  }
        }
	
	myprocess.process.destroy();
	TAWrite_EDT(ta_main,new String("NIOS stopped"));
      }
      else
      {
	TAWrite_EDT(ta_main,new String("NIOS: Process is dead"));
	while (!queue.isEmpty()) 
	{
	  try
	  {
	    l = queue.remove();
	    //  TAWrite_EDT(ta_main,new String(Long.toString(l)));
	    InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWrite_EDT(ta_main,new String("Load firmware: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	  }
        }
      } 
    }
}
//----------------------------------------------------------------------------
private static class MyActionListener_load_firm implements ActionListener {
  
  private MyProcess myprocess;
  private Queue<Long> queue;
  private java.awt.Component target = null;
  public MyActionListener_load_firm(MyProcess myprocess,java.awt.Component target , Queue<Long> queue) {
    this.myprocess = myprocess;
    this.target =target;
    this.queue = queue;
  }
 
  public void actionPerformed(ActionEvent e_b1)
    {
      String filename, filename_sh;
      filename = ta_firm.getText();
      filename_sh = ta_shell.getText();
      
      ReaderThread_firm reader_firm;
      boolean flag = true;
      
      if (!isRunning(myprocess.process))
      {
	flag = myprocess.setProcess();
	myprocess.setOutput();
	myprocess.setInput();
	
	reader_firm = new ReaderThread_firm(myprocess.processOutput,target);
	reader_firm.start();
	try
	{
	  queue.add(reader_firm.getId());
	}
	catch  (IllegalStateException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("Load firmware: "+" IllegalStateException was caught: "+ex_b1.getMessage()));
	}
	
	try
	{
	  myprocess.processInput.write(filename_sh +" \n");
	  myprocess.processInput.flush();
	  myprocess.processInput.write("nios2-configure-sof "+filename +"\n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("Load firmware: "+"IOException was caught: "+ex_b1.getMessage()));    
	}
      }
      else
      {
	TAWrite_EDT(ta_main,new String("NIOS Shell: Process is ready"));
	try
	{
	  myprocess.processInput.write(filename_sh +" \n");
	  myprocess.processInput.flush();
	  myprocess.processInput.write("nios2-configure-sof "+filename +"\n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("Load firmware: "+"IOException was caught: "+ex_b1.getMessage()));    
	}
      } 
    }
}
  
//----------------------------------------------------------------------------
private static class MyActionListener_firm_stop implements ActionListener {
  
  private MyProcess myprocess;
  private Queue<Long> queue;
  public MyActionListener_firm_stop(MyProcess myprocess, Queue<Long> queue) {
    this.myprocess = myprocess;
    this.queue = queue;
  }
 
  public void actionPerformed(ActionEvent e_b1)
    {
      boolean flag = true;
      long l =0;
      if (isRunning(myprocess.process))
      {
	while (!queue.isEmpty()) 
	{
	  try
	  {
	    l = queue.remove();
	    TAWrite_EDT(ta_main,new String(Long.toString(l)));    
	    InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWrite_EDT(ta_main,new String("Load firmware: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	  }
        }
	
	try
	{
	  myprocess.processInput.write( "q \n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("Stop firmware: "+"IOException was caught: "+ex_b1.getMessage()));    
	}
	
	TAWrite_EDT(ta_main,new String("Firmware stopped"));
      }
      else
      {
	TAWrite_EDT(ta_main,new String(" Firmware: Process is dead"));
	while (!queue.isEmpty()) 
	{
	  try
	  {
	    l = queue.remove();
	    TAWrite_EDT(ta_main,new String(Long.toString(l)));
	    InterruptThread(l);
	  }
	  catch  (IllegalStateException ex_b1) 
	  {
	    TAWrite_EDT(ta_main,new String("Load firmware: "+" IllegalStateException was caught: "+ex_b1.getMessage()));    
	  }
        }
      } 
    }
}
//------------------------------------------------------------------------  
private static class MyActionListener_firm_info implements ActionListener {
  
  private MyProcess myprocess;
  public MyActionListener_firm_info(MyProcess myprocess) {
    this.myprocess = myprocess;
  }
 
  public void actionPerformed(ActionEvent e_b1)
    {
      boolean flag = true;
      long l =0;
      if (isRunning(myprocess.process))
      {
	
	try
	{
	  myprocess.processInput.write( "i \n");
	  myprocess.processInput.flush();
	}
	catch  (IOException ex_b1) 
	{
	  TAWrite_EDT(ta_main,new String("Info firmware: "+"IOException was caught: "+ex_b1.getMessage()));    
	}
	
	TAWrite_EDT(ta_main,new String("Firmware info requested"));
      }
      else
      {
	TAWrite_EDT(ta_main,new String(" Firmware: Process is dead"));
      } 
    }
}
    
//---------------------------------------------------------------------------
private static class MyActionListener_load_nios implements ActionListener {
  
  private MyProcess myprocess;
  public MyActionListener_load_nios(MyProcess myprocess) {
    this.myprocess = myprocess;
  }
  
  public void actionPerformed(ActionEvent e_b2)
    {
      String filename1, filename2;
      filename1 = ta_file1.getText();
      filename2 = ta_file2.getText();
      
      try
      {
	if (isRunning(myprocess.process))
	{
	  myprocess.processInput.write("nios2-download -g "+ filename1 +" && "+ filename2 +" | nios2-terminal \n");
	  myprocess.processInput.flush(); 	  
	}
	else 
	  TAWrite_EDT(ta_main,new String("Loading NIOS Shell: Process is dead")); 
      } 
      catch  (IOException ex_b2) 
      {
	TAWrite_EDT(ta_main,new String("Load the NIOS: "+"IOException was caught: "+ex_b2.getMessage()));   
      }
      
    }
  
}
//-----------------------------------------------------------------------------------------
  private static class MyActionListener_file_chooser implements ActionListener {
    private JTextArea field;
    private JButton button;
    private String dir, name, ext_name, ext;
    public MyActionListener_file_chooser(JButton button, JTextArea field, String dir, String name, String ext_name, String ext) {
      this.button = button;
      this.field = field;
      this.dir = dir;
      this.name =name;
      this.ext_name = ext_name;
      this.ext = ext;
    }
    public void actionPerformed(ActionEvent e_b2)
      {
	String currentdir = (new String(home +dir));
	ChosenFile cfile1 = FileChooser(button,name,currentdir,ext_name,ext);
	if (cfile1.option == JFileChooser.APPROVE_OPTION) 
	{
	  field.setText(cfile1.path);
	}
	else 
	{
	  field.setText("No Selection");
	}       
      }
  }
//-----------------------------------------------------------------------------------------
  private static class MyActionListener_file_chooser_main implements ActionListener {
    private JTextArea field;
    private JButton button;
    private String dir, name, ext_name, ext;
    public MyActionListener_file_chooser_main(JButton button, JTextArea field, String dir, String name, String ext_name, String ext) {
      this.button = button;
      this.field = field;
      this.dir = dir;
      this.name =name;
      this.ext_name = ext_name;
      this.ext = ext;
    }
    public void actionPerformed(ActionEvent e_b2)
      {
	String currentdir = (new String(home +dir));
	ChosenFile cfile1 = FileChooser(button,name,currentdir,ext_name,ext);
	if (cfile1.option == JFileChooser.APPROVE_OPTION) 
	{
	  field.setText(cfile1.path);
	  Component[] com = panelf.getComponents();
	  for (int a = 0; a < com.length; a++) 
	    com[a].setEnabled(true);
	  
	  com = panelc.getComponents();
	  for (int a = 0; a < com.length; a++) 
	    com[a].setEnabled(true);  
	  b_enter_nios.setEnabled(true);
	  menu_f.setEnabled(true);
	  menu_m.setEnabled(true);
	  i_service.setEnabled(true);
	}
	else 
	{
	  field.setText("No Selection");
	  Component[] com = panelf.getComponents();
	  for (int a = 0; a < com.length; a++) 
	    com[a].setEnabled(false);
	  
	  com = panelc.getComponents();
	  for (int a = 0; a < com.length; a++) 
	    com[a].setEnabled(false);  
	  b_enter_nios.setEnabled(false);
	  menu_f.setEnabled(false);
	  menu_m.setEnabled(false);
	  i_service.setEnabled(true);
	}       
      }
  }

//---------------------------- Process class-------------------
  private static class MyProcess
{
  private ProcessBuilder processBuilder;
  public Process process;
  public BufferedReader processOutput;
  public BufferedWriter processInput;
  
  public MyProcess(ProcessBuilder processBuilder) 
    {
      this.processBuilder = processBuilder;
    }
  
  public boolean setProcess()
    {
      boolean flag;
      try
      {
	process = processBuilder.start();
	return flag = true;
      }	 
      catch  (IOException ex_b2) 
      {
	TAWrite_EDT(ta_main,new String("MyProcess NIOS: "+"IOException was caught: "+ex_b2.getMessage()));   
	return flag = false;
      }
    }
  
  public void setOutput()
    {
      processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
  public void setInput()
    {
      processInput = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }  
  
}
//-------------------------------Kill threads---------------------------
static public void InterruptThread(long id_g) {
    Thread currentThread = Thread.currentThread();
    ThreadGroup threadGroup = getRootThreadGroup(currentThread);
    int allActiveThreads = threadGroup.activeCount();
    Thread[] allThreads = new Thread[allActiveThreads];
    threadGroup.enumerate(allThreads);

    for (int i = 0; i < allThreads.length; i++) {
        Thread thread = allThreads[i];
        long id = thread.getId();
	if (id == id_g)
	  thread.interrupt();
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
// ---------------------------Redirect console----------------------------
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
 
     
    
 //------------------- Main method ---------------------------------
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








