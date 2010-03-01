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

public class Stop {
	private static Server server;

	public static void main(String[] args) throws Exception {
		try {
		stop(args);
		} catch (ConnectException e) {
			System.err.println("Jetty was not running");
		}
	}

	public static void stop(String[] args) throws Exception {
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 8079);
		OutputStream out = s.getOutputStream();
		System.out.println("*** sending jetty stop request");
		out.write(("\r\n").getBytes());
		out.flush();
		s.close();
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
