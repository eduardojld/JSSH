package com.epa.ssh;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;



public class SimpleLog {

	 private static final String LOG_CONFIG_FILE_XML = "log4j.xml";

	     // Static variables
	    private static boolean initialized = false;

	    // Instance variables
	    private Category category = null;
	    
	 SimpleLog(Class component) {
	        super();

	        // Initialize the logging system if required
	        if (!initialized) {
	            init();
	        }

	        // Register this component as a Log4J category
	        this.category = Category.getInstance(component);
	    }
	 
	 private synchronized void init() {
	        // Safeguard against possible race condition
	        if (initialized) {
	            return;
	        }

	        try {
	            // Use a Log4J DOMConfigurator to load logging information from
	            // a xml file. configureAndWatch() will start a thread to
	            // check the properties file every 60 seconds to see if it has
	            // changed, and reload the configuration if necessary.
	            // DOMConfigurator.configureAndWatch(LOG_CONFIG_FILE_XML, 30000);
	            //URL configFile = new File(LOG_CONFIG_FILE_XML).toURL();
	        	//Thread.currentThread().getContextClassLoader().getResource(LOG_CONFIG_FILE_XML);
	        	 
	            URL configFile = Sender.class.getResource(LOG_CONFIG_FILE_XML); 
	           // System.out.println("----->"+configFile);
	            if (configFile != null) {
	                DOMConfigurator.configure(configFile);
	            }

	       /* } catch (MalformedURLException e) {
	            throw new RuntimeException(e.getLocalizedMessage(), e);*/
	        } catch (FactoryConfigurationError e) {
	            throw new RuntimeException(e.getLocalizedMessage(), e);
	        }

	        // We have now initialized the logging system successfully
	        initialized = true;
	    }
	 
	 public void info(Object o) {
	        this.category.info(o);
	    }
	 
	 public void error(Object o) {
	        this.category.error(o);
	    }
	 
	  public void debug(Object o) {
	        this.category.debug(o);
	    }
	  
	  public static synchronized SimpleLog getInstance(Class component) {
	        return new SimpleLog(component);
	    }
}
