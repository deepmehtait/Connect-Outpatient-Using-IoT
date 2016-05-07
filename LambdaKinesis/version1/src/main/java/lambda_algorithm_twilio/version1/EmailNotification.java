package lambda_algorithm_twilio.version1;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotification {
	public static void emailDoctor(String patientName, String value, String doctorName, String emergencyContact, String emergencyName) {
		Properties properties = new Properties();
		// To authenticate the user using AUTH command
		properties.put("mail.smtp.auth", "true");
		// Connection to specified SMTP Server
		properties.put("mail.smtp.host", "smtp.gmail.com");
		// Used to connect to specified port of SMPT socket
		properties.put("mail.smtp.socketFactory.port", "465");
		// Used to create SMTP Sockets
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// SMTP server port is mentioned to which connection will be made
		properties.put("mail.smtp.port", "465");
		
		// The user-name and the password is mentioned to authenticate the Session using Authenticator()
		Session session = Session.getDefaultInstance(properties,
			new javax.mail.Authenticator() {
				// Mention the user-name and password of the sender account
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("connectoutpatient","Qwerty123!");
				}
			});

		try {
			// Used to parse and store the headers of the message to be sent
			Message emailContent = new MimeMessage(session);
			// Email ID of the source
			emailContent.setFrom(new InternetAddress("connectoutpatient@gmail.com"));
			// Email ID of the Doctor
			emailContent.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ishneet.kaur@sjsu.edu"));
			// Subject to be sent for the Email to Doctor
			emailContent.setSubject("Emergency Situation for " + patientName);
			// The Content of the mail to be sent to the Doctor
			emailContent.setText("Dear " + doctorName + "," + "\n\n" + "Patient " + patientName + " has heart rate of " + value + "." + "\n\n" + "It is beyond the set delta value."
					+ "\n\nYou can reach the emergency contact on file : " + emergencyName + " at " + emergencyContact + ".\n\nThanks, \nConnect Outpatient Team");
			// Sending the mail after setting the properties and header
			Transport.send(emailContent);
			
			System.out.println("Sent Email Notification");
		} catch (Exception e) {
			System.out.println("Exception caught while sending Email Notification.");
			e.printStackTrace();
		}
	}
}