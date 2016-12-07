package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PushThread extends Thread{
	private Socket socket = null;
	private AuctionServer as = null;
	private int clientId = -1;
	private int nNum = 0;
	
	public PushThread(Socket socket, AuctionServer as) {
		super("PushThread");
		this.socket = socket;
		this.as = as;
	}
	
	public void run(){
		try {
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
		                new InputStreamReader(
		                socket.getInputStream()));
		    String inputLine = in.readLine();
		    clientId = Integer.parseInt(inputLine);
		    
		    String outputLine = "";	    
		    out.println(outputLine);
		    while (true){
		    	if(as.cList.get(clientId) == null)
					break;
		    	if(nNum > as.notification.size() - 1){
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	} else {
		    		outputLine = as.notification.get(nNum);
		    		out.println(outputLine);
		    		nNum++;
		    	}
		    }
		    out.close();
		    in.close();
		    socket.close();
		} 
		catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
