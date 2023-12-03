package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MetaService extends Thread{
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;
	
	private List<String> emails = new ArrayList<>();
	
	protected Map<String, String> usuariosRegistrados = new HashMap<>();
	Map<String, String> mInicioSesion = new HashMap<String, String>();
	
	private static String DELIMITER = "#";
	
	public MetaService(Socket socket) {
		try {
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			
			this.usuariosRegistrados.put("elchigna@gmail.com", "111");
			this.emails.add("elchigna@gmail.com");
			
			this.start();
		} catch (Exception e) {
			System.out.println("# MetaService - TCPConnection IO error:" + e.getMessage());
		}
	}
	
	public void run() {
		try {
			String data = this.in.readUTF();			
			System.out.println("   - MetaService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");					
			StringTokenizer tokenizer = new StringTokenizer(data, DELIMITER);
			String accion = tokenizer.nextToken();
			if(accion.equals("login")) {
				String email = tokenizer.nextToken();
				String contrasena = tokenizer.nextToken();
				
				String datos = email + DELIMITER + contrasena;
				
				data = this.login(datos);
			} else if(accion.equals("checkMail")) {
				String datos = tokenizer.nextToken();
				data = this.checkMail(datos);
			}
			this.out.writeUTF(data);					
			System.out.println("   - MetaService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (Exception e) {
			System.out.println("   # MetaService - TCPConnection error" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (Exception e) {
				System.out.println("   # MetaService - TCPConnection IO error:" + e.getMessage());
			}
		}
	}
	
	public String login(String msg) {
		
		if(msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);
				String email = tokenizer.nextToken();
				String contrasena = tokenizer.nextToken();
				
				if(!email.equals("") && !contrasena.equals("")) {
					for (String correo  : usuariosRegistrados.keySet()) {
						if(correo.equals(email)) {
							mInicioSesion.put(email, contrasena);
							
							System.out.println("- Result login(): true");
							
							return "true";
						} else {
							System.out.println("  #MetaService - El usuaio no concuerda con el email: " + email);
						}
					}
				}
			} catch (Exception e) {
				System.out.println("   # MetaService - login() on Meta Failed: " + e.getMessage());
			}
		}
		return null;
	}
	
	public String checkMail(String msg) {
		if(msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);
				String email = tokenizer.nextToken();
				
				if(!email.equals("") && emails.size() > 0) {
					for (String correo : emails) {
						if(correo.equals(email) ) {
							return "true";
						}
					}
				}
			} catch (Exception e) {
				System.out.println("   # MetaService - comprobarMail() on Meta Failed: " + e.getMessage());
			}
		}
		return null;
		
	}
	
}
