package MyTradingBot;


import java.util.LinkedList;
import java.util.Timer;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountContext;
import com.oanda.v20.account.AccountGetResponse;
import com.oanda.v20.transaction.TransactionID;

import Documenting.FailedTweet;
import Documenting.SendReport;
import MyTradingBot.ConstantValues;
import Threads.QueueManagmentThread;
import Threads.TwitterThread;

/**
 * Hello world!
 *
 */
public class Main 
{
	
    public static void main(String[] args)
    {
    	
    	AccountGetResponse response = null;
    	AccountContext accountCtx = new AccountContext(ConstantValues.getCtx());
		try {
			response = accountCtx.get(ConstantValues.getAccountId());
		} catch (RequestException e) {
			e.printStackTrace();
		} catch (ExecuteException e) {
			e.printStackTrace();
		}
		TransactionID startOfDayID = response.getAccount().getLastTransactionID();
		@SuppressWarnings("unused")
		SendReport sendReport = new SendReport(response, 0, 0,
        		0, response.getAccount().getNAV().doubleValue(), new LinkedList<FailedTweet>(), new LinkedList<String>(), startOfDayID);
		
		
    	Timer time = new Timer(); // Instantiate Timer Object
		SendReport st = new SendReport(); // Instantiate SheduledTask class
		time.schedule(st, 0, 1000 * 60 * 60 * 2);
    	TwitterThread twitterThread = new TwitterThread();
    	QueueManagmentThread queueThread = new QueueManagmentThread();
        twitterThread.start();
        queueThread.start();   
    }
}
