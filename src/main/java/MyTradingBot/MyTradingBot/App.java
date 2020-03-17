package MyTradingBot.MyTradingBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountChangesRequest;
import com.oanda.v20.account.AccountChangesResponse;
import com.oanda.v20.trade.TradeSummary;
import com.oanda.v20.transaction.TransactionID;

import Threads.QueueManagmentThread;
import Threads.TwitterThread;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private final static Context ctx = new ContextBuilder("https://api-fxpractice.oanda.com")
    		.setToken("edbe715b87f077cbb4567a4a58f90421-90ac55c43f6c136f2194b70051ff103f")
    		.setApplication("MyTradingBot")
    		.build();
    public static void main(String[] args) throws RequestException, ExecuteException
    {
    	
    	TransactionID since = null;
    	TwitterThread twitterThread = new TwitterThread();
    	QueueManagmentThread queueThread = new QueueManagmentThread();
        twitterThread.start();
        queueThread.start();
        
        while(true) {
        	if(since == null) {
        		since = new TransactionID("1511");
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = ctx.account.changes(accountRequest);
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
        		AccountChangesResponse accountResponse = ctx.account.changes(accountRequest);
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
