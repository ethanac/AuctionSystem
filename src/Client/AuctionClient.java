package Client;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class AuctionClient {
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
		DataOutputStream outToServer = null;
		ClientThread ct = null;
		try{
			Socket clientSocket = new Socket("localhost", 6789);
			System.out.println("\n****Welcome to Auction Client****\n");
			while(true){
				if(clientId == -1){
					System.out.println("You have not registered. Please register first. Enter 1 or 2:");
					menuBeforeReg();
					userInput = sc.next();
					int choice = Integer.parseInt(userInput);
					if(choice == 1){
						outToServer = new DataOutputStream(clientSocket.getOutputStream());
						ct = new ClientThread(clientId, clientSocket);
						ct.start();
						//inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						System.out.println("Please enter your name: ");
						userInput = sc.next();
						System.out.println("Your name is " + userInput);
						outToServer.writeBytes(1 + "," + userInput + "\n");
						if(ct.clientId != -1)
							clientId = ct.clientId;
						//System.out.println(clientId);
						break;
					}
					else if(choice == 2){
						System.out.println("Bye!");
						clientSocket.close();
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
						outToServer.writeBytes(2 + "," + userInput + "\n");
						break;
					case 2:
						String itemMsg = "";
						System.out.println("Please enter the name of your item and the description of this item: ");
						userInput = sc.next();
						itemMsg += userInput + ",";
						//System.out.println("Please enter the description of your item: ");
						userInput = sc.nextLine();
						itemMsg += userInput + ",";
						outToServer.writeBytes(3 + "," + itemMsg + "\n");
						break;
					case 3:
						String biddingMsg = "";
						System.out.println("Please enter the number of the item you want to bid: ");
						userInput = sc.next();
						biddingMsg += Integer.parseInt(userInput) + ",";
						System.out.println("Please enter the bidding price (an integer): ");
						userInput = sc.next();
						biddingMsg += Integer.parseInt(userInput) + ",";
						outToServer.writeBytes(4 + "," + biddingMsg + "\n");
						break;
					case 4:
						outToServer.writeBytes(5 + "," + ct.clientId + "\n");
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
