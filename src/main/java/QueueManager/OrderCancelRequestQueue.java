package QueueManager;

import java.util.LinkedList;
import java.util.Queue;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.order.OrderCancel404RequestException;
import com.oanda.v20.order.OrderCancelResponse;
import com.oanda.v20.order.OrderSpecifier;
import com.oanda.v20.transaction.TransactionID;

<<<<<<< HEAD
import Documenting.SendReport;
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import MyTradingBot.ConstantValues;

/**
 * This class will hold and manage the queue that contains all
 * the requests to cancel orders. This queue is constantly
 * checked if it is empty. If the queue is not empty
 * The requested orders will be executed.
 * */
public class OrderCancelRequestQueue {
	private static Queue<OrderSpecifier> queueOfCancelRequests = new LinkedList<OrderSpecifier>();
	private final static AccountID accountId = ConstantValues.getAccountId();
	private static final String url = ConstantValues.getURL();
	private static final String accessToken = ConstantValues.getAccessToken();
	private final static Context ctx = new ContextBuilder(url)
    		.setToken(accessToken)
    		.setApplication("MyTradingBot")
    		.build();
	
	public OrderCancelRequestQueue() {}
	
	/**
	 * @return returns true if queue is empty
	 * */
	public Boolean isEmpty() {
		return queueOfCancelRequests.isEmpty();
	}
	
	/**
	 * @return returns true if the order has been cancelled correctly
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * @throws OrderCancel404RequestException 
	 * */
	public Boolean executeCancel() throws OrderCancel404RequestException, RequestException, ExecuteException { 
		OrderSpecifier specifier = getNextOrder();
		if(specifierIsValid(specifier)) {
			OrderCancelResponse response = ctx.order.cancel(accountId, specifier);
			TransactionID closingTransactionId = response.getOrderCancelTransaction().getId();
			if(isEmpty()/*TODO OrderManager.getLatestID == closingTransactionId*/) {
				sendNotification(closingTransactionId);
				return true;
			}
			return true;
		}else {
			SendReport.addError("the specifier is not valid | in the method executeCancel() | OrderCancelRequestQueue");
			return false;
		}
	}
	
	/**
	 * @param the specifier to test
	 * @return true if the specifier is valid
	 * */
	private Boolean specifierIsValid(OrderSpecifier specifier) { //TODO find better way to verify
		if(specifier != null) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *@param the transactionID of the closed order
	 * */
	private void sendNotification(TransactionID transactionId) {
		System.out.println("This method is not implemented!!");
		//TODO implement this method
	}
	
	/**
	 * @param the order specifier of the order that will be cancelled
	 * @return returns true if the order specifier is adds correctly
	 * */
	public static Boolean addToQueue(OrderSpecifier orderSpecifier) {
		return queueOfCancelRequests.add(orderSpecifier);
	}
	
	/**
	 * @return the order specifier that is next in the queue
	 * */
	private OrderSpecifier getNextOrder() {
		return queueOfCancelRequests.poll();
	}
	
	
}
