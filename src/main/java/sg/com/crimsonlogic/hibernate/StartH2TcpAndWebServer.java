package sg.com.crimsonlogic.hibernate;

import org.h2.tools.Server;

public class StartH2TcpAndWebServer {

	public static final String TCP_PORT = "8088";
	public static final String WEB_PORT = "8089";
	
	public static void main(String[] args) {
		
		try {
			
			// Create TCP server
			Server tcpServer = Server.createTcpServer(
					"-tcp",
					"-tcpPort", TCP_PORT, 
					"-tcpAllowOthers",			// to allow client TCP connections
					"-ifNotExists").start();	// to allow remote database creation
			Util.logging(Util.LEVEL.INFO, tcpServer.getStatus());
			
			// Create Web Console Server
			Server webServer = Server.createWebServer(
					"-web", 
					"-webAllowOthers", 
					"-webDaemon", 
					"-webPort", WEB_PORT).start();
			Util.logging(Util.LEVEL.INFO, webServer.getStatus());
			
		} 
		catch (Exception e) {
			Util.logging(Util.LEVEL.ERROR, e.getMessage());
			e.printStackTrace();
		}
		finally {
			Util.logging(Util.LEVEL.INFO, "H2 database is ready for connect.");
		}
	}

}
