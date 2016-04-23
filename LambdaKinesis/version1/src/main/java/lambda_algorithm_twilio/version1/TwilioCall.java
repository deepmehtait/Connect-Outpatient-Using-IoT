package lambda_algorithm_twilio.version1;

import java.util.*; 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 
 
public class TwilioCall { 
 
 public static final String ACCOUNT_SID = "AC1455ee0bbd738e954a909af61ecd1139"; 
 public static final String AUTH_TOKEN = "68f23912666fdd213738678e35742e71"; 
 
 public void callTwilio(String contactNumber) throws TwilioRestException { 
	TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
	System.out.println("In Twilio Call");
	List<NameValuePair> params = new ArrayList<NameValuePair>(); 
	params.add(new BasicNameValuePair("To", contactNumber)); 
	params.add(new BasicNameValuePair("From", "+18442932272")); 
	params.add(new BasicNameValuePair("Url", "http://ishneetkaur.com/test1.xml"));  
	params.add(new BasicNameValuePair("Method", "GET"));  
	params.add(new BasicNameValuePair("FallbackMethod", "GET"));  
	params.add(new BasicNameValuePair("StatusCallbackMethod", "GET"));    
	params.add(new BasicNameValuePair("Record", "false")); 
 
	CallFactory callFactory = client.getAccount().getCallFactory(); 
	Call call = callFactory.create(params); 
	System.out.println(call.getSid()); 
 } 
}
