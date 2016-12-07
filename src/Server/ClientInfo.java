package Server;
import java.util.LinkedList;

public class ClientInfo {
	private int ID;
	private String clientName;
	private LinkedList<ItemForSale> itemsOffering;
	private LinkedList<ItemForSale> itemsBidding;
	
	public ClientInfo(int id, String name){
		ID = id;
		clientName = name;
		itemsOffering = new LinkedList<ItemForSale>();
		itemsBidding = new LinkedList<ItemForSale>();
	}
	
	public int getId(){
		return ID;
	}
	
	public String getName(){
		return clientName;
	}
	
	public void offerItem(ItemForSale item){
		itemsOffering.add(item);
	}
	
	public void bidItem(ItemForSale item){
		itemsBidding.add(item);
	}
	
	public LinkedList<ItemForSale> getItemOffering(){
		return itemsOffering;
	}
	
	public void removeItem(ItemForSale item){
		if(itemsOffering.contains(item)){
			itemsOffering.remove(item);
		}
	}
	
	public LinkedList<ItemForSale> getItemBidding(){
		return itemsBidding;
	}
	
}
