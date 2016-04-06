package com.pranav.jms;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

public class MyReceiver {
	public static void main(String[] args) {
		try {
			// 1) Create and start connection
			Properties props = new Properties();
			//For GlassFish 3.1.2
			props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");

			props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
			props.setProperty("java.naming.provide.url", "iiop://localhost:3700");

			//For JBOSS
			
			/*props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");

			props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			props.setProperty("java.naming.provide.url", "jnp://iondelws432:1099");*/
			
			InitialContext ctx = new InitialContext(props);
			QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup("myQueueConnectionFactory");
			QueueConnection con = f.createQueueConnection();
			con.start();
			// 2) create Queue session
			QueueSession ses = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			// 3) get the Queue object
			Queue t = (Queue) ctx.lookup("myQueue");
			// 4)create QueueReceiver
			QueueReceiver receiver = ses.createReceiver(t);

			// 5) create listener object
			MyListener listener = new MyListener();

			// 6) register the listener object with receiver
			receiver.setMessageListener(listener);

			System.out.println("Receiver1 is ready, waiting for messages...");
			System.out.println("press Ctrl+c to shutdown...");
			while (true) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}