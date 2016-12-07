package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This thread is used to listen at the TCP port to accept a request from a client.
 * @author Ethan
 *
 */
public class ListenThread extends Thread{
	private AuctionServer as;
	private ServerSocket serverSocket;
	private Socket socket;
	
	public ListenThread(AuctionServer as) throws IOException{
		this.as = as;
		serverSocket = new ServerSocket(6789);
	}
	
	public void run(){
		try {
			//When get a connection request, start a new thread to handle.
			while(true){
				socket = serverSocket.accept();
				new PushThread(socket, as).start();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
