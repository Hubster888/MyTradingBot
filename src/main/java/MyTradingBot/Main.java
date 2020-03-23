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

import Documenting.Documentor;
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
    	Documentor documentor = new Documentor();
    	TransactionID since = null;
    	TwitterThread twitterThread = new TwitterThread();
    	QueueManagmentThread queueThread = new QueueManagmentThread();
        twitterThread.start();
        queueThread.start();
        
        while(true) {
        	if(since == null) {
        		since = new TransactionID("1000");
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = null;
				try {
					accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
				} catch (RequestException | ExecuteException e1) {
					documentor.addError(e1.getMessage());
					e1.printStackTrace();
				}
        		since = accountResponse.getLastTransactionID();
        		List<TradeSummary> closedTrades = accountResponse.getChanges().getTradesClosed();
        		for(TradeSummary trade : closedTrades) {
        			try {
						recordClosedTrade(trade);
					} catch (IOException e) {
						documentor.addError(e.getMessage());
						e.printStackTrace();
					}
        		}
        	}else {
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = null;
				try {
					accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
				} catch (RequestException | ExecuteException e1) {
					documentor.addError(e1.getMessage());
					e1.printStackTrace();
				}
        		since = accountResponse.getLastTransactionID();
        		List<TradeSummary> closedTrades = accountResponse.getChanges().getTradesClosed();
        		for(TradeSummary trade : closedTrades) {
        			try {
						recordClosedTrade(trade);
					} catch (IOException e) {
						documentor.addError(e.getMessage());
						e.printStackTrace();
					}
        		}
        	}
        	
        	
        	try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				documentor.addError(e.getMessage());
				e.printStackTrace();
			}
        }
        
    }
    
    public static void recordClosedTrade(TradeSummary trade) throws IOException {
    	Documentor documentor = new Documentor();
	    File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\closedTradesStorage.txt");
	    FileWriter fr = new FileWriter(file, true);
		try {
			fr.write(trade.toString() + "\n");
			fr.close();
		} catch (IOException e) {
			documentor.addError(e.getMessage());
			e.printStackTrace();
		}
	}
}
