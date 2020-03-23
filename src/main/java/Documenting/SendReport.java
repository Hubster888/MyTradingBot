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
	private AccountGetResponse response;
	private int numOfTweetsRecived;
	private int numOfTradesOpened;
	private int numOfTradesClosed;
	private Double NAVAtStartOfDay;
	private List<FailedTweet> listOfFailedTweets;
	private List<TradeSummary> listOfClosedTrades = new LinkedList<TradeSummary>();
	private TransactionID startOfDayID;
	private List<String> listOfErrors;
	private static Documentor documentor = new Documentor();
	
	public SendReport(AccountGetResponse response, int numOfTweetsRecived, int numOfTradesOpened, int numOfTradesClosed,
			Double NAVAtStartOfDay, List<FailedTweet> listOfFailedTweets, List<String> listOfErrors, TransactionID startOfDayID) {
		this.response = response;
		this.numOfTweetsRecived = numOfTweetsRecived;
		this.numOfTradesOpened = numOfTradesOpened;
		this.numOfTradesClosed = numOfTradesClosed;
		this.NAVAtStartOfDay = NAVAtStartOfDay;
		this.listOfFailedTweets = listOfFailedTweets;
		this.listOfErrors = documentor.getListOfErrors();
		this.startOfDayID = startOfDayID;
	}
	
	private void sendReport() throws RequestException, ExecuteException {
		this.listOfErrors = documentor.getListOfErrors();
		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(startOfDayID);
		AccountChangesResponse accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
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
		Double NAVAtEndOfTheDay = response.getAccount().getNAV().doubleValue();
		Double successRate = ((double)numOfTradesWithProfit) / numOfTradesClosed;
		StringBuilder title = new StringBuilder();
		title.append("The report for trading bot");
		
		StringBuilder stats = new StringBuilder();
		stats.append("\n Number of tweets recived: " + numOfTweetsRecived + "\n" +
		        "Number of trades created: " + numOfTradesOpened + "\n" +
				"Number of trades closed: " + numOfTradesClosed + "\n" +
		        "The profit/loss: " + (NAVAtStartOfDay - NAVAtEndOfTheDay) + "\n" +
				"The success rate: " + (successRate * 100) + "\n" +
		        "Number of orders cancelled: " + numOfOrdersCancelled);
		
		StringBuilder failedTweets = new StringBuilder();
		for(FailedTweet failedTweet : listOfFailedTweets) {
			failedTweets.append(failedTweet.FailedTweetToString());
		}
		
		StringBuilder errors = new StringBuilder();
		for(String error : listOfErrors) {
			errors.append(error);
		}
		
		StringBuilder closedTrades = new StringBuilder();
		closedTrades.append("List of closed trades");
		for(TradeSummary trade : listOfClosedTrades) {
			closedTrades.append(trade.toString());
		}
		
		StringBuilder mainMessage = new StringBuilder();
		mainMessage.append(title);
		mainMessage.append(stats);
		mainMessage.append(failedTweets);
		mainMessage.append(errors);
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
		    	 documentor.addError(e.getMessage());
		     }
		     //Reset values for next day
		     numOfTweetsRecived = 0;
		     numOfTradesOpened = 0;
		     numOfTradesClosed = 0;
		     NAVAtStartOfDay = response.getAccount().getNAV().doubleValue();
		     listOfFailedTweets = new LinkedList<FailedTweet>();
		     listOfErrors = new LinkedList<String>();
		     startOfDayID = response.getAccount().getLastTransactionID();
	}

	@Override
	public void run() {
		try {
			sendReport();
	       } catch (Exception ex) {
	    	   documentor.addError(ex.getMessage());
	           System.out.println("error running thread " + ex.getMessage());
	       }
		
	}
	
	

}
