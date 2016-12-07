package Server;

public class ItemForSale {
	private int itemId;
	private int providerId;
	private String itemName;
	private String itemDescription = "It is valuable.";
	
	public ItemForSale(int id, int providerId, String name, String description){
		itemId = id;
		this.providerId = providerId;
		itemName = name;
		if(!description.equals(""))
			itemDescription = description;
	}
	
	public int getId(){
		return itemId;
	}
	
	public int getProvider(){
		return providerId;
	}
	
	public String getName(){
		return itemName;
	}
	
	public String getDescription(){
		return itemDescription;
	}
}
