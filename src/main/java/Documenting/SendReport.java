package Documenting;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountChangesRequest;
import com.oanda.v20.account.AccountChangesResponse;
import com.oanda.v20.account.AccountGetResponse;
import com.oanda.v20.trade.TradeSummary;
import com.oanda.v20.transaction.TransactionID;

import MyTradingBot.ConstantValues;

public class SendReport extends TimerTask {
	private static AccountGetResponse response;
	private static int numOfTweetsRecived;
	private static int numOfTradesOpened;
	private static int numOfTradesClosed;
	private static List<FailedTweet> listOfFailedTweets;
	private static List<TradeSummary> listOfClosedTrades = new LinkedList<TradeSummary>();
	private static TransactionID startOfDayID;
	private static List<String> listOfErrors = new LinkedList<String>();
	private static List<String> listOfOrdersCreated = new LinkedList<String>();
	
	public SendReport(AccountGetResponse response, int numOfTweetsRecived, int numOfTradesOpened, int numOfTradesClosed,
			Double NAVAtStartOfDay, List<FailedTweet> listOfFailedTweets, List<String> listOfErrors, TransactionID startOfDayID) {
		SendReport.response = response;
		SendReport.numOfTweetsRecived = numOfTweetsRecived;
		SendReport.numOfTradesOpened = numOfTradesOpened;
		SendReport.numOfTradesClosed = numOfTradesClosed;
		SendReport.listOfFailedTweets = getListOfFailedTweets();
		SendReport.listOfErrors = getListOfErrors();
		SendReport.startOfDayID = startOfDayID;
		SendReport.listOfOrdersCreated = getListOfOrdersCreated();
	}
	
	public SendReport() {}
	
	private static void sendReport(){
		SendReport.listOfErrors = getListOfErrors();
		SendReport.listOfOrdersCreated = getListOfOrdersCreated();
		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(startOfDayID);
		AccountChangesResponse accountResponse = null;
		try {
			accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
		} catch (RequestException | ExecuteException e1) {
			e1.printStackTrace();
		}
		listOfClosedTrades = accountResponse.getChanges().getTradesClosed();
		int numOfOrdersCancelled = accountResponse.getChanges().getOrdersCancelled().size();
		numOfTradesClosed = listOfClosedTrades.size();
		numOfTradesOpened = accountResponse.getChanges().getTradesOpened().size();
		int numOfTradesWithProfit = 0;
		for(TradeSummary trade : listOfClosedTrades) {
			if(trade.getRealizedPL().doubleValue() > 0) {
				numOfTradesWithProfit++;
			}
		}
		Double successRate = ((double)numOfTradesWithProfit) / numOfTradesClosed;
		StringBuilder title = new StringBuilder();
		title.append("The report for trading bot");
		
		StringBuilder stats = new StringBuilder();
		stats.append("\n Number of tweets recived: " + numOfTweetsRecived + "\n" +
		        "Number of trades created: " + numOfTradesOpened + "\n" +
				"Number of trades closed: " + numOfTradesClosed + "\n" +
				"The success rate: " + (successRate * 100) + "\n" +
		        "Number of orders cancelled: " + numOfOrdersCancelled);
		
		StringBuilder errors = new StringBuilder();
		errors.append("List of errors--------------------------------------------------------- \n");
		if(listOfErrors != null) {
			for(String error : listOfErrors) {
				errors.append(error + "\n");
			}
		}
		
		StringBuilder ordersCreated = new StringBuilder();
		ordersCreated.append("Orders Created ------------------------------------");
		if(listOfOrdersCreated != null) {
			for(String order : listOfOrdersCreated) {
				ordersCreated.append(order);
			}
		}
		
		
		
		StringBuilder closedTrades = new StringBuilder();
		closedTrades.append("\n" + "List of closed trades" + "\n");
		for(TradeSummary trade : listOfClosedTrades) {
			closedTrades.append(trade.toString() + "\n");
		}
		
		StringBuilder mainMessage = new StringBuilder();
		mainMessage.append(title);
		mainMessage.append(stats);
		mainMessage.append(errors);
		mainMessage.append(ordersCreated);
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		  // Get a Properties object
		     Properties props = System.getProperties();
		     props.setProperty("mail.smtp.host", "smtp.gmail.com");
		     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		     props.setProperty("mail.smtp.socketFactory.fallback", "false");
		     props.setProperty("mail.smtp.port", "465");
		     props.setProperty("mail.smtp.socketFactory.port", "465");
		     props.put("mail.smtp.auth", "true");
		     props.put("mail.debug", "true");
		     props.put("mail.store.protocol", "pop3");
		     props.put("mail.transport.protocol", "smtp");
		     final String username = "rzeminski16@gmail.com";//
		     final String password = "Hubert56";
		     try{
		     Session session = Session.getDefaultInstance(props, 
		                          new Authenticator(){
		                             protected PasswordAuthentication getPasswordAuthentication() {
		                                return new PasswordAuthentication(username, password);
		                             }});

		   // -- Create a new message --
		     Message msg = new MimeMessage(session);

		  // -- Set the FROM and TO fields --
		     msg.setFrom(new InternetAddress("rzeminski16@gmail.com"));
		     msg.setRecipients(Message.RecipientType.TO, 
		                      InternetAddress.parse("rzeminski16@gmail.com",false));
		     msg.setSubject("Trading bot report");
		     msg.setText(mainMessage.toString());
		     msg.setSentDate(new Date());
		     Transport.send(msg);
		     System.out.println("Message sent.");
		     }catch(Exception e) {
		    	 addError(e.getMessage());
		     }
		     //Reset values for next day
		     numOfTweetsRecived = 0;
		     numOfTradesOpened = 0;
		     numOfTradesClosed = 0;
		     listOfFailedTweets = new LinkedList<FailedTweet>();
		     listOfErrors = new LinkedList<String>();
		     startOfDayID = response.getAccount().getLastTransactionID();
		     System.out.println("Email sent!");
	}

	@Override
	public void run() {
		
		sendReport();
	       
		
	}
	
	/**
	 * Adds to the tweet count
	 * */
	public static void addTweetRecived() {
		numOfTweetsRecived++;
	}

	/**
	 * Adds to opened trade count
	 * */
	public void addTradeOpened() {
		numOfTradesOpened++;
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
	public static void addError(String error) {
		listOfErrors.add(error);
	}
	
	/**
	 * @return the list of errors
	 * */
	public static List<String> getListOfErrors(){
		return listOfErrors;
	}
	
	public static List<String> getListOfOrdersCreated(){
		return listOfOrdersCreated;
	}
	
	public static void addOrderCreated(String order) {
		listOfOrdersCreated.add(order);
	}
	
	public static List<FailedTweet> getListOfFailedTweets(){
		return listOfFailedTweets;
	}
}
