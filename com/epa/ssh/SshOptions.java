package com.epa.ssh;

import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;





public class SshOptions extends WindowAdapter implements ActionListener, MouseListener{
	private static SimpleProperties appprop = null;
	boolean multiple;
	
	/****************/
	JTextField host_input;
	JTextField ip_base_input;
	JTextField ip_menor_input;
	JTextField ip_mayor_input;
	JTextField user_input;
	JTextField pass_input;
	JCheckBox checkTo;
	JCheckBox checkFrom;
	JComboBox appType;
	JComboBox dbDriver;
	JTextField file_ori_input;
	JTextField file_dest_input;
	
	JTextField file_sql_input;
	JTextField file_ips_input;
	JTextField file_sh_input;
	
	Vector frames;
	Vector consoles;
	
	JFrame frame;
	
	
	/****************/
	String file_ori;
	String file_dest;
	
	String file_sh;
	String file_ips;
	String file_sql;
	/****************/
	String ip_base;	
	int ip_menor;
	int ip_mayor;
	String user;
	String pass;
	JTextArea command_output;
	
	 
	
	boolean props_ip = false;
	boolean props_sh = false;
	boolean props_sql = false;
	
	
	static final int SH = 3;
	static final int SQL = 4;
	
	static final int SCP_FROM = 2;
	static final int SCP_TO = 1;
	
	
	 //constants for action commandssi
	protected final static String GO = "0";
	protected final static String FILE_CHECK = "1";
	protected final static String APP_TYPE = "2";
	
	protected final static String FILE_IP_PROPERTIES = "ips.properties";
	protected final static String FILE_SH_PROPERTIES = "comandos.properties";
	protected final static String FILE_SQL_PROPERTIES = "sql.properties";
	
	
	/**
	 * 
	 */
	public SshOptions() {
		super();	
		

	}


	private Component createButtonPane() {
		   JButton button = new JButton("Process");
	       button.setActionCommand(GO);
	       button.addActionListener(this);	       

	       //Center the button in a panel with some space around it.
	       JPanel pane = new JPanel(); //use default FlowLayout
	       pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	       pane.add(button);

	       return pane;
	}

	
	private Component createOutputArea() {
		Box box = Box.createVerticalBox();
	    
	    //inputs and buttons
	    JLabel label = new JLabel("Salida");
	   
	    command_output = new JTextArea();
	    command_output.setLineWrap(true);
	    JScrollPane scroll = new JScrollPane(command_output);
	    scroll.setViewportView(command_output);
	    DefaultCaret caret = (DefaultCaret)command_output.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    

	    scroll.setPreferredSize(new Dimension(700,250));
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	    
	    
	    
	   // box.add(command_output);
	    box.add(scroll);
	    box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	    box.setPreferredSize(new Dimension(500,200));
	    return box;
	}


	private Component createOptionControls(int opt) {
					    
			//Add everything to a container.
		    Box box = Box.createVerticalBox();
		    		    
		    //inputs and buttons
		    /**************Datos de conexion******************/
		    JLabel label = new JLabel("Usuario");
		    user_input = new JTextField();
		    //label.setPreferredSize(new Dimension(175, 20));
		    
		    box.add(label);
		    box.add(user_input);
		    
		    label = new JLabel("Contrase�a");
		    pass_input = new JTextField();
		    
		    box.add(label);
		    box.add(pass_input);
		    
		    /*******************IPs***************************/
		    try {
		    	FileReader fr = new FileReader(FILE_IP_PROPERTIES);
				BufferedReader ips_reader = new BufferedReader(fr);					
									
				props_ip = true;
			} catch (FileNotFoundException e) {
				// TODO Bloque catch generado automticamente
				e.printStackTrace();				
			}
		    
		    
		    
		    if(opt==SH){
		    	/*******************SH***************************/	
		    	
		    	
			    			
				  try {
				    	FileReader fr = new FileReader(FILE_SH_PROPERTIES);
						BufferedReader sh_reader = new BufferedReader(fr);					
					
						props_sh = true;
					} catch (FileNotFoundException e) {
						// TODO Bloque catch generado automticamente
						e.printStackTrace();				
					}			
				
			    
			    
			    
			    label = new JLabel("IP Base");
			    ip_base_input = new JTextField();
			    if(props_ip){
			    	ip_base_input.disable();
			    }
			    	    
			    box.add(label);
			    box.add(ip_base_input);
			    
			    label = new JLabel("IP Menor");
			    ip_menor_input = new JTextField();
			    if(props_ip){
			    	ip_menor_input.disable();
			    }
			    
			    box.add(label);
			    box.add(ip_menor_input);
			    
			    label = new JLabel("IP Mayor");
			    ip_mayor_input = new JTextField();
			    if(props_ip){
			    	ip_mayor_input.disable();
			    }
			    
			    box.add(label);
			    box.add(ip_mayor_input);
			    
			    
			    
			    /*********Datos de los archivos*******/
			    label = new JLabel("Archivo Local");
			    file_ori_input = new JTextField();
			    file_ori_input.disable();
			    
			    box.add(label);
			    box.add(file_ori_input);
			    
			    label = new JLabel("Archivo Remoto");
			    file_dest_input = new JTextField();
			    file_dest_input.disable();
			    
			    box.add(label);
			    box.add(file_dest_input);
			    
			    box.add(Box.createVerticalStrut(5)); //spacer
			    
			    /*************Check archivos tipo de Scp FROM o TO*************/
			   // label.setPreferredSize(new Dimension(20, 20));
			   // JPanel pane = new JPanel(); //use default FlowLayout
			    //pane.setLayout(new BorderLayout());
			   // pane.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			    
			    	    
			    
			    checkTo = new JCheckBox("Enviar", false);	
			    checkTo.setActionCommand(FILE_CHECK);
			    checkTo.addActionListener(this);
			    box.add(checkTo);
			    
			    
			    checkFrom = new JCheckBox("Recuperar", false);
			    checkFrom.setActionCommand(FILE_CHECK);
			    checkFrom.addActionListener(this);
			    box.add(checkFrom);
			    
			    /*******Config files**********/
			  
			    label = new JLabel("Archivo de comandos");
			    file_sh_input = new JTextField();
			    if(props_sh){
			    	file_sh_input.setText(FILE_SH_PROPERTIES);
			    }
			    //file_sh_input.disable();
			    
			    box.add(label);
			    box.add(file_sh_input);
			    
			    label = new JLabel("Archivo de ips");
			    file_ips_input = new JTextField();
			    
			    if(props_ip){
			    	file_ips_input.setText(FILE_IP_PROPERTIES);
			    }
			    //file_ips_input.disable();
			    
			    box.add(label);
			    box.add(file_ips_input);
			    
			    file_sh_input.addMouseListener(this);
			    
		    }else{
		    	/*******************SQL***************************/
		    	try {
			    	FileReader fr = new FileReader(FILE_SQL_PROPERTIES);
					BufferedReader sh_reader = new BufferedReader(fr);
					props_sql = true;
				} catch (FileNotFoundException e) {
					// TODO Bloque catch generado automaticamente
					e.printStackTrace();				
				}			
			
				label = new JLabel("Driver BD");
				
				dbDriver = new JComboBox();	
				dbDriver.addItem("mysql");
				dbDriver.addItem("db2");
				
				
				box.add(label);
				box.add(dbDriver);
				
		    
			    /*******Config files**********/
			    		    	
			    label = new JLabel("Archivo de sqls");
			    file_sql_input = new JTextField();			    
			    if(props_sql){
			    	file_sql_input.setText(FILE_SQL_PROPERTIES);
			    }
			    
			  
			    
			    
			    box.add(label);
			    box.add(file_sql_input);
			    
			    label = new JLabel("Archivo de ips");
			    file_ips_input = new JTextField();
			    if(props_ip){
			    	file_ips_input.setText(FILE_IP_PROPERTIES);
			    }
			    //file_ips_input.disable();
			    
			    box.add(label);
			    box.add(file_ips_input);
			    
			    box.add(Box.createVerticalStrut(300));
			    file_sql_input.addMouseListener(this);
			    
		    }
			
			file_ips_input.addMouseListener(this);
			
		    
		    
		    //box.add(box2, 100);
		    
		      
		    /******************Add some breathing room.*****************/
		    box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		    box.setPreferredSize(new Dimension(200,500));
	    	return box;
	}


	public void actionPerformed(ActionEvent e) {
		 String command = e.getActionCommand();

		
	       if (command.equals(GO)) {	    	   
	    	   //setHost(host_input.getText());
	    	   
	    	   if(user_input.getText().length()>0 && pass_input.getText().length()>0  ){
		    	   setUser(user_input.getText());
		    	   setPass(pass_input.getText());
		    	   
		    	   if(appType.getSelectedIndex()==(SH-3) && 
		    			   ( (ip_base_input.getText().length()>0 && ip_menor_input.getText().length()>0 &&  ip_mayor_input.getText().length()>0) || (props_ip) ) ){			    	   			    	   
			    	   if(!props_ip){
				    	   setIp_base(ip_base_input.getText());
				    	   setIp_menor(Integer.valueOf(ip_menor_input.getText()).intValue());
				    	   setIp_mayor(Integer.valueOf(ip_mayor_input.getText()).intValue());
			    	   }else{
			    		   setIp_base("0");
				    	   setIp_menor(0);
				    	   setIp_mayor(0);
			    	   }
			    	   setFile_ori(file_ori_input.getText());
			    	   setFile_dest(file_dest_input.getText());
			    	   
			    	   setFile_ips(file_ips_input.getText());
			    	   setFile_sh(file_sh_input.getText());

			    	   

			    	   			    	   
			    	   if(checkTo.isSelected()){
			    		   SshTestMultiple.startInstructionsThread(getUser(), getPass(), getIp_base(), 
			    				   getIp_menor(), getIp_mayor(), SCP_TO,getFile_ori(), getFile_dest(), getFile_sh(), getFile_ips());
			    	   }else if(checkFrom.isSelected()){
			    		   SshTestMultiple.startInstructionsThread(getUser(), getPass(), getIp_base(),
			    				   getIp_menor(), getIp_mayor(), SCP_FROM ,getFile_ori(), getFile_dest(), getFile_sh(), getFile_ips());
			    	   }else{
			    		   SshTestMultiple.startInstructionsThread(getUser(), getPass(), getIp_base(),
			    				   getIp_menor(), getIp_mayor(), SH ,getFile_ori(), getFile_dest(), getFile_sh(), getFile_ips());
			    	   }
		    	   }else if(appType.getSelectedIndex()==(SQL-3) && 
		    			   ( (file_sql_input.getText().length()>0 ) || (props_ip) )  ){
		    		   
		    		   
		    		   setFile_ips(file_ips_input.getText()); 
			    	   setFile_sql(file_sql_input.getText());
		    		   SshTestMultiple.startSQLThread(getUser(), getPass(), getIp_base(), 
		    				   getIp_menor(), getIp_mayor(), SQL ,getFile_ori(), getFile_dest(),getFile_sql(), getFile_ips(), dbDriver.getSelectedIndex());
		    		   
		    	   }
		    	   
		    	   
					
		    	   
	    	   }
	       }else if(command.equals(FILE_CHECK)){
	    	   if(checkTo.isSelected() || checkFrom.isSelected()){
	    		   file_dest_input.enable();
		    	   file_ori_input.enable();	    		   
	    	   }else{
	    		   file_dest_input.disable();
		    	   file_ori_input.disable();
	    	   }
	    	   
	    	   
	      }else if(command.equals(APP_TYPE)){	    	  
	    	  if(appType.getSelectedIndex()==(SH-3)){
	    		  Container contentPane = frame.getContentPane();
	    		  contentPane.removeAll();
	    		  contentPane.add(createAppTypeControls(SH), BorderLayout.PAGE_START);
		    	  contentPane.add(createOptionControls(SH), BorderLayout.WEST);
		          contentPane.add(createOutputArea(), BorderLayout.EAST);
		          contentPane.add(createButtonPane(), BorderLayout.PAGE_END);
	    	  }else{
	    		  Container contentPane = frame.getContentPane();
	    		  contentPane.removeAll();
	    		  contentPane.add(createAppTypeControls(SQL), BorderLayout.PAGE_START);
	    		  contentPane.add(createOptionControls(SQL), BorderLayout.WEST);
		          contentPane.add(createOutputArea(), BorderLayout.EAST);
	    		  contentPane.add(createButtonPane(), BorderLayout.PAGE_END);
	    	
	    		  
	    	  }
	    	  frame.pack();
	          frame.setLocationRelativeTo(null); //center it
	          frame.setVisible(true);  
	      }
		
	}

	public void newConsole(int cmd){
		if(frames==null){
			frames = new Vector();
		}
		
		if(consoles==null){
			consoles = new Vector();
		}
		
		for(int k=0;k<cmd;k++){
		
			JFrame frame_tmp = new JFrame("JavaSSH"+k);
			frame_tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    //Create and set up the content pane.
	        
		    
		    /*create console*/
		    
		    
		    
		    Box box = Box.createVerticalBox();
		    
		    //inputs and buttons
		    JLabel label = new JLabel("Salida");
		   
		    JTextArea newConsole = new JTextArea();
		    newConsole.setLineWrap(true);
		    JScrollPane scroll = new JScrollPane(newConsole);
		    scroll.setViewportView(newConsole);
		    DefaultCaret caret = (DefaultCaret)newConsole.getCaret();
		    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		    
		    consoles.add(k,newConsole);

		    scroll.setPreferredSize(new Dimension(700,250));
		    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	    
		    
		    
		   // box.add(command_output);
		    box.add(scroll);
		    box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		    box.setPreferredSize(new Dimension(500,200));

	        //	Add components to it.
	        Container contentPane = frame_tmp.getContentPane();
	        
	        contentPane.add(box, BorderLayout.PAGE_START);
	        //	Display the window.
	        frame_tmp.pack();
	        if(frames!=null && frames.size()>1){
	        	frame_tmp.setLocation(((Component) frames.get(k-1)).getX()+((Component) frames.get(k-1)).getWidth(), ((Component) frames.get(k-1)).getY());
	        	
	        }else{
	        	frame_tmp.setLocation(frame.getX()+frame.getWidth(), frame.getY());
	        	
	        }
	        
	         
	        frame_tmp.setVisible(true);
	        
	        frames.add(k,frame_tmp);
			
		}
		       
		
	}



	public void getOptions() {
		frame = new JFrame("JavaSSH");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //Create and set up the content pane.
        

        //	Add components to it.
        Container contentPane = frame.getContentPane();
        contentPane.add(createAppTypeControls(SH), BorderLayout.PAGE_START);
        contentPane.add(createOptionControls(SH), BorderLayout.WEST);
        contentPane.add(createOutputArea(), BorderLayout.EAST);
        contentPane.add(createButtonPane(), BorderLayout.PAGE_END);
        
       
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setVisible(true);       
        
		
	}



	private Component createAppTypeControls(int sh2) {
	
		//Add everything to a container.
	    Box box = Box.createVerticalBox();
		
		appType = new JComboBox();	
		appType.addItem("Comandos");
		appType.addItem("Consultas");
		appType.setSelectedIndex(sh2-3);
		appType.setActionCommand(APP_TYPE);
		appType.addActionListener(this);
		box.add(appType);
		    
		    
	    /******************Add some breathing room.*****************/
	    box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	    box.setPreferredSize(new Dimension(200,50));
    	return box;
	}


	/**
	 * @return el host
	 */
	public String getFile_dest() {
		return file_dest;
	}


	/**
	 * @param host el host a establecer
	 */
	public void setFile_dest(String host) {
		this.file_dest = host;
	}
	
	/**
	 * @return el host
	 */
	public String getFile_ori() {
		return file_ori;
	}


	/**
	 * @param host el host a establecer
	 */
	public void setFile_ori(String host) {
		this.file_ori = host;
	}


	/**
	 * @return el ip_base
	 */
	public String getIp_base() {
		return ip_base;
	}


	/**
	 * @param ip_base el ip_base a establecer
	 */
	public void setIp_base(String ip_base) {
		this.ip_base = ip_base;
	}


	/**
	 * @return el ip_mayor
	 */
	public int getIp_mayor() {
		return ip_mayor;
	}


	/**
	 * @param ip_mayor el ip_mayor a establecer
	 */
	public void setIp_mayor(int ip_mayor) {
		this.ip_mayor = ip_mayor;
	}


	/**
	 * @return el ip_menor
	 */
	public int getIp_menor() {
		return ip_menor;
	}


	/**
	 * @param ip_menor el ip_menor a establecer
	 */
	public void setIp_menor(int ip_menor) {
		this.ip_menor = ip_menor;
	}


	/**
	 * @return el multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}


	/**
	 * @param multiple el multiple a establecer
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}


	/**
	 * @return el pass
	 */
	public String getPass() {
		return pass;
	}


	/**
	 * @param pass el pass a establecer
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}


	/**
	 * @return el user
	 */
	public String getUser() {
		return user;
	}


	/**
	 * @param user el user a establecer
	 */
	public void setUser(String user) {
		this.user = user;
	}


	/**
	 * @return el command_output
	 */
	public JTextArea getCommand_output() {
		return command_output;
	}


	/**
	 * @param command_output el command_output a establecer
	 */
	public void setCommand_output(JTextArea command_output) {
		this.command_output = command_output;
	}


	public JCheckBox getCheckFrom() {
		return checkFrom;
	}


	public void setCheckFrom(JCheckBox checkFrom) {		
		this.checkFrom = checkFrom;
	}


	public JCheckBox getCheckTo() {
		return checkTo;
	}


	public void setCheckTo(JCheckBox checkTo) {		
		this.checkTo = checkTo;
	}


	public String getFile_ips() {
		return file_ips;
	}


	public void setFile_ips(String file_ips) {
		this.file_ips = file_ips;
	}


	public String getFile_sh() {
		return file_sh;
	}


	public void setFile_sh(String file_sh) {
		this.file_sh = file_sh;
	}


	public String getFile_sql() {
		return file_sql;
	}


	public void setFile_sql(String file_sql) {
		this.file_sql = file_sql;
	}


	public void mouseClicked(MouseEvent e) {
		// TODO Apendice de metodo generado automaticamente
		
		
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File("."));
	    int r = chooser.showOpenDialog(new JFrame());
	    if (r == JFileChooser.APPROVE_OPTION) {
	      String name = chooser.getSelectedFile().getAbsolutePath();
	      Sender.log("seleccionado "+name,Sender.LOG);
	      if(e.getComponent().equals(file_sql_input)){
	    	  file_sql_input.setText(name);
	    	  setFile_sql(name);
	      }
	      if(e.getComponent().equals(file_ips_input)){
	    	  file_ips_input.setText(name);
	    	  setFile_ips(name);
	      }
	      if(e.getComponent().equals(file_sh_input)){
	    	  file_sh_input.setText(name);
	    	  setFile_sh(name);
	      }
	    
	    }
		
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Apendice de metodo generado automaticamente
		
	}


	public void mouseExited(MouseEvent e) {
		// TODO Apendice de metodo generado automaticamente
		
	}


	public void mousePressed(MouseEvent e) {
		// TODO Apendice de metodo generado automaticamente
		
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Apendice de metodo generado automaticamente
		
	}


	
	
}
