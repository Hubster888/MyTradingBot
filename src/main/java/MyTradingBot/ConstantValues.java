package MyTradingBot;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.transaction.TransactionID;

public class ConstantValues {
	private static String URL = "https://api-fxpractice.oanda.com";
	private static String ACCESS_TOKEN = "dc2d5455f7afb93f08fdb9215d743d63-b71c4c121ffdfe94b8da839aec55e568";
	private static AccountID ACCOUNT_ID = new AccountID("101-004-13661335-003");
	private static Double STANDARD_UNITS_TRADED = 2000.0;
	private final static Context CTX = new ContextBuilder(URL)
    		.setToken(ACCESS_TOKEN)
    		.setApplication("MyTradingBot-1.jar")
    		.build();
	private static Double stopLoss1 = 0.0;
	private static Double stopLoss2 = 0.0;
	private static Double stopLoss3 = 0.0;
	private static Double stopLoss4 = 0.0;
	private static Double stopLoss5 = 0.0;
	private static Double stopLoss6 = 0.0;
	private static Double takeProfit1 = 0.0;
	private static Double takeProfit2 = 0.0;
	private static Double takeProfit3 = 0.0;
	private static Double takeProfit4 = 0.0;
	private static Double takeProfit5 = 10.0;
	private static Double takeProfit6 = 0.0;
	private static Double MIN_BALANCE = 300.0;
	private static Double MAX_OPEN_TRADES = 15.0;
	private static Double MIN_MARGIN = 400.0;
	private static Double BAD_MARGIN = 200.0;
	private static TransactionID TRANSCATION_ID = new TransactionID("1762");
	
	public static Double getTakeProfit1() {
		return takeProfit1;
	}
	
	public static Double getTakeProfit2() {
		return takeProfit2;
	}
	
	public static Double getTakeProfit3() {
		return takeProfit3;
	}
	
	public static Double getTakeProfit4() {
		return takeProfit4;
	}
	
	public static Double getTakeProfit5() {
		return takeProfit5;
	}
	
	public static Double getTakeProfit6() {
		return takeProfit6;
	}
	
	public static Double getStopLoss1() {
		return stopLoss1;
	}
	
	public static Double getStopLoss2() {
		return stopLoss2;
	}
	
	public static Double getStopLoss3() {
		return stopLoss3;
	}
	
	public static Double getStopLoss4() {
		return stopLoss4;
	}
	
	public static Double getStopLoss5() {
		return stopLoss5;
	}
	
	public static Double getStopLoss6() {
		return stopLoss6;
	}
	
	public static void setStopLoss1(Double value) {
		stopLoss1 = value;
	}
	
	public static void setStopLoss2(Double value) {
		stopLoss2 = value;
	}
	
	public static void setStopLoss3(Double value) {
		stopLoss3 = value;
	}
	
	public static void setStopLoss4(Double value) {
		stopLoss4 = value;
	}
	
	public static void setStopLoss5(Double value) {
		stopLoss5 = value;
	}
	
	public static void setStopLoss6(Double value) {
		stopLoss6 = value;
	}
	
	public static void SetTakeProfit1(Double value) {
		takeProfit1 = value;
	}
	
	public static void setTakeProfit2(Double value) {
		takeProfit2 = value;
	}
	
	public static void setTakeProfit3(Double value) {
		takeProfit3 = value;
	}
	
	public static void setTakeProfit4(Double value) {
		takeProfit4 = value;
	}
	
	public static void setTakeProfit5(Double value) {
		takeProfit5 = value;
	}
	
	public static void setTakeProfit6(Double value) {
		takeProfit6 = value;
	}
	
	public static void setBadMargin(Double value) {
		BAD_MARGIN = value;
	}
	
	public static void setMinMargin(Double value) {
		MIN_MARGIN = value;
	}
	
	public static void setMaxOpenTrades(Double value) {
		MAX_OPEN_TRADES = value;
	}
	
	public static void setMinBalance(Double value) {
		MIN_BALANCE = value;
	}
	
	/**
	 * @param the units to trade
	 * */
	public static void setUnitsTraded(Double units) {
		STANDARD_UNITS_TRADED = units;
	}
	
	/**
	 * @param accountID
	 * */
	public static void setAccountID(String id) {
		ACCOUNT_ID = new AccountID(id);
	}
	
	/**
	 * @param brokers url
	 * */
	public static void setUrl(String url) {
		URL = url;
	}
	
	/**
	 * @param Access token
	 * */
	public static void setAccessToken(String accessToken) {
		ACCESS_TOKEN = accessToken;
	}
	
	/**
	 * @param cancel percent for 1h
	 * */
	
	/**
	 * @return the last transaction id when the program starts
	 * */
	public static TransactionID getLatestTransactionID() {
		return TRANSCATION_ID;
	}
	
	/**
	 * @return the point where the margin is too low
	 * */
	public static Double getBadMargin() {
		return BAD_MARGIN;
	}
	
	/**
	 * @return the minimum margin required
	 * */
	public static Double getMinMargin() {
		return MIN_MARGIN;
	}
	
	/**
	 * @return the max open trades allowed
	 * */
	public static Double getMaxOpenTrades() {
		return MAX_OPEN_TRADES;
	}
	
	/**
	 * @return the minimum balance to make order
	 * */
	public static Double getMinBalance() {
		return MIN_BALANCE;
	}
	
	/**
	 * @return the url used
	 * */
	public static String getURL() {
		return URL;
	}
	
	/**
	 * @return the requested context
	 * */
	public static Context getCtx() {
		return CTX;
	}
	
	/**
	 * @return the access token used
	 * */
	public static String getAccessToken() {
		return ACCESS_TOKEN;
	}
	
	/**
	 * @return the account ID used
	 * */
	public static AccountID getAccountId() {
		return ACCOUNT_ID;
	}
	
	/**
	 * @return standard number of units to trade
	 * */
	public static Double getUnitsTraded() {
		return STANDARD_UNITS_TRADED;
	}
	
	
}
