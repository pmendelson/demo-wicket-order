package app.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.webapp.WebInfConfiguration;
import org.mortbay.jetty.webapp.WebXmlConfiguration;

public class Start {
	private static Server server;

	public static void main(String[] args) throws Exception {
		try {
			Stop.main(args);
		} catch (ConnectException e) {
			System.err.println("Jetty was not running");
		}
		server = new Server();
		SocketConnector connector = new SocketConnector();

		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(1000 * 60 * 60);
		connector.setSoLingerTime(-1);
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setWar("src/main/webapp");
		bb.setConfigurationClasses(new String[] {
				WebInfConfiguration.class.getName(),
				WebXmlConfiguration.class.getName() });
		bb.setParentLoaderPriority(true);
		// START JMX SERVER
		// MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		// MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		// server.getContainer().addEventListener(mBeanContainer);
		// mBeanContainer.start();

		server.addHandler(bb);

		try {
			Thread monitor = new MonitorThread();
			monitor.start();
			System.out
					.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
			server.start();
			// System.in.read();
			// System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
			// // while (System.in.available() == 0) {
			// // Thread.sleep(5000);
			// // }
			// server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

	private static class MonitorThread extends Thread {
		private ServerSocket socket;

		public MonitorThread() {
			setDaemon(true);
			setName("StopMonitor");
			try {
				socket = new ServerSocket(8079, 1, InetAddress
						.getByName("127.0.0.1"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			System.out.println("*** running jetty 'stop' thread");
			Socket accept;
			try {
				accept = socket.accept();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(accept.getInputStream()));
				reader.readLine();
				System.out.println("*** stopping jetty embedded server");
				server.stop();
				accept.close();
				socket.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
