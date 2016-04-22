package lambda_algorithm_twilio.version1;

import java.util.*; 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*;  
 
public class TwilioSMS { 
 public static final String ACCOUNT_SID = "AC1455ee0bbd738e954a909af61ecd1139"; 
 public static final String AUTH_TOKEN = "68f23912666fdd213738678e35742e71"; 
 
 public void messageTwilio(String contactNumber) throws TwilioRestException { 
	TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
 
	 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
	 params.add(new BasicNameValuePair("To", contactNumber)); 
	 params.add(new BasicNameValuePair("From", "+18442932272")); 
	 params.add(new BasicNameValuePair("Body", "Your heart rate has reached beyond the threshold."));   
 
	 MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
	 Message message = messageFactory.create(params); 
	 System.out.println(message.getSid()); 
 } 
}