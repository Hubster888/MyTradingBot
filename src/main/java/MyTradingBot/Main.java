package MyTradingBot;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountChangesRequest;
import com.oanda.v20.account.AccountChangesResponse;
import com.oanda.v20.trade.TradeSummary;
import com.oanda.v20.transaction.TransactionID;

import MyTradingBot.ConstantValues;
import Threads.QueueManagmentThread;
import Threads.TwitterThread;

/**
 * Hello world!
 *
 */
public class Main 
{
	
    public static void main(String[] args) throws RequestException, ExecuteException
    {
    	
    	TransactionID since = null;
    	TwitterThread twitterThread = new TwitterThread();
    	QueueManagmentThread queueThread = new QueueManagmentThread();
        twitterThread.start();
        queueThread.start();
        
        while(true) {
        	if(since == null) {
        		since = new TransactionID("1");
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
        		since = accountResponse.getLastTransactionID();
        		List<TradeSummary> closedTrades = accountResponse.getChanges().getTradesClosed();
        		for(TradeSummary trade : closedTrades) {
        			try {
						recordClosedTrade(trade);
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}else {
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
        		since = accountResponse.getLastTransactionID();
        		List<TradeSummary> closedTrades = accountResponse.getChanges().getTradesClosed();
        		for(TradeSummary trade : closedTrades) {
        			try {
						recordClosedTrade(trade);
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}
        	
        	
        	try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
    }
    
    public static void recordClosedTrade(TradeSummary trade) throws IOException {
	    
	    File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\closedTradesStorage.txt");
	    FileWriter fr = new FileWriter(file, true);
		try {
			fr.write(trade.toString() + "\n");
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
