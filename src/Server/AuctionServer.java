package Server;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@WebService(endpointInterface="Server.ClinicServerInterface")
@SOAPBinding(style=Style.RPC)
public class AuctionServer {
	final int NUMOFMINUTE = 5;
	public ArrayList<ClientInfo> cList;
	public LinkedList<ItemForSale> items;
	private ArrayList<Integer> activeClients;
	private ArrayList<Integer> highestPrice;
	public LinkedList<String> notification;
	private Timer timer;
	private int clientSequence = -1;
	private int itemSequence = -1;
	
	public AuctionServer(){
		cList = new ArrayList<ClientInfo>();
		items = new LinkedList<ItemForSale>();
		activeClients = new ArrayList<Integer>();
		highestPrice = new ArrayList<Integer>();
		notification = new LinkedList<String>();
		timer = new Timer();
	}
	
	/**
	 * Show current items.
	 * @return
	 */
	public String showInfo(){
		if(!items.isEmpty()){
			String s = "";
			int itemId = 0;
			for(ItemForSale i : items){
				if(i != null){
					s += itemId + "-" + i.getName() + ":" + highestPrice.get(itemId) + "|    ";
					itemId++;
				}
			}
			if(!s.equals(""))
				return "We have these items for sale:  " + s;
		}
		return "We have nothing for sale now. Please wait.";
	}
	
	/**
	 * Register a client.
	 * @param name
	 * @return
	 */
	public int register(String name){
		int id;
		//String output = "";
		synchronized(this){
			id = ++clientSequence;
			cList.add(new ClientInfo(id, name));
		}
		return id;
	}
	
	/**
	 * Unregister a client. The client may not be able to unregister.
	 * @param id
	 * @return
	 */
	public String unRegister(int id){
		boolean quitable = true;
		String output = "";
		if(cList.get(id).getItemOffering().size() > 0){
			quitable = false;
		}
		else{
			for(int i : activeClients){
				if(i == id){
					quitable = false;
					break;
				}
			}
		}
		if(quitable){
			cList.set(id, null);
		}
		else
			output = "Cannot quit now! You may have offered items or be active.";
		return output;
	}
	
	/**
	 * Offer an item to the auction.
	 * @param name
	 * @param providerId
	 * @param desc
	 * @return
	 */
	public String offerItem(String name, int providerId, String desc){
		int itemId;
		synchronized(this){
			itemId = ++itemSequence;
			activeClients.add(-1);
			items.add(new ItemForSale(itemId, providerId, name, desc));
			highestPrice.add(0);
		}
		cList.get(providerId).offerItem(items.get(itemId));
		startTimer(itemId);
		//notification.add("Client #" + providerId + " offered an item: #" + itemId + " " + name);
		return itemId + ": " + name;
	}
	
	/**
	 * Bid for an item.
	 * @param clientId
	 * @param itemId
	 * @param price
	 * @return
	 */
	public String bid(int clientId, int itemId, int price){
		String output = "";
		int p;
		if(itemId >= highestPrice.size())
			p = -2;
		else
			p = highestPrice.get(itemId);
		synchronized(this){
			if(p != -2 && price > p){
				highestPrice.set(itemId, price);
				activeClients.set(itemId, clientId);
				output = "Client " + clientId + " bid " + price + " for item " + itemId;
			}
			else if(p == -2){
				output = "unavailable";
			}
		}
		return output;
	}
	
	/**
	 * End a bidding.
	 * @param itemId
	 */
	public void endBidding(int itemId){
		int winner = -1;
		String result = "";
		String mailAddr = "";
		String itemInfo = items.get(itemId).getName() + items.get(itemId).getDescription();
		if(activeClients.get(itemId) > -1){
			winner = activeClients.get(itemId);
			result = "Client #" + winner + " won the item #" + itemId + "-" + items.get(itemId).getName() + ". Congratulations!";
			synchronized(this){
				activeClients.set(itemId, -2);
				highestPrice.set(itemId, -2);
			}
			mailAddr = cList.get(winner).getName();
			sendMail(mailAddr, itemInfo);
		}
//		else
//			result = "No body bid for item #" + itemId + "-" + items.get(itemId).getName() + ". It will be returned to client #" + items.get(itemId).getProvider();
		int providerId = items.get(itemId).getProvider();
		cList.get(providerId).removeItem(items.get(itemId));
		items.set(itemId, null);
		notification.add(result);
	}
	
	/**
	 * Timer for an item.
	 * @param itemId
	 */
	public void startTimer(int itemId){
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(activeClients.get(itemId) > -1)
					endBidding(itemId);
				else{
					startTimer(itemId);
					notification.add("Nobody bid for item #" + itemId + ". Start a new round.");
				}
			}
		}, NUMOFMINUTE*60*1000);
	}
	
	/**
	 * Get an item.
	 * @param itemId
	 * @return
	 */
	public ItemForSale getItem(int itemId){
		return items.get(itemId);
	}
	
	public void sendMail(String addr, String item){
		final String username = "haoz86@gmail.com";
		final String password = "55612Dirichlet";

		String from = "haoz86@gmail.com";
		String to = addr;
		String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "587");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	         }
	      });

	      try {
	         // Create a MimeMessage object.
	         Message message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(from));
	         message.setRecipients(Message.RecipientType.TO,
	         InternetAddress.parse(to));

	         message.setSubject("You won!");
	         message.setText("Congratulations! You won the item " + item + ".");

	         // Send message
	         Transport.send(message);

	         //System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	}
}
