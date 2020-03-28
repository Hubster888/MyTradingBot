package QueueManager;

<<<<<<< HEAD
import java.util.Date;
=======
import java.io.IOException;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
<<<<<<< HEAD
import com.oanda.v20.account.AccountChangesRequest;
import com.oanda.v20.account.AccountChangesResponse;
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import com.oanda.v20.account.AccountContext;
import com.oanda.v20.account.AccountGetResponse;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.order.LimitOrder;
import com.oanda.v20.order.LimitOrderRequest;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.Order;
import com.oanda.v20.order.OrderCreate400RequestException;
import com.oanda.v20.order.OrderCreate404RequestException;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.order.OrderRequest;
import com.oanda.v20.order.OrderSpecifier;
<<<<<<< HEAD
import com.oanda.v20.order.OrderState;
import com.oanda.v20.order.OrderType;
import com.oanda.v20.position.Position;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.TradeSpecifier;
import com.oanda.v20.trade.TradeState;
import com.oanda.v20.trade.TradeSummary;

import Documenting.SendReport;
import MyTradingBot.ConstantValues;
=======
import com.oanda.v20.transaction.TransactionID;

import MyTradingBot.ConstantValues;
import Threads.TwitterThread;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7

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
<<<<<<< HEAD
=======
		try {
			setup();
		} catch (IOException e) {
			System.out.println("There is an error with the logg set up");
			e.printStackTrace();
		}
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
					SendReport.addOrderCreated(response.toString());
					return true;
=======
					TransactionID createTransactionId = response.getOrderCreateTransaction().getId();
					if(true/*TODO OrderManager.getLatestID == createTransactionId*/) { // Check if the order is added correctly and return true
						addToLog(createTransactionId);
						sendNotification(createTransactionId);
						return true;
					}
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
				}else {
					return false;
				}
			}catch(Exception e) {
<<<<<<< HEAD
				SendReport.addError(e.getMessage() + " " + new Date());
				e.printStackTrace();
				return false;
=======
				System.out.println("execute request failed");
				e.printStackTrace();
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
			}
			return false;
		}else { // If the order parameters are not valid, send a signal that the order is not added.
			System.out.println("the request is not valid | executeRequest() | OrderCreateRequestQueue " + requestedOrder.toString());
			SendReport.addError("the request is not valid | executeRequest() | OrderCreateRequestQueue " + requestedOrder.toString());
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
<<<<<<< HEAD
	 * @return true if it is safe to order
	 * */
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
		Boolean marginIsVeryBad = marginAvaliable < ConstantValues.getBadMargin();
		Boolean marginRateIsGood = response.getAccount().getMarginRate().doubleValue() <  0.4;
		if(marginIsVeryBad || !balanceIsCorrect) {
			emergencyExit(response);
		}
		if(balanceIsCorrect && notTooManyTrades && marginIsFine && marginRateIsGood) {
			return true;
		}
		System.out.println("it is not safe to order | OrderCreateRequestQueue numOfTrades: " + openTrades);
		SendReport.addError("it is not safe to order | OrderCreateRequestQueue numOfTrades: " + openTrades);
		return false;
	}
	
	private static void emergencyExit(AccountGetResponse response) throws RequestException, ExecuteException {
		List<TradeSummary> listOfTrades = response.getAccount().getTrades();
		for(TradeSummary trade : listOfTrades) {
			if(trade.getState().equals(TradeState.OPEN)) {
				TradeChangeRequestQueue.addCloseTradeToQueue(new TradeSpecifier(trade.getId()));
			}
		}
		List<Order> listOfOrders = response.getAccount().getOrders();
		for(Order order : listOfOrders) {
			if(order.getState().equals(OrderState.PENDING)) {
				OrderCancelRequestQueue.addToQueue(new OrderSpecifier(order.getId()));
			}
		}
		SendReport.addError("SYSTEM FAILED: The system triggered an emergency exit");
		System.exit(1);
=======
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
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
	}
	
	/**
	 * @param the order request to be checked
	 * @return true if the order type is valid
	 * */
	private Boolean RequestIsValid(OrderRequest orderRequest) {
		if(orderRequest != null && orderRequest.getType() != null) {
			return true;
		}else {
			System.out.println(orderRequest);
			return false;
		}
	}
	
<<<<<<< HEAD
	/**
	 * @param the order request to be executed
	 * @return true if the order is fine to continue
	 * */
	@SuppressWarnings("unused")
	private Boolean orderIsNotRepeated(OrderRequest request) {
		OrderType orderType = request.getType();
		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(ConstantValues.getLatestTransactionID());
		AccountChangesResponse accountResponse = null;
		try {
			accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
		} catch (RequestException | ExecuteException e) {
			SendReport.addError(e.getMessage() + " " + new Date());
			e.printStackTrace();
			return false;
		}
		
		if(orderType.equals(OrderType.MARKET)) {
			InstrumentName instrument = ((MarketOrderRequest) request).getInstrument();
			List<Position> listOfOpenPositions = accountResponse.getChanges().getPositions();
			for(Position position : listOfOpenPositions) {
				if(position.getInstrument().equals(instrument)) {
					SendReport.addError("The order instrument already exists in an open position: " );
					//return false;
				}
			}
		}else if(orderType.equals(OrderType.LIMIT)) {
			
			InstrumentName instrument = ((LimitOrderRequest) request).getInstrument(); 
			AccountContext accountCTX = new AccountContext(ConstantValues.getCtx());
			List<Order> listOfOrders = null;
			try {
				listOfOrders = accountCTX.get(ConstantValues.getAccountId()).getAccount().getOrders();
			} catch (RequestException | ExecuteException e) {
				SendReport.addError(e.getMessage());
				e.printStackTrace();
				return false;
			}
			for(Order order : listOfOrders) {
				if(order.getState().equals(OrderState.PENDING)) {
					LimitOrder limitOrder = (LimitOrder) order;
					if(!limitOrder.getInstrument().equals(instrument)) {
						SendReport.addError("The order already exists in the pending orders list");
						//return false;
					}
				}
			}
		}else {
			return true;
		}
		return true;
	}
=======
	static public void setup() throws IOException {
		logger.setUseParentHandlers(false);
		
        logger.setLevel(Level.INFO);
        FileHandler fileTxt = new FileHandler("orderLogger.txt");

        // create a TXT formatter
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
}
