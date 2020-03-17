package QueueManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountContext;
import com.oanda.v20.account.AccountGetResponse;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.order.OrderCreate400RequestException;
import com.oanda.v20.order.OrderCreate404RequestException;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.order.OrderRequest;
import com.oanda.v20.order.OrderSpecifier;
import com.oanda.v20.transaction.TransactionID;

import MyTradingBot.MyTradingBot.ConstantValues;
import Threads.TwitterThread;

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
	private static final Logger logger =
	        Logger.getLogger(TwitterThread.class.getName());
	
	/**
	 * Empty constructor
	 * */
	public OrderCreateRequestQueue() {
		try {
			setup();
		} catch (IOException e) {
			System.out.println("There is an error with the logg set up");
			e.printStackTrace();
		}
	}
	
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
	public static Boolean addToQueue(OrderRequest orderRequest) {
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
		if(RequestIsValid(requestedOrder)) {
			// If all the order parameters are valid then create the order.
			try {
				if(isSafeToOrder()) {
					OrderCreateRequest createRequest = new OrderCreateRequest(accountId)
							.setOrder(requestedOrder);
					OrderCreateResponse response = ctx.order.create(createRequest);
					TransactionID createTransactionId = response.getOrderCreateTransaction().getId();
					if(true/*TODO OrderManager.getLatestID == createTransactionId*/) { // Check if the order is added correctly and return true
						addToLog(createTransactionId);
						sendNotification(createTransactionId);
						return true;
					}
				}else {
					return false;
				}
			}catch(Exception e) {
				System.out.println("execute request failed");
				e.printStackTrace();
			}
			return false;
		}else { // If the order parameters are not valid, send a signal that the order is not added.
			return false;
		}
	}
	
	private static Boolean isSafeToOrder() throws RequestException, ExecuteException {
		AccountContext accountCtx = new AccountContext(ConstantValues.getCtx());
		AccountGetResponse response = accountCtx.get(ConstantValues.getAccountId());
		Double balance = response.getAccount().getBalance().doubleValue();
		Double unrealisedPNL = response.getAccount().getUnrealizedPL().doubleValue();
		Boolean balanceIsCorrect = (balance + unrealisedPNL) > ConstantValues.getMinBalance();
		
		Double openTrades = response.getAccount().getOpenTradeCount().doubleValue();
		Boolean notTooManyTrades = openTrades < ConstantValues.getMaxOpenTrades();
		
		Double marginAvaliable = response.getAccount().getMarginAvailable().doubleValue();
		Boolean marginIsFine = marginAvaliable > ConstantValues.getMinMargin();
		
		if(balanceIsCorrect && notTooManyTrades && marginIsFine) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param the transaction ID of the request
	 * Creates a log entry that shows the created order info
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * */
	private void addToLog(TransactionID transactionId) throws RequestException, ExecuteException {
		logger.log(Level.INFO, ctx.order.get(accountId, new OrderSpecifier(transactionId)).getOrder().toString());
	}
	
	/**
	 * @param the transaction ID of the request
	 * Sends an email notification about the created order
	 * */
	private void sendNotification(TransactionID transactionId) {
		//System.out.println("This method is not implemented!!");
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
			System.out.println(orderRequest);
			return false;
		}
	}
	
	static public void setup() throws IOException {
		logger.setUseParentHandlers(false);
		
        logger.setLevel(Level.INFO);
        FileHandler fileTxt = new FileHandler("orderLogger.txt");

        // create a TXT formatter
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
}
