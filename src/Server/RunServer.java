package Server;

import java.io.IOException;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(endpointInterface="Server.AuctionServerInterface")
@SOAPBinding(style=Style.RPC)
public class RunServer implements AuctionServerInterface {
	public AuctionServer as;
	
	public RunServer() throws IOException{
		as = new AuctionServer();
		new ListenThread(as).start();
	}
	
	/**
	 * Register.
	 * Format: "userName"
	 */
	public String register(String s){
		int clientId;
		clientId = as.register(s);
		
		return "You have registered successfully. Your id is #" + clientId + "#. " + as.showInfo();
	}
	
	/**
	 * show items.
	 */
	public String showItems(){
		return as.showInfo();
	}
	
	/**
	 * Offer an item.
	 * Format: "itemName,clientId,itemDescription"
	 */
	public String offerItems(String s){
		String fields[] = s.split(",");
		int clientId = Integer.parseInt(fields[0]);
		String r = as.offerItem(fields[1], clientId, fields[2]);
		as.notification.add("Client #" + clientId + " offered an item: #" + s );
		return "You offered an item: #" + r + ". Thank you.";
	}
	
	/**
	 * Bid an item.
	 * Format: "clientId,itemNum,price"
	 */
	public String bidItems(String s){
		String fields[] = s.split(",");
		String output;
		int itemId = Integer.parseInt(fields[1]);
		int clientId = Integer.parseInt(fields[0]);
		int bidPrice = Integer.parseInt(fields[2]);
		if(itemId < as.items.size() && clientId == as.items.get(itemId).getProvider())
			output = "You cannot bid the item provided by you.";
		else{
			String t = as.bid(clientId, itemId, bidPrice);
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
		return output;
	}
	
	/**
	 * Unregister.
	 * Format: "clientId"
	 */
	public String unregister(String s){
		String output;
		output = as.unRegister(Integer.parseInt(s));
		if (output.equals(""))
			output = "You have unregistered. Bye!";
		return output;
	}

//	public static void main(String[] agrs) throws IOException{
//		//Create an auction server.
//		RunServer rs = new RunServer();
//		
//		System.out.println("The auction server is running!");
//		
//	}
}
