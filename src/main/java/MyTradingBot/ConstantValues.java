package MyTradingBot;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.account.AccountID;

public class ConstantValues {
	private final static String URL = "https://api-fxpractice.oanda.com";
	private final static String ACCESS_TOKEN = "dc2d5455f7afb93f08fdb9215d743d63-b71c4c121ffdfe94b8da839aec55e568";
	private final static AccountID ACCOUNT_ID = new AccountID("101-004-13661335-002");
	private final static Double STANDARD_UNITS_TRADED = 2000.0;
	private final static Context CTX = new ContextBuilder(URL)
    		.setToken(ACCESS_TOKEN)
    		.setApplication("MyTradingBot-1.jar")
    		.build();
	private final static Double CANCEL_PERCENT_1H = 0.0095;
	private final static Double CANCEL_PERCENT_4H = 0.012;
	private final static Double CANCEL_PERCENT_5H = 0.013;
	private final static Double CANCEL_PERCENT_1D = 0.025;
	private final static Double MIN_BALANCE = 300.0;
	private final static Double MAX_OPEN_TRADES = 15.0;
	private final static Double MIN_MARGIN = 200.0;
	
	
	/**
	 * @return the percent used for 1H time frames
	 * */
	public static Double getCancelPercent1H() {
		return CANCEL_PERCENT_1H;
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
	 * @return the percent used for 1D time frames
	 * */
	public static Double getCancelPercent1D() {
		return CANCEL_PERCENT_1D;
	}
	
	/**
	 * @return the percent used for 5H time frames
	 * */
	public static Double getCancelPercent5H() {
		return CANCEL_PERCENT_5H;
	}
	
	/**
	 * @return the url used
	 * */
	public static String getURL() {
		return URL;
	}
	
	/**
	 * @return the percent used for 4H time frames
	 * */
	public static Double getCancelPercent4H() {
		return CANCEL_PERCENT_4H;
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
