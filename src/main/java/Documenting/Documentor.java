package Documenting;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountContext;
import com.oanda.v20.account.AccountGetResponse;

import MyTradingBot.ConstantValues;

public class Documentor {
	private static int numOfTweetsRecived;
	private static int numOfTradesOpened;
	private static int numOfTradesClosed;
	private static List<FailedTweet> listOfFailedTweets;
	private static List<String> listOfErrors;
	private static Double NAVAtStartOfDay;
	private static AccountGetResponse response;

	/**
	 * Creates a new documentor when the program starts
	 * */
	public Documentor() {
		numOfTweetsRecived = 0;
		numOfTradesOpened = 0;
		numOfTradesClosed = 0;
		listOfFailedTweets = new LinkedList<FailedTweet>();
		listOfErrors = new LinkedList<String>();
		AccountContext accountCtx = new AccountContext(ConstantValues.getCtx());
		try {
			response = accountCtx.get(ConstantValues.getAccountId());
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NAVAtStartOfDay = response.getAccount().getNAV().doubleValue();
		Timer time = new Timer(); // Instantiate Timer Object
		SendReport st = new SendReport(response, numOfTweetsRecived, numOfTradesOpened,
        		numOfTradesClosed, NAVAtStartOfDay, listOfFailedTweets, listOfErrors); // Instantiate SheduledTask class
		time.schedule(st, 0, 1000 * 60 * 60 * 12);
	}

	/**
	 * Adds to the tweet count
	 * */
	public void addTweetRecived() {
		numOfTweetsRecived++;
	}

	/**
	 * Adds to opened trade count
	 * */
	public void addTradeOpened() {
		numOfTradesOpened++;
	}

	/**
	 * Adds to trades closed count
	 * */
	public void addTradeClosed() {
		numOfTradesClosed++;
	}

	/**
	 * Adds to failed tweet list
	 * */
	public void addFailedTweet(FailedTweet failedTweet) {
		listOfFailedTweets.add(failedTweet);
	}

	/**
	 * Adds an error to the document
	 * */
	public void addError(String error) {
		listOfErrors.add(error);
	}

}

