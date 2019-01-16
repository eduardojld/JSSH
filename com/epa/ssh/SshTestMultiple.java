package com.epa.ssh;


import com.jcraft.jsch.*;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.*;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import sun.net.ftp.FtpClient;


public class SshTestMultiple{	
		 static SshOptions options = null;	 
		 static String user = null;
		 static String pass = null;		
		 static String tipo= null;
		 static String file_ori= "";
		 static String file_dest= "";
		 static String file_ip= null;
		 static String file_sh= null;
		 static String file_sql= null;
		 static String ip_base= null;
		 static String menor= "0";
		 static String mayor= "0";
		 static int  N = 0;
		 static String  driver = "0";
		 
		 static Vector ips = new Vector();
			 

	public static void getIps(){
		BufferedReader ips_reader = null;
		boolean props=false;
		try {
				Sender.log("leyendo "+file_ip, Sender.LOG);
				ips_reader = new BufferedReader(new FileReader(file_ip));
				props = true;
			} catch (FileNotFoundException e) {
				// TODO Bloque catch generado 
				e.printStackTrace();				
		}			
			
		 if(props && ips_reader != null){
	    	  String ip_tmp;
			try {
				ip_tmp = ips_reader.readLine();
		
	    	 
	    	  while(ip_tmp!=null){
	    		  ips.add(ip_tmp);
	    		  ip_tmp = ips_reader.readLine();
	    	  }
	    	 
	    	  N = ips.size();
			} catch (IOException e) {
				// TODO Bloque catch generado autommente
				e.printStackTrace();
			}
	      }else{
	    	  N = Integer.valueOf(mayor).intValue() - Integer.valueOf(menor).intValue();
	    	 
	      }
		
		
	}	 
		 
	
	 
	 public static void startInstructionsThread(final String user, final String pass,final String ip_base,final int menor,final int mayor, final int tipo, final String file_ori, final String file_dest, String file_sh, String file_ip){
		 
		 	//System.out.println(file_ip);
		 	setValues(user,pass,ip_base,menor,mayor,tipo,file_ori,file_dest,file_sh,file_ip,null,-1);
		 	//System.out.println(SshTestMultiple.file_ip);
	 		getIps();  
		    
		     Sender.log("hay "+N+" ips en total, primera  ", Sender.LOG_AND_CONSOLE);
		 
		 	if(options!=null){
		 		/*TODO
		 		 * lanzar varios hilos*/
		 		
		 		
			     if(N%4==0){ 
			    	 /*4 hilos*/
			    	 int step = N/4;
			    	 /*3 hilos*/
			    	 options.newConsole(3);
			    	 //new			    	 
			    	 Vector half_ips1 =new Vector(ips.subList((step), step*2));			    	 
			    	 new Sender(user, pass, half_ips1 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(0), file_sh, file_ip).start();
			    	 //new 2			    	 
			    	 Vector half_ips3 =new Vector(ips.subList((step)*2, (step*3)));			    	 
			    	 new Sender(user, pass, half_ips3 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(1), file_sh, file_ip).start();
			    	 //new 3			    	 
			    	 Vector half_ips4 =new Vector(ips.subList((step)*3, N));			    	 
			    	 new Sender(user, pass, half_ips4 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(2), file_sh, file_ip).start();
			    	 //default
			    	 Vector half_ips2 =new Vector(ips.subList(0, step));			    	 
			    	 new Sender(user, pass, half_ips2, tipo, file_ori, file_dest, options.command_output, file_sh, file_ip).start();
			    	 
			     }else if(N%3==0){
			    	 int step = N/3;
			    	 /*3 hilos*/
			    	 options.newConsole(2);
			    	 //new			    	 
			    	 Vector half_ips1 =new Vector(ips.subList((step), step*2));			    	 
			    	 new Sender(user, pass, half_ips1 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(0), file_sh, file_ip).start();
			    	 //new 2			    	 
			    	 Vector half_ips3 =new Vector(ips.subList((step)*2, N));			    	 
			    	 new Sender(user, pass, half_ips3 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(1), file_sh, file_ip).start();
			    	 //default
			    	 Vector half_ips2 =new Vector(ips.subList(0, step));			    	 
			    	 new Sender(user, pass, half_ips2, tipo, file_ori, file_dest, options.command_output, file_sh, file_ip).start();
			    	 
			     }else if(N%2==0){
			    	 int step = N/2;
			    	 /*2 hilos*/			    	 
			    	 options.newConsole(1);
			    	 //new
			    	 Vector half_ips1 =new Vector(ips.subList(step, N));
			    	 Sender.log(half_ips1.get(0)+" "+half_ips1.size(), Sender.LOG_AND_CONSOLE);
			    	 new Sender(user, pass, half_ips1 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(0), file_sh, file_ip).start();
			    	 //default
			    	 Vector half_ips2 =new Vector(ips.subList(0, step));
			    	 Sender.log(half_ips2.get(0)+" "+half_ips2.size(), Sender.LOG_AND_CONSOLE);
			    	 new Sender(user, pass, half_ips2, tipo, file_ori, file_dest, options.command_output, file_sh, file_ip).start();
			     }else{
		 		
			    	 int step = N/2;
			    	 System.out.println(step +" "+(N-step));
			    	 /*2 hilos*/			    	 
			    	 options.newConsole(1);
			    	 //new
			    	 Vector half_ips1 =new Vector(ips.subList(N-step, N));
			    	 Sender.log(half_ips1.get(0)+" "+half_ips1.size(), Sender.LOG_AND_CONSOLE);
			    	 new Sender(user, pass, half_ips1 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(0), file_sh, file_ip).start();
			    	 //default
			    	 Vector half_ips2 =new Vector(ips.subList(0, step));
			    	 Sender.log(half_ips2.get(0)+" "+half_ips2.size(), Sender.LOG_AND_CONSOLE);
			    	 new Sender(user, pass, half_ips2, tipo, file_ori, file_dest, options.command_output, file_sh, file_ip).start();
			     }
		 		
		 	}else{
		 		//consola
		 		new Sender(user, pass, ips, tipo, file_ori, file_dest, null, file_sh, file_ip).start();
		 	}
	 }
	 
	 private static void setValues(String user2, String pass2, String ip_base2, int menor2, int mayor2, int tipo2, String file_ori2, String file_dest2, String file_sh2, String file_ip2,String file_sql2, int driver2) {
		
		 SshTestMultiple.user = user2;
		 SshTestMultiple.pass = pass2;
		 SshTestMultiple.ip_base = ip_base;
		 SshTestMultiple.menor = String.valueOf(menor2);
		 SshTestMultiple.mayor = String.valueOf(mayor2);
		 SshTestMultiple.tipo = String.valueOf(tipo2);
		 SshTestMultiple.file_ori = file_ori2;
		 SshTestMultiple.file_dest = file_dest2;		 
		 SshTestMultiple.file_sh = file_sh2;
		 SshTestMultiple.file_ip = file_ip2;
		 SshTestMultiple.file_sql = file_sql2;
		 SshTestMultiple.driver = String.valueOf(driver2);
	}



	public static void startSQLThread(String user, String pass, String ip_base, int menor, int mayor, int tipo, String file_ori, String file_dest, String file_sql,  String file_ip, int driver) {
		System.out.println(file_ip);
		 setValues( user,  pass,  ip_base,  menor,  mayor,  tipo,  null,  null, null,file_ip,file_sql,  driver);
		 System.out.println(SshTestMultiple.file_ip);
		 getIps();
		 
		 if(options!=null){
			 
			      
			      if(N%2==0){
				    	 int step = N/2;
				    	 /*2 hilos*/			    	 
				    	 options.newConsole(1);
				    	 //new
				    	 Vector half_ips1 =new Vector(ips.subList(step, N));
				    	 Sender.log(half_ips1.get(0)+" "+half_ips1.size(), Sender.LOG_AND_CONSOLE);
				    	 new Sender(user, pass, half_ips1 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(0), file_sql, driver).start();
				    	 //default
				    	 Vector half_ips2 =new Vector(ips.subList(0, step));
				    	 Sender.log(half_ips2.get(0)+" "+half_ips2.size(), Sender.LOG_AND_CONSOLE);
				    	 new Sender(user, pass, half_ips2, tipo, file_ori, file_dest, options.command_output, file_sql, driver).start();
				  }else{
					  int step = N/2;
					  System.out.println(step +" "+(N-step));
				    	 /*2 hilos*/			    	 
				    	 options.newConsole(1);
				    	 //new
				    	 Vector half_ips1 =new Vector(ips.subList((N-step), N));
				    	 Sender.log(half_ips1.get(0)+" "+half_ips1.size(), Sender.LOG_AND_CONSOLE);
				    	 new Sender(user, pass, half_ips1 , tipo, file_ori, file_dest, (JTextArea) options.consoles.get(0), file_sql, driver).start();
				    	 //default
				    	 Vector half_ips2 =new Vector(ips.subList(0, step));
				    	 Sender.log(half_ips2.get(0)+" "+half_ips2.size(), Sender.LOG_AND_CONSOLE);
				    	 new Sender(user, pass, half_ips2, tipo, file_ori, file_dest, options.command_output, file_sql, driver).start();
					  
				  }
			 
		 		
		 	}else{
		 		//consola
		 		new Sender(user, pass, ips, tipo, file_ori, file_dest, null, file_sql, driver).start();
		 	}
			
		}
	 
	

	   

		public static void main(String[] args) throws IOException {
	    		
	    
	    
	    	if(!(args.length>0)){
	    		 JFrame.setDefaultLookAndFeelDecorated(true);
	    		    SwingUtilities.invokeLater(new Runnable() {
	    		      public void run() {
	    		        try {
	    		          UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel");
	    		        } catch (Exception e) {
	    		          System.out.println("Substance Graphite failed to initialize");
	    		        }
	    		        System.out.println("levantando Interfaz");
	    		    	options = new SshOptions();
	    		        options.getOptions();
	    		      }
	    		    });
	    		
	    		
	    	}else if(args.length>0){
	    		System.out.println("desde linea de comando");
	    		parse_arguments(args);
	    		if(tipo!=null){
	    			int tipo_tmp = Integer.valueOf(tipo).intValue();
	    			switch (tipo_tmp) {
					case SshOptions.SH:
					case SshOptions.SCP_FROM:
					case SshOptions.SCP_TO:
						startInstructionsThread(user, pass, ip_base, Integer.valueOf(menor).intValue(), Integer.valueOf(mayor).intValue(), Integer.valueOf(tipo).intValue(), file_ori, file_dest, file_sh, file_ip);
						break;
					case SshOptions.SQL:
						startSQLThread(user, pass, ip_base, Integer.valueOf(menor).intValue(), Integer.valueOf(mayor).intValue(), Integer.valueOf(tipo).intValue(), file_ori, file_dest, file_sql, file_ip,Integer.valueOf(driver).intValue());
						break;
					default:
						System.out.println("problemas en los parametros");
						print_usage();
						break;
						}
	    		}else{
	    			print_usage();
	    		}
	    			
	    	}
	    		
	    }
	    	
	    		
	    	
	    	
	    



		private static void print_usage() {
			System.out.println("Uso: java -jar JSSH.jar [OPTIONS]" +
					"\n Opciones " +
					"\n -h --help " +
					"\n\tMuestra esta ayuda" +
					"\n -i --ips " +
					"\n\t archivo de ips" +
					"\n -c --commands " +
					"\n\t archivo de comandos" +
					"\n -s --sqls " +
					"\n\t archivo de sqls" +
					"\n -u --user " +
					"\n\t usuario de conexion SSH o SQL" +
					"\n -p --password" +
					"\n\t password de conexion SSH o SQL" +
					"\n -f --from " +
					"\n\t ruta de archivo local, al hacer operaciones de SCP" +
					"\n -t --to " +
					"\n\t ruta del archivo remoto al hacer operaciones de SCP" +
					"\n -a --action" +
					"\n\t tipo de operacion a realizar "+SshOptions.SH+"=ejecutar comandos,"+SshOptions.SCP_TO+"=llevar archivos,"+SshOptions.SCP_FROM+"=traer archivos,"+SshOptions.SQL+"=ejecutar sqls" +
					"\n -b --base " +
					"\n\t ip base " +
					"\n -m --menor " +
					"\n\t ip menor en el rango de ips" +
					"\n -M --mayor " +
					"\n\t ip mayor en el rango de ips" +
					"\n -d --driver " +
					"\n\t driver a usar mysql, as400" +
					"\n Archivos: " +
					"\n\t	comandos.properties (contiene los comandos a ejecutar por tipo=0)" +
					"\n\t   ips.properties (contiene ips x.x.x.x una por linea de las ips a procesar)" +
					"\n\t   sql.properties (contiene las consultas a ejecutar)" +
					"\n\n 2009-2010 Eduardo Lugo" +
					"");
			
		}


		
		private static void parse_arguments(String[] args) {
//			 ------------------------------------------------------------
			// Procesamiento de parametros ----------------------------->
			
			for(int i=0; i< args.length; i++){
				if(args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--help")){ 
					
					print_usage();
					System.exit(0);
					
				}else if(args[i].startsWith("--ips=") || args[i].startsWith("-i=")){
					
					file_ip = args[i].split("=")[1];
					
				}else if(args[i].startsWith("--commands=") || args[i].startsWith("-c=")){
					
					file_sh = args[i].split("=")[1];					
					
					
				}else if(args[i].startsWith("--sqls=") || args[i].startsWith("-s=")){
					
					file_sql = args[i].split("=")[1];
					
					
				}else if(args[i].startsWith("--user=") || args[i].startsWith("-u=")){
					
					user = args[i].split("=")[1];
					
				}else if(args[i].startsWith("--password=") || args[i].startsWith("-p=")){
					
					pass = args[i].split("=")[1];
					
				}else if(args[i].startsWith("--from=") || args[i].startsWith("-f=")){
					
					file_ori = args[i].split("=")[1];
					Sender.log("    ADVERTENCIA    : Usando archivo de propiedades \""+file_ori+"\"", Sender.LOG);
					
				}else if(args[i].startsWith("--action=") || args[i].startsWith("-a=")){
					
					tipo = args[i].split("=")[1];
					
					
				}else if(args[i].startsWith("--base=") || args[i].startsWith("-b=")){
					
					ip_base = args[i].split("=")[1];
					
					
				}else if(args[i].startsWith("--menor=") || args[i].startsWith("-m=")){
					
					menor = args[i].split("=")[1];
					
					
				}else if(args[i].startsWith("--mayor=") || args[i].startsWith("-M=")){
					
					mayor = args[i].split("=")[1];
					
					
				}else if(args[i].startsWith("--to=") || args[i].startsWith("-t=")){
					
					file_dest = args[i].split("=")[1];
					Sender.log("    ADVERTENCIA    : Usando archivo de propiedades \""+file_dest+"\"", Sender.LOG);
					
				}else if(args[i].startsWith("--driver=") || args[i].startsWith("-d=")){
					
					driver = args[i].split("=")[1];
					
					
				}else{
					// Si se recibe algun parametro desconocido, puede ser error al invocar el programa por
					// por lo que se muestra el mensaje, la ayuda y se termina el programa.
					// Con esto se ayuda a evitar la generacion de reportes erroneos por parametros incompletos
					
					System.out.println("JSSH: Opcion desconocida \""+args[i]+"\"\n");
					print_usage();
					System.exit(0);
				}
			}
			
			// <---------------------- Fin de procesamiento de parametros
			// ------------------------------------------------------------
			
			
		}

		
	    

		
		 
		

}




