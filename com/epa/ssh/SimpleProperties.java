package com.epa.ssh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;



public class SimpleProperties extends Properties {

	 private static final String PROPERTIES_FILE = "jssh.properties";
	 SimpleLog logger = new SimpleLog(SimpleProperties.class);
	    
	 SimpleProperties() {
	        super();
	       
	    }
	
	 protected void cargarDesdeArchivo(String propFileName, String defaultPath)  throws IOException {

			 URL propURL = findResource(propFileName, defaultPath, (Class) null);
			 if (propURL != null) {
			     InputStream propIS = propURL.openStream();
			
			     if (propIS != null) {
			         try {
			             load(propIS);
			         } catch (IOException e) {
			             logger.error("loadProperties()" + e.toString());
			             throw e;
			         }
			     } else {
			         logger.error("cargarDesdeArchivo() - No se consiguio el archivo de propiedades: "
			                         + propFileName);
			         throw new FileNotFoundException(
			                 "Falta archivo de propiedades: " + propFileName);
			     }
			 } else {
			     logger
			             .error("cargarDesdeArchivo() - No se consiguio el archivo de propiedades: "
			                     + propFileName);
			     throw new FileNotFoundException("Falta archivo de propiedades: "
			             + propFileName);
			 }
			
			}
	 
	 
	 public static URL findResource(final String resource, String defaultPath,
	            final Class clase) throws MalformedURLException {
	        URL propURL = null;

	        propURL = Thread.currentThread().getContextClassLoader().getResource(
	                resource);
	        if (propURL != null) {
	            System.out.println("Encontrado recurso: " + resource
	                    + " en ContextClassLoader del Thread");
	        }
	        if (propURL == null) {
	            String propsFile = System.getProperty("user.home") + File.separator
	                    + resource;
	            File file = new File(propsFile);
	            if (file.exists() && file.isFile()) {
	                propURL = file.toURL();
	            } else {
	                propURL = null;
	            }
	            if (propURL != null) {
	            	System.out.println("Encontrado recurso en user.home: " + propsFile);
	            }
	        }

	        // Busco en la ruta por defecto
	        if (propURL == null && defaultPath != null) {
	            if (defaultPath.length() > 0
	                    && defaultPath.charAt(defaultPath.length() - 1) != '/'
	                    && defaultPath.charAt(defaultPath.length() - 1) != '\\') {
	                defaultPath = defaultPath.concat(File.separator);
	            }
	            File propsFile = new File(defaultPath + resource);
	            if (propsFile.exists() && propsFile.isFile()) {
	                propURL = propsFile.toURL();
	            } else {
	                propURL = null;
	            }
	            if (propURL != null ) {
	                System.out.println("Encontrado recurso en defaultPath: " + propsFile);
	            }
	        }

	        if (propURL == null) {
	            File propsFile = new File(resource);
	            if (propsFile.exists() && propsFile.isFile()) {
	                propURL = propsFile.toURL();
	            } else {
	                propURL = null;
	            }
	            if (propURL != null ) {
	                System.out.println("Encontrado recurso en user.dir: " + propsFile);
	            }
	        }

	        // Busco como recurso del sistema en el classLoader de esta clase
	        if (clase != null) {
	            if (propURL == null) {
	                propURL = clase.getClassLoader().getResource(resource);
	                if (propURL != null ) {
	                    System.out.println("Encontrado recurso en ClassLoader de la Clase '"
	                            + clase.getPackage().getName() + "."
	                            + clase.getName() + "': " + propURL);
	                }
	            }

	            // Busco como recurso del sistema en el classpath
	            if (propURL == null) {
	                propURL = clase.getResource(resource);
	                if (propURL != null) {
	                    System.out.println("Encontrado recurso en ClassPath de la Clase '"
	                            + clase.getPackage().getName() + "."
	                            + clase.getName() + "': " + propURL);
	                }
	            }
	        }
	        // Busco como recurso del sistema en el classLoader de esta clase
	        if (propURL == null) {
	            propURL = SimpleProperties.class.getClassLoader().getResource(resource);
	            if (propURL != null) {
	                System.out.println("Encontrado recurso en ClassLoader del EpaComun: "
	                        + propURL);
	            }
	        }

	        // Busco como recurso del sistema en el classpath
	        if (propURL == null) {
	            propURL = SimpleProperties.class.getResource(resource);
	            if (propURL != null ) {
	            	System.out.println("Encontrado recurso en Class del EpaComun: "
	                                + propURL);
	            }
	        }
	        // Busco como recurso del sistema en el classpath
	        if (propURL == null) {
	            propURL = ClassLoader.getSystemResource(resource);
	            if (propURL != null) {
	            	System.out.println("Encontrado recurso en  ClassLoader.getSystemResource: "
	                                + propURL);
	            }
	        }
	        if (propURL == null) {
	        	System.out.println("NO SE ENCONTRO RECURSO: " + resource);
	        }
	        return propURL;
	    }
	
}
