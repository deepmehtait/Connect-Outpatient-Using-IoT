package lambda_algorithm_twilio.version1;

import com.twilio.sdk.TwilioRestException;

public class TriggerTwilio {
	public static void process(String record){
		try {
			if(record.equalsIgnoreCase("72")){
			TwilioCall twilioCall = new TwilioCall();
	        try {
				twilioCall.callTwilio("+14087149328");
			} catch (TwilioRestException e) {
				System.out.println("Twilio Call Exception");
				e.printStackTrace();
			}
	        
	        TwilioSMS twilioSms = new TwilioSMS();
	        try {
				twilioSms.messageTwilio("+14087149328");
			} catch (TwilioRestException e) {
				System.out.println("Twilio Message Exception");
				e.printStackTrace();
			}
			}
		}
		catch (Exception e) {
			System.out.println("Data Value Exception");
	        e.printStackTrace();
	}
}
}
