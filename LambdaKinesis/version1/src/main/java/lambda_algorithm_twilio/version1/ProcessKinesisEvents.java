package lambda_algorithm_twilio.version1;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;

public class ProcessKinesisEvents {
    public void recordHandler(KinesisEvent event) throws IOException {
        for(KinesisEventRecord rec : event.getRecords()) {
//            JSONObject obj = new JSONObject(rec.getKinesis().getData().array());
            try{
            	System.out.println("Value Received: " + new String(rec.getKinesis().getData().array()));
            	TriggerWebNotification.process(new String(rec.getKinesis().getData().array()), new String(rec.getKinesis().getPartitionKey().toString()));
            	
            }catch(Exception e){
            	System.out.println("Error Kinesis Event Processing: " + new String(rec.getKinesis().getData().array()));
            }
        }
    }
}