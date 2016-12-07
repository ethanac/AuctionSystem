package Client;

import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import Server.AuctionServerInterface;

public class RunClient {
	public static void menuBeforeReg(){
		System.out.println("1. Register \n2. Quit");
	}
	public static void mainMenu(){
		System.out.println("1. Show items \n2. Offer an item \n3. Bid for an item \n4. Unregister and quit");
	}
	
	public static void main(String argv[]) {
		int clientId = -1;
		Scanner sc = new Scanner(System.in);
		String userInput = "";
		ClientThread ct = null;
		
		try{
			AuctionServerInterface server = null;
			// Get the web service by url and qName.
			URL url = new URL("http://localhost:8080/auction?wsdl");
			QName qName = new QName("http://Server/", "RunServerService");
			Service service = Service.create(url, qName);
			server = service.getPort(AuctionServerInterface.class);
			
			System.out.println("\n****Welcome to Auction Client****\n");
			while(true){
				if(clientId == -1){
					System.out.println("You have not registered. Please register first. Enter 1 or 2:");
					menuBeforeReg();
					userInput = sc.next();
					int choice = Integer.parseInt(userInput);
					if(choice == 1){
						System.out.println("Please enter your name: ");
						userInput = sc.next();
						System.out.println("Your name is " + userInput);
						String msg = server.register(userInput);
						if(msg != null && msg.contains("Your id is")){
							clientId = Integer.parseInt(msg.split("#")[1]);
							msg = msg.replace("#", "");
							System.out.println(msg);
						}
						// Start a message receiver.
						Socket clientSocket = new Socket("localhost", 6789);
						ct = new ClientThread(clientId, clientSocket);
						ct.start();
						
						break;
					}
					else if(choice == 2){
						System.out.println("Bye!");
						System.exit(0);
					}
					else{
						System.out.println("Please enter a valid number.");
					}
				}
			}
			
			while(true) {
				mainMenu();
				System.out.println("");
				System.out.println("Please enter your choice (1/2/3/4):");
				if(sc.hasNext()){
					userInput = sc.next();
					// Manage user selection.
					switch(Integer.parseInt(userInput)) {
					case 1:
						System.out.println(server.showItems());
						break;
					case 2:
						String itemMsg = ",";
						System.out.println("Please enter the name of your item and the description of this item: ");
						userInput = sc.next();
						itemMsg += userInput + ",";
						//System.out.println("Please enter the description of your item: ");
						userInput = sc.nextLine();
						itemMsg += userInput;
						System.out.println(server.offerItems(clientId + itemMsg));
						break;
					case 3:
						String biddingMsg = ",";
						System.out.println("Please enter the number of the item you want to bid: ");
						userInput = sc.next();
						biddingMsg += Integer.parseInt(userInput) + ",";
						System.out.println("Please enter the bidding price (an integer): ");
						userInput = sc.next();
						biddingMsg += Integer.parseInt(userInput) + ",";
						System.out.println(server.bidItems(clientId + biddingMsg));
						break;
					case 4:
						String msg = (server.unregister(clientId + ""));
						System.out.println(msg);
						if("You have unregistered. Bye!".equals(msg)){
							sc.close();
							System.exit(0);
						}
						break;
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
