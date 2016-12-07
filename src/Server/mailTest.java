package Server;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Notify clients by sending an e-mail.
 * This feature may not be used.
 * @author Ethan
 *
 */
public class mailTest {
	public static void main(String [] args) {    
		final String username = "haoz86@gmail.com";
		final String password = "55612Dirichlet";

//		Properties props = new Properties();
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.socketFactory.port", "465");
//		props.put("mail.smtp.socketFactory.class",
//				"javax.net.ssl.SSLSocketFactory");
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.port", "465");
//
//		Session session = Session.getDefaultInstance(props,
//			new javax.mail.Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(username,password);
//				}
//			});
//
//		try {
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("haoz86@gmail.com"));
//			message.setRecipients(Message.RecipientType.TO,
//					InternetAddress.parse("haoz.mtl@gmail.com"));
//			message.setSubject("Testing Subject");
//			message.setText("Dear Mail Crawler," +
//					"\n\n No spam to my email, please! You won!");
//
//			Transport.send(message);
//
//			System.out.println("Done");
//
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		}
		String from = "haoz86@gmail.com";
		String to = "haoz.mtl@gmail.com";
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

	         message.setSubject("Testing Subject");
	         message.setText("Hello, this is sample for to check send "
	            + "email using JavaMailAPI ");

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	}
}
