package Documenting;

import java.util.Date;

public class FailedTweet {
	private final String tweet;
	private final String reason;
	private final Date dateOfTweet;
	
	public FailedTweet(String tweet, String reason) {
		this.tweet = tweet;
		this.reason = reason;
		this.dateOfTweet = new Date();
	}
	
	public String FailedTweetToString() {
		return "The following tweet has failed because: \n" +
	this.reason + "\n" + this.tweet + "\n" + this.dateOfTweet;
	}
}
