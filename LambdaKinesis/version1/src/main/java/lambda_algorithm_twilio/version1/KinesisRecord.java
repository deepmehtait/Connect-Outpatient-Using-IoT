package lambda_algorithm_twilio.version1;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import lambda_algorithm_twilio.version1.KinesisAdmin;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.kinesis.model.Shard;

public class KinesisRecord {
	private static KinesisAdmin kinesisAdmin;
	private static AmazonKinesisClient kinesisClient;
	private static DescribeStreamRequest describeStreamRequest;
	private static String endRecord = "[End-of-Records]";
	private static String regionId = "us-east-1";
	private static String serviceName = "kinesis";
	private static String endpoint = "kinesis.us-east-1.amazonaws.com";
	public static void main(String[] args){
		initAccess();
		PutRecordsRequest putRecordsRequest  = new PutRecordsRequest();
		putRecordsRequest.setStreamName("testStream");
		List <PutRecordsRequestEntry> putRecordsRequestEntryList  = new ArrayList<PutRecordsRequestEntry>(); 
		for (int i = 0; i < 100; i++) {
		    PutRecordsRequestEntry putRecordsRequestEntry  = new PutRecordsRequestEntry();
		    putRecordsRequestEntry.setData(ByteBuffer.wrap(String.valueOf(i).getBytes()));
		    putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
		    putRecordsRequestEntryList.add(putRecordsRequestEntry); 
		}

		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		PutRecordsResult putRecordsResult  = kinesisClient.putRecords(putRecordsRequest);
		System.out.println("Put Result" + putRecordsResult);
	}
	public KinesisRecord(String streamName) {
		kinesisAdmin = new KinesisAdmin(streamName, 2);
		kinesisAdmin.setStreamName(streamName);
	}
	
	@SuppressWarnings("deprecation")
	public static void initAccess() {
		// These are for checking the credentials
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Check the Credentials file Location", e);
		}

		kinesisClient = new AmazonKinesisClient(credentials);
		kinesisClient.setEndpoint(endpoint, serviceName, regionId);
	}
	
	public String getRecordsFromStream() {
		// Read the records from the stream shards
		List<Shard> shards = new ArrayList<Shard>();
		String exclusiveStartShardId = null;
	
		do {
			describeStreamRequest =  new DescribeStreamRequest().withStreamName(kinesisAdmin.getStreamName());
			describeStreamRequest.setExclusiveStartShardId(exclusiveStartShardId);
			DescribeStreamResult describeStreamResult = kinesisClient.describeStream(describeStreamRequest);
			shards.addAll(describeStreamResult.getStreamDescription().getShards());
			if (describeStreamResult.getStreamDescription().getHasMoreShards() && shards.size() > 0) {
				exclusiveStartShardId = shards.get(shards.size() - 1).getShardId();
			} else {
				exclusiveStartShardId = null;
			}
		} while (exclusiveStartShardId != null);

		String shardIterator;
		GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
		getShardIteratorRequest.setStreamName(kinesisAdmin.getStreamName());
		getShardIteratorRequest.setShardId(shards.get(0).getShardId());
		getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");
		GetShardIteratorResult getShardIteratorResult = kinesisClient.getShardIterator(getShardIteratorRequest);
		shardIterator = getShardIteratorResult.getShardIterator();

		List<Record> records = null;
		while (shardIterator != null) {
			GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
			getRecordsRequest.setShardIterator(shardIterator);
			getRecordsRequest.setLimit(1000);
			GetRecordsResult result = kinesisClient.getRecords(getRecordsRequest);
			// Put result into record list. Result may be empty.
			records = result.getRecords();

			if (records.size() == 0) {
				return endRecord;
			}

			try {
				Thread.sleep(1000);
				System.out.println(records.toString());
			} catch (InterruptedException exception) {
				throw new RuntimeException(exception);
			}

			shardIterator = result.getNextShardIterator();

		}
		return records.toString();
	}
}
