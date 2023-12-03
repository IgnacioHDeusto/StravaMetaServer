package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class MetaServer {
	
	private static int clientes;

	public static void main(String[] args) {
			if (args.length < 1) {
				System.out.println("# Usage: MetaServer[PORT]");
				System.exit(1);
			}

			int serverPort = Integer.parseInt(args[0]);
			
			try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);){
				System.out.println("- MetaServer: Esperando conexiones '" + tcpServerSocket.getInetAddress().getHostAddress() + ": " + tcpServerSocket.getLocalPort() + "' ...");
				while(true) {
					new MetaService(tcpServerSocket.accept());
					System.out.println("- MetaServer: Aceptada conexión de nuevo cliente. Numero de cliente: " + ++clientes);
				}
			} catch (IOException e) {
				System.out.println("# FacebookServer: IO error:" + e.getMessage());
			}
		

	}

}
