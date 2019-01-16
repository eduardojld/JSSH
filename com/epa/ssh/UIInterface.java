package com.epa.ssh;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class UIInterface implements UserInfo, UIKeyboardInteractive{
	
	String passwd;

	public String getPassphrase() {
		// TODO Apendice de metodo generado autom�ticamente
		return passwd;
	}

	public String getPassword() {		
		return passwd;
	}

	public boolean promptPassphrase(String arg0) {
		// TODO Apendice de metodo generado autom�ticamente
		return true;
	}

	public boolean promptPassword(String arg0) {
		// TODO Apendice de metodo generado autom�ticamente
		return true;
	}

	public boolean promptYesNo(String arg0) {
		// pregunta si aceptas la llave
		return true;
	}

	public void showMessage(String arg0) {
		// TODO Apendice de metodo generado autom�ticamente

	}

	public String[] promptKeyboardInteractive(String destination, String name, String instruction,
            String[] prompt,    boolean[] echo) {
		String[] response=new String[prompt.length];
	    for(int i=0; i<prompt.length; i++){
	    	 System.out.println(prompt[i]);
	         response[i]=passwd;
	       }
		return response;		
	}

	/**
	 * @return el passwd
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @param passwd el passwd a establecer
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
