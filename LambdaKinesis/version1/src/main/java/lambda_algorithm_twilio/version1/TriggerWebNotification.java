package lambda_algorithm_twilio.version1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twilio.sdk.TwilioRestException;

public class TriggerWebNotification {
	private static final int medValue = 72;
	private static int centerValue = 72;
	private static final int delta = 5;
	private static int lastValue;
	private static final int MAX_ENTRIES = 5;
	
	private static LinkedHashMap<String,Boolean> sensorMap = new LinkedHashMap<String,Boolean>(MAX_ENTRIES + 1, .75F, false) {
		protected boolean removeEldestEntry(Map.Entry  eldest) {
            return size() >  MAX_ENTRIES;
         }
      };
	
	private static LinkedHashMap<Integer,Integer> weightMap = new LinkedHashMap<Integer,Integer>(MAX_ENTRIES + 1, .75F, false) {
		protected boolean removeEldestEntry(Map.Entry  eldest) {
            return size() >  MAX_ENTRIES;
         }
      };
    
	public static void process(String value, String sensorID) throws Exception{
		if(!sensorMap.containsKey(sensorID)){
			System.out.println("Adding Sensor ID: " + sensorID);
			sensorMap.put(sensorID, true);
		}
		
		if(criticalEstimate(Integer.parseInt(value)) && sensorMap.get(sensorID)){
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
		  System.out.println("Made Emergency Call for Sensor ID: " + sensorID);
		  sensorMap.put(sensorID, false);
		}
	}
	
	private static boolean criticalEstimate(int heartRate) {
		int difference = heartRate - medValue;
		
		if(weightMap.containsKey(heartRate)){
			int val = weightMap.get(heartRate) + 1;
			weightMap.remove(heartRate);
			weightMap.put(heartRate, val);
			if(((weightMap.get(heartRate) > 1) && (heartRate >= lastValue) && getSeries(heartRate)) || (Math.abs(centerValue - medValue) > 10)){
				System.out.println("Critical HeartRate: " + heartRate);
				return true;
			}
		}
		else{
			if(Math.abs(difference) >= delta){
				weightMap.put(heartRate, 1);
			}
		}
		centerValue = (centerValue + heartRate)/2 ;
		System.out.println("Current Weight Map: " + weightMap);
		System.out.println("No Critical HeartRate! Current Centroid: " + centerValue + "Current Heart Rate: " + heartRate);
		lastValue = heartRate;
		return false;
	}
	
	private static boolean getSeries(int heartRate){
		int count = 0;
		Set<Integer> keys = weightMap.keySet();
		for(int k : keys){
            if((heartRate - k) > 1){
            	count++;
            }
        }

		if(count > 2){
			return true;
		}
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
