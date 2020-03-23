package QueueManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.order.OrderReplace400RequestException;
import com.oanda.v20.order.OrderReplace404RequestException;
import com.oanda.v20.order.OrderReplaceRequest;
import com.oanda.v20.order.OrderReplaceResponse;
import com.oanda.v20.order.OrderRequest;
import com.oanda.v20.order.OrderSpecifier;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.transaction.TransactionID;

import Documenting.Documentor;
import MyTradingBot.ConstantValues;

/**
 * This class will hold and manage the queue that contains all
 * the requests to modify orders. This queue is constantly
 * checked if it is empty. If the queue is not empty
 * The requested changes will be made.
 * */
public class OrderChangeRequestQueue {
	private static Queue<HashMap<OrderSpecifier,OrderRequest>> queueOfChangesForOrders = new LinkedList<HashMap<OrderSpecifier,OrderRequest>>();
	private final static AccountID accountId = ConstantValues.getAccountId();
	private static final String url = ConstantValues.getURL();
	private static final String accessToken = ConstantValues.getAccessToken();
	private final static Context ctx = new ContextBuilder(url)
			.setToken(accessToken)
			.setApplication("MyTradingBot")
			.build();
	private static Documentor documentor = new Documentor();
	
	/**
	 * An empty constructor
	 * */
	public OrderChangeRequestQueue() {}

	/**
	 * @return true if the queue is empty
	 * */
	public Boolean isEmpty() {
		return queueOfChangesForOrders.isEmpty();
	}

	/**
	 * @return true if the change is executed correctly
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * @throws OrderReplace404RequestException 
	 * @throws OrderReplace400RequestException 
	 * */
	public Boolean executeChange() throws OrderReplace400RequestException, OrderReplace404RequestException, RequestException, ExecuteException { 
		HashMap<OrderSpecifier,OrderRequest> theChange = getNext();
		OrderSpecifier specifier = (OrderSpecifier) theChange.keySet().toArray()[0];
		OrderRequest order = theChange.get(specifier);
		if(changeIsValid(specifier, order)) {
			OrderReplaceRequest replaceRequest = new OrderReplaceRequest(accountId, specifier);
			replaceRequest.setOrder(order);
			OrderReplaceResponse response = ctx.order.replace(replaceRequest);
			TransactionID transactionId = response.getLastTransactionID();
			if(isEmpty() /*TODO OrderManager.getLatestID == transactionId*/) {
				sendNotification(transactionId);
				return true;
			}else {
				return false;
			}
		}else {
			documentor.addError("the change is not valid | in executeChange | OrderChangeRequestQueue");
			return false;
		}
	}

	/**
	 * @param specifier the order to be replaced
	 * @param request the order to replace
	 * @return true if the values are valid
	 * */
	private Boolean changeIsValid(OrderSpecifier specifier, OrderRequest request) { //TODO find better way to verify
		if(specifier != null && request != null) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @param the transactionId of the change
	 * */
	private void sendNotification(TransactionID transactionId) {
		System.out.println("This method is not implemented");
		//TODO implement method
	}

	/**
	 * @param the Order specifier and order request to add
	 * @return true if the element is added successfully
	 * */
	public Boolean addToQueue(HashMap<OrderSpecifier, OrderRequest> hashMapOfSpecifierAndRequest) {
		return queueOfChangesForOrders.add(hashMapOfSpecifierAndRequest);
	}

	/**
	 * @return the next change to be made in the form of a hashMap
	 * */
	private HashMap<OrderSpecifier,OrderRequest> getNext(){
		return queueOfChangesForOrders.poll();
	}

	public static void recordClosedTrade(Trade trade) throws IOException {
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
