package Server;

import javax.xml.ws.Endpoint;

public class Publish {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Endpoint server = Endpoint.publish("http://localhost:8080/auction", new RunServer());
			
			System.out.println("The auction server is running: " + server.isPublished());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

