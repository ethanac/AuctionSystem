package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
	private Socket msgSocket = null;
	private BufferedReader in;
	private DataOutputStream out;
	public String msg = "";
	public int clientId = -1;
	public boolean quit = false;
	
	public ClientThread(int clientId, Socket socket) throws IOException{
		msgSocket = socket;
		this.clientId = clientId;
		in = new BufferedReader(new InputStreamReader(
	                     msgSocket.getInputStream()));
		out = new DataOutputStream(msgSocket.getOutputStream());
	}
	
	public void run(){
		try {
			out.writeBytes(clientId + "\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
			try {
				if(in.ready()){
					msg = in.readLine();					
//					if(msg != null && msg.contains("Your id is")){
//						clientId = Integer.parseInt(msg.split("#")[1]);
//						//yield();
//						msg = msg.replace("#", "");
//					}
					System.out.println("----------------------------");
					System.out.println(msg);
					
//					if("You have unregistered. Bye!".equals(msg)){
//						quit = true;
//						msgSocket.close();
//						break;
//					}
					msg = "";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.exit(0);
	}
}
