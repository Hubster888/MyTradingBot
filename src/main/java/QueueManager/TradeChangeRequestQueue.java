package QueueManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.trade.TradeClose400RequestException;
import com.oanda.v20.trade.TradeClose404RequestException;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeCloseResponse;
import com.oanda.v20.trade.TradeSetDependentOrders400RequestException;
import com.oanda.v20.trade.TradeSetDependentOrdersRequest;
import com.oanda.v20.trade.TradeSetDependentOrdersResponse;
import com.oanda.v20.trade.TradeSpecifier;
import com.oanda.v20.transaction.StopLossDetails;
import com.oanda.v20.transaction.TakeProfitDetails;
import com.oanda.v20.transaction.TransactionID;

<<<<<<< HEAD
import Documenting.SendReport;
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import MyTradingBot.ConstantValues;

/**
 * This class is used to keep track of all the different trade requests.
 * The things this calls deals with include trades to cancel, change take profit 
 * and change stop loss.
 * */
public class TradeChangeRequestQueue {
	private static Queue<TradeSpecifier> tradesToCancel = new LinkedList<TradeSpecifier>();
	private static Queue<HashMap<TradeSpecifier,Double>> changeTakeProfitQueue = new LinkedList<HashMap<TradeSpecifier,Double>>();
	private static Queue<HashMap<TradeSpecifier,Double>> changeStopLossQueue = new LinkedList<HashMap<TradeSpecifier,Double>>();
	private final static AccountID accountId = ConstantValues.getAccountId();
	private static final String url = ConstantValues.getURL();
	private static final String accessToken = ConstantValues.getAccessToken();
	private final static Context ctx = new ContextBuilder(url)
    		.setToken(accessToken)
    		.setApplication("MyTradingBot")
    		.build();
	
	/**
	 * An empty constructor
	 * */
	public TradeChangeRequestQueue() {}
	
	/**
	 * @return true if the queue is empty
	 * */
	public Boolean tradesToCancelIsEmpty() {
		return tradesToCancel.isEmpty();
	}
	
	/**
	 * @return true if the queue is empty
	 * */
	public Boolean changeTakeProfitQueueIsEmpty() {
		return changeTakeProfitQueue.isEmpty();
	}
	
	/**
	 * @return true if queue is empty
	 * */
	public Boolean changeStopLossQueueIsEmpty() {
		return changeStopLossQueue.isEmpty();
	}
	
	/**
	 * @return the trade specifier of the trade that is to be cancelled next
	 * */
	private TradeSpecifier getNextCancel() {
		return tradesToCancel.poll();
	}
	
	/**
	 * @return A hash map with the values of the trade to modify and a value to modify it with
	 * */
	private HashMap<TradeSpecifier,Double> getNextTakeProfit(){
		return changeTakeProfitQueue.poll();
	}
	
	/**
	 * @return A hash map with the values of the trade to modify and a value to modify it with
	 * */
	private HashMap<TradeSpecifier,Double> getNextStopLoss(){
		return changeStopLossQueue.poll();
	}
	
	/**
	 * @param the trade specifier of the trade to cancel
	 * @return true if the trade is added to the queue
	 * */
	public static Boolean addCloseTradeToQueue(TradeSpecifier tradeSpecifier) {
		return tradesToCancel.add(tradeSpecifier);
	}
	
	/**
	 * @param the specifier of the trade to be modified
	 * @param price the to use when modifying the trade
	 * @return true if the change is added to the queue
	 * */
	public Boolean addTakeProfitToQueue(TradeSpecifier tradeSpecifier, Double price) {
		HashMap<TradeSpecifier, Double> map = new HashMap<TradeSpecifier,Double>();
		map.put(tradeSpecifier, price);
		return changeTakeProfitQueue.add(map);
	}
	
	/**
	 * @param the specifier of the trade to be modified
	 * @param price the to use when modifying the trade
	 * @return true if the change is added to the queue
	 * */
	public Boolean addStopLossToQueue(TradeSpecifier tradeSpecifier, Double price) {
		HashMap<TradeSpecifier, Double> map = new HashMap<TradeSpecifier,Double>();
		map.put(tradeSpecifier, price);
		return changeStopLossQueue.add(map);
	}
	
	/**
	 * @return true if the trade is cancelled correctly
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * @throws TradeClose404RequestException 
	 * @throws TradeClose400RequestException 
	 * */
	public Boolean executeCancel() throws TradeClose400RequestException, TradeClose404RequestException, RequestException, ExecuteException {
		TradeSpecifier specifier =  getNextCancel();
		if(tradeSpecifierIsValid(specifier)) {
			TradeCloseRequest request = new TradeCloseRequest(accountId, specifier);
			TradeCloseResponse response = ctx.trade.close(request);
			TransactionID transactionId = response.getLastTransactionID();
			if(changeStopLossQueueIsEmpty()/*TODO OrderManager.getLatestID == transactionId*/) {
				sendNotification(transactionId);
				return true;
			}
			return true;
		}else {
			SendReport.addError("the trade specifier is not valid | executeCancel() | TradeChangeRequestQueue"); 
			return false;
		}
	}
	
	/**
	 * @return true if the stop loss is changed correctly
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * @throws TradeSetDependentOrders400RequestException 
	 * */
	public Boolean executeStopLoss() throws TradeSetDependentOrders400RequestException, RequestException, ExecuteException { 
		HashMap<TradeSpecifier,Double> map = getNextStopLoss();
		TradeSpecifier specifier = (TradeSpecifier) map.keySet().toArray()[0];
		Double price = map.get(specifier);
		if(valuesAreValid(specifier, price)) {
			TradeSetDependentOrdersRequest request = new TradeSetDependentOrdersRequest(accountId, specifier)
					.setStopLoss(new StopLossDetails()
							.setGuaranteed(true)
							.setPrice(price));
			TradeSetDependentOrdersResponse response = ctx.trade.setDependentOrders(request);
			TransactionID transactionId = response.getLastTransactionID();
			if(changeStopLossQueueIsEmpty()/*TODO OrderManager.getLatestID == transactionId*/) {
				sendNotification(transactionId);
				return true;
			}else {
				return false;
			}
		}else {
			SendReport.addError("the values are not valid | executeStopLoss() | TradeChangeRequestQueue"); 
			return false;
		}
	}
	
	/**
	 * @return true if the take profit is changed correctly
	 * @throws ExecuteException 
	 * @throws RequestException 
	 * @throws TradeSetDependentOrders400RequestException 
	 * */
	public Boolean executeTakeProfit() throws TradeSetDependentOrders400RequestException, RequestException, ExecuteException { 
		HashMap<TradeSpecifier,Double> map = getNextTakeProfit();
		TradeSpecifier specifier = (TradeSpecifier) map.keySet().toArray()[0];
		Double price = map.get(specifier);
		if(valuesAreValid(specifier, price)) {
			TradeSetDependentOrdersRequest request = new TradeSetDependentOrdersRequest(accountId, specifier)
					.setTakeProfit(new TakeProfitDetails()
							.setPrice(price));
			TradeSetDependentOrdersResponse response = ctx.trade.setDependentOrders(request);
			TransactionID transactionId = response.getLastTransactionID();
			if(changeStopLossQueueIsEmpty()/*TODO OrderManager.getLatestID == transactionId*/) {
				sendNotification(transactionId);
				return true;
			}else {
				return false;
			}
		}else {
			SendReport.addError("the values are not valid | executeStopLoss() | TradeChangeRequestQueue"); 
			return false;
		}
	}
	
	/**
	 * @param the specifier to be checked
	 * @param the price to be checked
	 * @return true if both values are valid
	 * */
	private Boolean valuesAreValid(TradeSpecifier specifier, Double price) { //TODO find better way to verify
		if(specifier != null && price != 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * @param the transactionID of the trade change
	 * */
	private void sendNotification(TransactionID transactionId) {
		System.out.println("Method is not implemented");
		//TODO implement method
	}
	
	/**
	 * @param the specifier to be tested
	 * @return true if the specifier is valid
	 * */
	private Boolean tradeSpecifierIsValid(TradeSpecifier specifier) { //TODO find better way to verify
		if(specifier != null) {
			return true;
		}else {
			return false;
		}
	}
}
