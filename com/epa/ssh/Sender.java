package com.epa.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class Sender extends Thread {
		static SimpleProperties ip_props = new SimpleProperties();
		private static boolean props = false;
		private static SimpleLog log = SimpleLog.getInstance(Sender.class);
	 	private static BufferedReader command_reader;	 	
	 	Vector ips;		
		static int tipo;
		static String user;
		static String pass;
		static String remote_file;
		static String local_file;
		static String command_file;
		static String ips_file;
		static String sql_file;
		private String message;
		private int driver;
		JTextArea command_output;
		static int LOG = 0;
		static int LOG_AND_CONSOLE = 1;
		
		
		private final static int MYSQL = 0;
		private final static int AS400 = 1;
		
		
		
		
		public Sender(String message, JTextArea textarea) {
			this.message = message;
			command_output = textarea;	
		}
		
		public Sender(String user, String pass, Vector ips, int tipo, String file_ori, String file_dest, JTextArea command_output2, String command_file, String ips_file) {
			
			
			
			Sender.user = user;
			Sender.pass = pass;			
			this.ips = ips;			
			Sender.remote_file = file_dest.trim();
			Sender.local_file = file_ori.trim();
			Sender.tipo = tipo;
			this.command_output = command_output2;
			Sender.ips_file = ips_file;
			Sender.command_file = command_file;
			
		}
		
		public Sender(String user, String pass, Vector ips, int tipo, String file_ori, String file_dest, JTextArea command_output2, String sql_file, int driver) {
			
			
			
			Sender.user = user;
			Sender.pass = pass;			
			this.ips= ips;			
			Sender.remote_file = "";
			Sender.local_file = "";
			Sender.tipo = tipo;
			this.command_output = command_output2;
			Sender.sql_file = sql_file;
			this.driver = driver;
			
			
		}

		public void run() {
				if(Sender.sql_file!=null){
					RunQuerys();
				}else{
					RunInstructions();
				}
			}
		 
		public void logg(String text, int where){
			//System.out.println(text);
			String[] lines = text.split("\n");
			for(int i = 0; i<lines.length; i++){
				Sender.log.info(lines[i]);
			}			
			text = text.replaceAll("\n", " ");				
			System.out.println(text);
			if(where == LOG_AND_CONSOLE){
			 	if(command_output!=null){
			 		command_output.append("\n"+text);
			 	}
			}
		}
		
		public static void log(String text, int where){
			
		 	//System.out.println(text);
			String[] lines = text.split("\n");
			for(int i = 0; i<lines.length; i++){
				Sender.log.info(lines[i]);
			}			
			text = text.replaceAll("\n", " ");				
			System.out.println(text);
			
		}
		
		
		
		public void RunQuerys() {
		 	
		 	 
		      if(command_output!=null){
		    	  command_output.setText("");
		      }
	      
		      int N = 0;
		      int ip_actual = 0;
		      
		     	    	 
	    	  ip_actual = 0;
	    	  N = ips.size();
		     
		      
		      logg("Hay "+N+" Equipos en cola", LOG_AND_CONSOLE);
		      
		      
		      
		      
		      for(int k = 0; k<N;k++){
		    	  
		    	  try{
		    		  
		    	  boolean connected = true;
		    	  String ip = (String) ips.get(k);			    		  
		    	  
		    	  
		    	  logg("============",LOG_AND_CONSOLE);
		    	  logg("$Conectando a "+ip,LOG_AND_CONSOLE);
		    	  
		    	  /*conectar por jdbc*/
		    	  String driver_class = "";
		    	  Connection con;
		    	  switch (driver) {
			    	
					case AS400:
						driver_class = "com.ibm.as400.access.AS400JDBCDriver";
						Class.forName(driver_class);
			    	    con = DriverManager.getConnection("jdbc:as400://"+ip+"/", user, pass);
						break;
					case MYSQL:
			    	default:
			    		driver_class = "com.mysql.jdbc.Driver";
			    		Class.forName(driver_class);
			    	    con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/", user, pass);
						break;
	
					
					}
		    	  	
	
		    	  
		    	  
		    	  /****************/
			      if(connected){
				      logg("Conexion establecida", LOG_AND_CONSOLE);
				      sendQuerys(con);
			      }
			      
		    	  }catch(Exception e){
				      System.out.println(e);
				      logg("Ha ocurrido un error", LOG_AND_CONSOLE);
				  }
			      
			      ip_actual++;
		    	  
		      }
		    logg("========Done===========",LOG_AND_CONSOLE); 
		    
		  
	            
	    }
		
		 public void sendQuerys(Connection con) throws  IOException, SQLException{
 			 
			 
			 Statement st = con.createStatement();
			/*enviar consultas*/
			 
		 	    	    
    	    String command = "";
			logg("enviando queries ", LOG_AND_CONSOLE);
			 BufferedReader sql_reader = new BufferedReader(new FileReader(sql_file));
			 String command_line = "";
			 command=sql_reader.readLine();			 
			 while(command!=null){			
				 st.addBatch(command);
				 command_line = command_line+";"+command;
				 command=sql_reader.readLine();				 
			 }
			 command = command_line;
			 logg("$"+command, LOG_AND_CONSOLE);
			 
			 
			 
    	    int[] val = st.executeBatch();
    	    for(int k=0;k<val.length;k++)
    	    	logg(val[k] +" row affected",LOG_AND_CONSOLE); 
		
		 }
		
		
		public void RunInstructions() {
			 	
			 	 // JSch.setLogger(new Logger());
			      JSch jsch=new JSch();

			      try {
					jsch.setKnownHosts(System.getProperty("user.home")+".ssh/known_hosts");
				} catch (JSchException e1) {
					// TODO Bloque catch generado automticamente
					e1.printStackTrace();
				}
			      if(command_output!=null){
			    	  command_output.setText("");
			      }
		      
			      int N = 0;
			      int ip_actual = 0;
			      
			     
		    	 
		    	  ip_actual = 0;
		    	  N = ips.size();
			    
			      
			      logg("Hay "+N+" Equipos en cola", LOG_AND_CONSOLE);
			      
			      
			      
			      
			      for(int k = 0; k<N;k++){		
			    	  
			    	  try{
			    	  
				    	  boolean connected = true;
				    	  String ip = (String) ips.get(k);			    		  
				    	  
				    	  
				    	  logg("============",LOG_AND_CONSOLE);
				    	  logg("$Conectando a "+ip,LOG_AND_CONSOLE);
				    	  Session session=jsch.getSession(user, ip, 22);
	
					      //session.setPassword(pass);
					      
	
					      // username and password will be given via UserInfo interface.
				    	  UIInterface bridge = new UIInterface();
				    	  
				    	  bridge.setPasswd(pass);
				    	  
					      UserInfo ui= bridge;
					      session.setUserInfo(ui);
					      
					      session.setConfig("StrictHostKeyChecking", "no");
					      
					      //Compression
					      session.setConfig("compression.s2c", "zlib,none");
					      session.setConfig("compression.c2s", "zlib,none");
					      session.setConfig("compression_level", "9");
					      
					      //ciphers				      
					      /*session.setConfig("cipher.s2c", "blowfish-cbc,aes128-cbc,3des-cbc");
					      session.setConfig("cipher.c2s", "blowfish-cbc,aes128-cbc,3des-cbc");
					      session.setConfig("CheckCiphers", "blowfish-cbc");*/
					      
					     // session.setConfig("cipher.s2c", "none,blowfish-cbc,aes128-cbc,3des-cbc");
					      //session.setConfig("cipher.c2s", "none,blowfish-cbc,aes128-cbc,3des-cbc");
					      
	
					      //session.connect();
					      try{
					    	  session.connect(20000);   // making a connection with timeout.
					    	  //none cipher					      					      				      
						    //  session.rekey();					     
					    	  connected = true;
					      }catch (com.jcraft.jsch.JSchException e) {				    	  
							logg("problemas de conexion", LOG_AND_CONSOLE);
							connected = false;
						}
	
					     // Channel channel=session.openChannel("shell");
					     // channel.setInputStream(System.in);
					      /*
					      // a hack for MS-DOS prompt on Windows.
					      channel.setInputStream(new FilterInputStream(System.in){
					          public int read(byte[] b, int off, int len)throws IOException{
					            return in.read(b, off, (len>1024?1024:len));
					          }
					        });
					       */
	
					      //channel.setOutputStream(System.out);
	
	
					      //channel.connect();
					      //channel.connect(3*1000);
					      if(connected){
						      logg("Conexion establecida", LOG_AND_CONSOLE);
						      sendCommands(session);
					      }
					      
			    	  }catch(Exception e){
					      e.printStackTrace();
					      System.out.println(e);
					      logg("Ha ocurrido un error", LOG_AND_CONSOLE);
					    }
				      
				      ip_actual++;
			    	  
			      }
			    logg("========Done===========",LOG_AND_CONSOLE); 
			   
			  
		            
		    }
		
		 public void sendCommands(Session session) throws JSchException, IOException{
			 			 
			 session.setTimeout(50000);
			 
			 switch (tipo) {
			case SshOptions.SH:
					ssh(session);				
				break;
			case SshOptions.SCP_TO:
					scpto(session);				 
				break;
			case SshOptions.SCP_FROM:
					scpfrom(session);			
				break;	
				
			default:
				logg("Falla General opcion desconocida", LOG);
				System.exit(0);
				break;
			}
			 
		
		 }
		 
		 
		 int checkAck(InputStream in) throws IOException{
			    int b=in.read();
			    // b may be 0 for success,
			    //          1 for error,
			    //          2 for fatal error,
			    //          -1
			    if(b==0) return b;
			    if(b==-1) return b;

			    if(b==1 || b==2){
			      StringBuffer sb=new StringBuffer();
			      int c;
			      do {
				c=in.read();
				sb.append((char)c);
			      }
			      while(c!='\n');
			      if(b==1){ // error
			    	 logg(sb.toString(), LOG_AND_CONSOLE);
			      }
			      if(b==2){ // fatal error
			    	  logg(sb.toString(),LOG_AND_CONSOLE);
			      }
			    }
			    return b;
			  }
		 
		 public  void ssh(Session session) throws IOException, JSchException{
			 String command = "";
			 logg("enviando comandos del sh", LOG_AND_CONSOLE);
			 command_reader = new BufferedReader(new FileReader(command_file));
			 String command_line = "";
			 command=command_reader.readLine();
			 command_line = command;
			 while(command!=null){				 
				 command=command_reader.readLine();
				 if(command!=null){
					 command_line = command_line+";"+command;
				 }
			 }
			 command = command_line;
			 logg("$"+command, LOG_AND_CONSOLE);
			 
			 if(command!=null){
				 Channel channel=session.openChannel("exec");
				 ((ChannelExec)channel).setCommand(command);
					
					// X Forwarding
					// channel.setXForwarding(true);
					
					//channel.setInputStream(System.in);
					channel.setInputStream(null);
					
					channel.setOutputStream(System.out);
					
					//FileOutputStream fos=new FileOutputStream("/opt/logs/javassh.logg");
					//((ChannelExec)channel).setErrStream(fos);
					((ChannelExec)channel).setErrStream(System.err);
					
					InputStream in=channel.getInputStream();
					
					channel.connect();
					
					byte[] tmp=new byte[1024];
					
					while(true){
						while(in.available()>0){				
							int i=in.read(tmp, 0, 1024);
						
							if(i<0)break;
								logg(new String(tmp, 0, i), LOG_AND_CONSOLE);
							}
						
							if(channel.isClosed()){
								logg("exit-status: "+channel.getExitStatus(),LOG_AND_CONSOLE);
								break;
							}
						try{Thread.sleep(1000);}catch(Exception ee){}
					}
				 
				
					logg("comandos enviados",LOG_AND_CONSOLE);
					
					channel.disconnect();
					session.disconnect();
			 }else{
				 logg("Falla leyendo los comandos",LOG_AND_CONSOLE);
			 }
		 }
		 
		 public  void scpto(Session session) throws IOException, JSchException{
			 String command = "";
			 logg("enviando archivo", LOG_AND_CONSOLE);
			  FileInputStream fis=null;
			  command="scp -p -t "+remote_file;
			  
			  
			  Channel channel=session.openChannel("exec");
		      ((ChannelExec)channel).setCommand(command);

		      // get I/O streams for remote scp
		      OutputStream out=channel.getOutputStream();
		      InputStream in=channel.getInputStream();

		      channel.connect();
			  
			  if(checkAck(in)!=0){
					System.exit(0);
				      }

				      // send "C0644 filesize filename", where filename should not include '/'
			  		  File localFile = new File(local_file);
				      long filesize=localFile.length();
				      
				      command="C0644 "+filesize+" ";
				      if(local_file.lastIndexOf('/')>0){
				        command+=local_file.substring(local_file.lastIndexOf('/')+1);
				      }
				      else{
				        command+=local_file;
				      }
				      command+="\n";
				      out.write(command.getBytes()); out.flush();
				      if(checkAck(in)!=0){
				    	  System.exit(0);
				      }

				      logg("enviando contenido de archivo"+ localFile.getName(), LOG);
				      // send a content of lfile
				      fis=new FileInputStream(local_file);
				      byte[] buf=new byte[1024];
				      while(true){
				        int len=fis.read(buf, 0, buf.length);					        
				        if(len<=0) break;
				        	out.write(buf, 0, len); //out.flush();
				      }
				      fis.close();
				      fis=null;
				      // send '\0'
				      buf[0]=0; out.write(buf, 0, 1); out.flush();
				      if(checkAck(in)!=0){
				    	  System.exit(0);
				      }
				      out.close();
				      logg("archivo "+ localFile.getName()+" enviado",LOG_AND_CONSOLE);

				      channel.disconnect();
				      session.disconnect();

		 } 
		 
		 public  void scpfrom(Session session) throws IOException, JSchException{
			 logg("recuperando archivo", LOG_AND_CONSOLE);
			 String command = "";
			 command="scp -f "+remote_file;
		      Channel channel=session.openChannel("exec");
		      ((ChannelExec)channel).setCommand(command);
		      FileOutputStream fos=null;
		      
		      String prefix=null;
		      if(new File(local_file).isDirectory()){
		        prefix=local_file+File.separator;
		      }

		      
		      
		      // get I/O streams for remote scp
		      OutputStream out=channel.getOutputStream();
		      InputStream in=channel.getInputStream();

		      channel.connect();

		      byte[] buf=new byte[1024];

		      // send '\0'
		      buf[0]=0; out.write(buf, 0, 1); out.flush();

		      while(true){
				 int c=checkAck(in);
		   		 if(c!='C'){
			 		break;
				 }

		        // read '0644 '
		        in.read(buf, 0, 5);

		        long filesize=0L;
		        while(true){
		          if(in.read(buf, 0, 1)<0){
		            // error
		            break; 
		          }
		          if(buf[0]==' ')break;
		          filesize=filesize*10L+(long)(buf[0]-'0');
		        }

		        String file=null;
		        for(int i=0;;i++){
		          in.read(buf, i, 1);
		          if(buf[i]==(byte)0x0a){
		            file=new String(buf, 0, i);
		            break;
		          }
		        }

			//System.out.println("filesize="+filesize+", file="+file);

		        // send '\0'
		        buf[0]=0; out.write(buf, 0, 1); out.flush();

		        // read a content of lfile
		        fos=new FileOutputStream(prefix==null ? channel.getSession().getHost()+"_"+local_file : prefix+file);
		        int foo;
		        while(true){
		          if(buf.length<filesize) foo=buf.length;
		          else foo=(int)filesize;
		          foo=in.read(buf, 0, foo);
		          if(foo<0){
		            // error 
		            break;
		          }
		          fos.write(buf, 0, foo);
		          filesize-=foo;
		          if(filesize==0L) break;
		        }
		        fos.close();
		        fos=null;

			if(checkAck(in)!=0){
				System.exit(0);
			}

		        // send '\0'
		        buf[0]=0; out.write(buf, 0, 1); out.flush();
		      }

		      session.disconnect();

			 
		 } 
}

