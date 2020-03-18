package Documenting;

import java.util.Date;
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

import com.oanda.v20.account.AccountGetResponse;

public class SendReport extends TimerTask {
	private AccountGetResponse response;
	private int numOfTweetsRecived;
	private int numOfTradesOpened;
	private int numOfTradesClosed;
	private Double NAVAtStartOfDay;
	private List<FailedTweet> listOfFailedTweets;
	private List<String> listOfErrors;
	
	public SendReport(AccountGetResponse response, int numOfTweetsRecived, int numOfTradesOpened, int numOfTradesClosed,
			Double NAVAtStartOfDay, List<FailedTweet> listOfFailedTweets, List<String> listOfErrors) {
		this.response = response;
		this.numOfTweetsRecived = numOfTweetsRecived;
		this.numOfTradesOpened = numOfTradesOpened;
		this.numOfTradesClosed = numOfTradesClosed;
		this.NAVAtStartOfDay = NAVAtStartOfDay;
		this.listOfFailedTweets = listOfFailedTweets;
		this.listOfErrors = listOfErrors;
	}
	
	private void sendReport() {
		Double NAVAtEndOfTheDay = response.getAccount().getNAV().doubleValue();
		StringBuilder title = new StringBuilder();
		title.append("The report for trading bot");
		
		StringBuilder stats = new StringBuilder();
		stats.append("\n Number of tweets recived: " + numOfTweetsRecived + "\n" +
		        "Number of trades created: " + numOfTradesOpened + "\n" +
				"Number of trades closed: " + numOfTradesClosed + "\n" +
		        "The change in NAV: " + NAVAtStartOfDay + " to " + NAVAtEndOfTheDay);
		
		StringBuilder failedTweets = new StringBuilder();
		for(FailedTweet failedTweet : listOfFailedTweets) {
			failedTweets.append(failedTweet.FailedTweetToString());
		}
		
		StringBuilder errors = new StringBuilder();
		for(String error : listOfErrors) {
			errors.append(error);
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
		    	 
		     }
	}

	@Override
	public void run() {
		try {
			sendReport();
	       } catch (Exception ex) {
	           System.out.println("error running thread " + ex.getMessage());
	       }
		
	}
	
	

}
