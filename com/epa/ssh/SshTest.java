package com.epa.ssh;

import com.jcraft.jsch.*;
import java.awt.*;

import javax.swing.*;







public class SshTest{	
	 public static void main2(String[] arg){
		    
		    try{
		      JSch jsch=new JSch();

		      jsch.setKnownHosts("/home/programador6/.ssh/known_hosts");

		      String host=null;
		      if(arg.length>0){
		        host=arg[0];
		      }
		      else{
		        host=JOptionPane.showInputDialog("Enter username@hostname",
		                                         System.getProperty("user.name")+"@localhost");
		        


		        
		      }
		      String user=host.substring(0, host.indexOf('@'));
		      host=host.substring(host.indexOf('@')+1);

		      Session session=jsch.getSession(user, host, 22);

		      //session.setPassword("your password");

		      // username and password will be given via UserInfo interface.
		      UserInfo ui= new MyUserInfo();
		      session.setUserInfo(ui);

		      // session.setConfig("StrictHostKeyChecking", "no");

		      //session.connect();
		      session.connect(30000);   // making a connection with timeout.

		      Channel channel=session.openChannel("shell");

		      // Enable agent-forwarding.
		      //((ChannelShell)channel).setAgentForwarding(true);

		      channel.setInputStream(System.in);
		      /*
		      // a hack for MS-DOS prompt on Windows.
		      channel.setInputStream(new FilterInputStream(System.in){
		          public int read(byte[] b, int off, int len)throws IOException{
		            return in.read(b, off, (len>1024?1024:len));
		          }
		        });
		       */

		      channel.setOutputStream(System.out);

		      /*
		      // Choose the pty-type "vt102".
		      ((ChannelShell)channel).setPtyType("vt102");
		      */

		      /*
		      // Set environment variable "LANG" as "ja_JP.eucJP".
		      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
		      */

		      //channel.connect();
		      channel.connect(3*1000);
		    }
		    catch(Exception e){
		      System.out.println(e);
		    }
		  }
	 
	 private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("FrameDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        JLabel emptyLabel = new JLabel("Hola");
	        emptyLabel.setPreferredSize(new Dimension(175, 100));
	        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }

	    public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	    }

}


