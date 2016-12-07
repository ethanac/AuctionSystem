package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

public class ServerImpl {
	public static void main(String[] agrs){
		//Create an auction server.
		AuctionServer as = new AuctionServer();
		
		//Create a socket
		ServerSocket serverSocket = null;
		boolean listening = true;
		
		try{
			serverSocket = new ServerSocket(6789);
		}
		catch (IOException e){
			System.err.println("Cannot listen on port 6789.");
			System.exit(-1);
		}
		System.out.println("The auction server is running!");
		while(listening){
			Socket socket = null;
			try {
				//When get a connection request, start a new thread to handle.
				socket = serverSocket.accept();
				new MultiServerThread(socket, as).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
