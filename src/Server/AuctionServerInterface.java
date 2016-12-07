package Server;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style=Style.RPC)
public interface AuctionServerInterface {
	String register(String s);
	String showItems();
	String offerItems(String s);
	String bidItems(String s);
	String unregister(String s);
}
