package ch.tkayser.budget.swing.service;

import java.io.InputStream;
import java.util.Properties;

import javax.naming.InitialContext;

import ch.tkayser.budget.exception.BudgetException;

import com.sun.appserv.security.ProgrammaticLogin;

public class GlasfishLoginHandler {
	
	private static final String HOST_PROPERTY = "org.omg.CORBA.ORBInitialHost";
	private static final String PORT_PROPERTY = "org.omg.CORBA.ORBInitialPort";
			
	
	public static InitialContext login(LoginInformation loginInformation)  throws BudgetException {
		
		try {
			// Initla context erstellen
			// Basis properties aus jndi.properties file
	        Properties p = new Properties();
	        InputStream stream = ServiceLocator.class.getClassLoader().getResourceAsStream("jndi.properties");
	        if (stream == null) {
	        	throw new BudgetException("jndi File not Found");
	        }
	        p.load(stream);
	        
	        // ergaenzen mit Server/port
	        p.put(HOST_PROPERTY, loginInformation.getServer());
	        p.put(PORT_PROPERTY, loginInformation.getPort());
	        InitialContext context = new InitialContext(p);
			
			
			// login
	        System.setProperty("java.security.auth.login.config", "auth.conf");
	        ProgrammaticLogin pl = new ProgrammaticLogin();
	        pl.login(loginInformation.getUserName(), loginInformation.getPassword().toCharArray());
	        
	        return context;
			
		} catch (Exception e) {
			if (e instanceof BudgetException) {
				throw (BudgetException)e;
			} else {
				throw new BudgetException("Fehler beim anmelden: "+e.getMessage(), e);
			}
		}
		
	}

}
