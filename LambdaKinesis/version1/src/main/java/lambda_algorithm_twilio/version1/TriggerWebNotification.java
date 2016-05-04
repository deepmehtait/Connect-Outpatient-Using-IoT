package lambda_algorithm_twilio.version1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twilio.sdk.TwilioRestException;

public class TriggerWebNotification {
	private static int centerValue = 72;
	private final static int delta = 9;
	
	public static void process(String value, String sensorID) throws Exception{
		if(criticalEstimate(Integer.parseInt(value))){
		  String url = "http://52.8.186.40/notification";
		  String param = "{\"fitBitId\":\"" + sensorID + "\", \"value\":\"" + value + "\"}";
		  String charset = "UTF-8"; 
		  URLConnection connection = new URL(url).openConnection();
		  connection.setDoOutput(true); 
		  connection.setRequestProperty("Accept-Charset", charset);
		  connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);

		  try (OutputStream output = connection.getOutputStream()) {
		    output.write(param.getBytes(charset));
		  }

		  InputStream response = connection.getInputStream();
		  BufferedReader bR = new BufferedReader(  new InputStreamReader(response));
		  String line = "";
		  StringBuilder responseStrBuilder = new StringBuilder();
		  while((line =  bR.readLine()) != null){
		      responseStrBuilder.append(line);
		  }
		  response.close();
		  JSONObject result= new JSONObject(responseStrBuilder.toString());    
		  JSONObject patientObj = (JSONObject) result.get("patientInfo");
		  JSONArray doctors = (JSONArray) result.get("doctorInfo");
		  for(int i = 0; i < doctors.length(); i++){
			  callTwilio(((JSONObject)doctors.get(i)).get("phoneNumber").toString(), value, patientObj.get("displayName").toString(), ((JSONObject)doctors.get(i)).get("displayName").toString());
		  }
		  callTwilio(patientObj.get("emergencyContactNumber").toString(), value, patientObj.get("displayName").toString(), patientObj.get("emergencyContactName").toString());
		}
	}
	
	private static boolean criticalEstimate(int heartRate) {
		int difference = heartRate - centerValue;
		if(Math.abs(difference) >= delta){
			System.out.println("Critical HeartRate");
			return true;
		}
		else{
			centerValue = (centerValue + heartRate)/2 ;
		}
		System.out.println("No Critical HeartRate" + centerValue);
		return false;
	}
	
	public static void callTwilio(String contactNumber, String record, String patientName, String contactName){
		try {
			TwilioCall twilioCall = new TwilioCall();
	        try {
				twilioCall.callTwilio(contactNumber, patientName, record, contactName);
			} catch (TwilioRestException e) {
				System.out.println("Twilio Call Exception");
				e.printStackTrace();
			}
	        
	        TwilioSMS twilioSms = new TwilioSMS();
	        try {
				twilioSms.messageTwilio(contactNumber, patientName, record, contactName);
			} catch (TwilioRestException e) {
				System.out.println("Twilio Message Exception");
				e.printStackTrace();
			}
			}
		catch (Exception e) {
			System.out.println("Data Value Exception");
	        e.printStackTrace();
	}
}
}
