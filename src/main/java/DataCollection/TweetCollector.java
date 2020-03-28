package DataCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Documenting.SendReport;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This is the tweet collector class that is called multiple times a minute to 
 * check if there is a new tweet. It then takes the tweets content and passes it back
 * to the thread that called it.
 * */
public class TweetCollector {

	private static final String API_KEY = "k6W4MFJuzAN5mqovWfkde3ttL";
	private static final String API_SECRET = "2nAk6J3DHu4IgwM24XqkZhUE71VGOn6AwGsps8KFHNivqiiWvV";
	private static final String ACCESS_TOKEN = "1229369724796129281-tv4PYNHjmPhphIynot1cWgTUa4A7bL";
	private static final String ACCESS_TOKEN_SECRET = "wV6FdTeeLFM1YMIe4bDSl5FemHY1UipYw8Dqo8rGMcPoz";
	private static Twitter twitter = new TwitterFactory(new ConfigurationBuilder()
			.setOAuthConsumerKey(API_KEY)
			.setOAuthConsumerSecret(API_SECRET)
			.setOAuthAccessToken(ACCESS_TOKEN)
			.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
			.setTweetModeExtended(true)
			.build())
			.getInstance();
	private static Date since = null;

	public TweetCollector() {
	}

	/**
	 * This method returns list of Tweets based on a search term
	 * @param the string to search tweets by
	 * @return a list of the resulted tweets.
	 * */
	public List<Status> getTweetsBySearchTerm(String searchTerm) {
		Query query = new Query(String.format("-filter:retweets -filter:images %s", searchTerm));
		query.count(100);
		query.resultType(Query.POPULAR);
		QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			SendReport.addError(e.getMessage());
			e.printStackTrace();
		}
		return result.getTweets();
	}

	/**
	 * @return The tweets content along with some other data
	 * */
	public ArrayList<String> getUserTimeLine() throws TwitterException {
		String result = "Null";
		ArrayList<String> results = new ArrayList<String>();
		List<Status> statuses = new ArrayList<Status>();
		ArrayList<String> listOfUsers = new ArrayList<String>();
		//listOfUsers.add("@SignalFactory");
		//listOfUsers.add("@MXInvesting");
		listOfUsers.add("@EzpipsS");
		for(String userName : listOfUsers) {
			try {
				statuses = twitter.getUserTimeline(userName);
				for (Status status : statuses) {
					if(status.getCreatedAt().after(since)) {
						result = status.getUser().getName() + ":" +
								status.getText();
						if(!result.equals("Null")) {results.add(result);}
					}
				}
			}catch(Exception e) {
				SendReport.addError(e.getMessage());
				System.out.println(e.toString() + " ff");
				return null;
			}
		}
		return results;
	}

	public void setSince(Date date) {
		since = date;
	}
}
