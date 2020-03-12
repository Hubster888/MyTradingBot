package MyTradingBot.MyTradingBot;

import com.oanda.v20.account.AccountID;

public class ConstantValues {
	private final static String URL = "";
	private final static String ACCESS_TOKEN = "";
	private final static AccountID ACCOUNT_ID = new AccountID("");
	
	public static String getURL() {
		return URL;
	}
	
	public static String getAccessToken() {
		return ACCESS_TOKEN;
	}
	
	public static AccountID getAccountId() {
		return ACCOUNT_ID;
	}
}
