package lambda_algorithm_twilio.version1;

public class KinesisAdmin {

	private String streamName;
	private int shardCount;

	public KinesisAdmin(String streamName, int shardCount) {
		this.setStreamName(streamName);
		this.setShardCount(shardCount);
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public int getShardCount() {
		return shardCount;
	}

	public void setShardCount(int shardCount) {
		this.shardCount = shardCount;
	}
}
