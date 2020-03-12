package QueueManager;

import java.util.LinkedList;
import java.util.Queue;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.order.OrderCreate400RequestException;
import com.oanda.v20.order.OrderCreate404RequestException;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.order.OrderRequest;
import com.oanda.v20.transaction.TransactionID;

import MyTradingBot.MyTradingBot.ConstantValues;

/**
 * This class will hold and manage the queue that contains all
 * the requests to create new orders. This queue is constantly
 * checked if it is empty. If the queue is not empty
 * The requested orders will be executed.
 * */
public class OrderCreateRequestQueue {
	
	private static Queue<OrderRequest> queueOfCreateRequests = new LinkedList<OrderRequest>();
	private final static AccountID accountId = ConstantValues.getAccountId();
	private static final String url = ConstantValues.getURL();
	private static final String accessToken = ConstantValues.getAccessToken();
	private final static Context ctx = new ContextBuilder(url)
    		.setToken(accessToken)
    		.setApplication("MyTradingBot")
    		.build();
	
	/**
	 * Empty constructor
	 * */
	public OrderCreateRequestQueue() {	}
	
	/**
	 * @return true if the queue is empty
	 * */
	public Boolean isEmpty() {
		return queueOfCreateRequests.isEmpty();
	}
	
	/**
	 * @return gets the next OrderRequest in the queue
	 * */
	private OrderRequest getNext() {
		return queueOfCreateRequests.poll();
	}
	
	/**
	 * @param orderRequset that should be added to the queue
	 * @return true if the orderRequest is successfully added.
	 * */
	public Boolean addToQueue(OrderRequest orderRequest) {
		return queueOfCreateRequests.add(orderRequest);
	}
	
	/**
	 * @return true if the request is executed correctly
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * @throws OrderCreate404RequestException 
	 * @throws OrderCreate400RequestException 
	 * */
	public Boolean executeRequest() throws OrderCreate400RequestException, OrderCreate404RequestException, RequestException, ExecuteException { 
		OrderRequest requestedOrder = getNext();
		if(RequestIsValid(requestedOrder)) { // If all the order parameters are valid then create the order.
			OrderCreateRequest createRequest = new OrderCreateRequest(accountId)
					.setOrder(requestedOrder);
			OrderCreateResponse response = ctx.order.create(createRequest);
			TransactionID createTransactionId = response.getOrderCreateTransaction().getId();
			if(isEmpty()/*TODO OrderManager.getLatestID == createTransactionId*/) { // Check if the order is added correctly and return true
				addToLog(createTransactionId);
				sendNotification(createTransactionId);
				return true;
			}else {
				return false;
			}
		}else { // If the order parameters are not valid, send a signal that the order is not added.
			return false;
		}
	}
	
	/**
	 * Creates a log entry that shows the created order info
	 * */
	private void addToLog(TransactionID transactionId) {
		System.out.println("This method is not implemented!!!");
		//TODO implement this method
	}
	
	/**
	 * Sends an email notification about the created order
	 * */
	private void sendNotification(TransactionID transactionId) {
		System.out.println("This method is not implemented!!");
		//TODO implement this method
	}
	
	/**
	 * @param the order request to be checked
	 * @return true if the order type is valid
	 * */
	private Boolean RequestIsValid(OrderRequest orderRequest) { //TODO find better way to verify
		if(orderRequest != null && orderRequest.getType() != null) {
			return true;
		}else {
			return false;
		}
	}
}
