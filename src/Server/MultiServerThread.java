package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(endpointInterface="Server.ClinicServerInterface")
@SOAPBinding(style=Style.RPC)
public class MultiServerThread extends Thread{
	private Socket socket = null;
	private AuctionServer as = null;
	private int clientId = -1;
	private int nNum = 0;
	
	public MultiServerThread(Socket socket, AuctionServer as) {
		super("MultiServerThread");
		this.socket = socket;
		this.as = as;
	}
	
	public void run(){
		try {
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
		                new InputStreamReader(
		                socket.getInputStream()));

		    String inputLine = null;
		    String outputLine = "";
		    inputLine = in.readLine();
		    outputLine = processInput(inputLine);
		    out.println(outputLine);
		    while (true){
		    	if(nNum > as.notification.size() - 1){
			    	if(in.ready()){
			    		inputLine = in.readLine();
				    	outputLine = processInput(inputLine);
				    	out.println(outputLine);
				    	if (outputLine.substring(outputLine.length()-4, outputLine.length()).equals("Bye!"))
				    		break;
			    	} else
						try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    	}
		    	else {
		    		//System.out.println(as.notification.getFirst());
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
	
	/**
	 * Regarding to the user's input, conduct operations.
	 * @param input
	 * @return
	 */
	private String processInput(String input){
		String output = "Invalid input.";
		if(!input.equals("")){
			String fields[] = input.split(",");
			switch(Integer.parseInt(fields[0])){
			case 1:
				clientId = as.register(fields[1]);
				output = "You have registered successfully. Your id is #" + clientId + "#. " + as.showInfo();
				break;
			case 2:
				output = as.showInfo();
				break;
			case 3:
				String s = as.offerItem(fields[1], clientId, fields[2]);
				as.notification.add("Client #" + clientId + " offered an item: #" + s );
				output = "You offered an item: #" + s + ". Thank you.";
				break;
			case 4:
				if(clientId == Integer.parseInt(fields[1]))
					output = "You cannot bid the item provided by you.";
				else{
					String t = as.bid(clientId, Integer.parseInt(fields[1]), Integer.parseInt(fields[2]));
					if (!t.equals("")){
						if(!t.equals("unavailable")){
							output = "You bid " + fields[1] + " for " + fields[2];
							as.notification.add("Client #" + clientId + " bid " + fields[2] + " for the item: #" + fields[1] );
						}
						else
							output = "The item is not available!";
					}
					else
						output = "Your price is not high enough.";
				}
				break;
			case 5:
				output = as.unRegister(Integer.parseInt(fields[1]));
				if (output.equals(""))
					output = "You have unregistered. Bye!";
				break;
			}
		}
		else if(as.notification != null){
			output = as.notification.getFirst();
			as.notification.removeFirst();
		}
		return output;
	}

}
