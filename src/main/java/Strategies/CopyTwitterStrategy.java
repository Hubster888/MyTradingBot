package Strategies;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oanda.v20.order.LimitOrderRequest;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderRequest;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.transaction.StopLossDetails;
import com.oanda.v20.transaction.TakeProfitDetails;

import Documenting.SendReport;
import MyTradingBot.ConstantValues;
import QueueManager.OrderCreateRequestQueue;

public class CopyTwitterStrategy {
	public static Queue<String> unusedTweets = new LinkedList<String>();
	private static final Pattern SignalFactoryPattern = Pattern.compile("^(Signal Factory{1})(.+)(Sell{1}|Buy{1})(.)([A-Z]{6})(@)([0-9]+.[0-9]+)(.+)(SL:)([0-9]+.[0-9]+)(.+)(TP:)([0-9]+.[0-9]+)");
	private static final Pattern MXInvestorPattern = Pattern.compile("^(.+)(Buy|Sell).(([A-Z]{3}.[A-Z]{3})|Gold)(.+)([0-9]{1,3}H|D)");

	/**
	 * @return the queue
	 * */
	public static Queue<String> getQueue() {
		return unusedTweets;
	}

	public CopyTwitterStrategy() {
	}

	/**
	 * @param the next tweet
	 * This method bisects the tweet and calls the method
	 * that can read this format
	 * */
	public static void executeTweet() {

		String tweet = unusedTweets.element();
		unusedTweets.remove();
		if(tweet.equals("")) { 
			System.out.println("tweet is empty");
			SendReport.addError("the tweet is empty | executeTweet() | CopyTwitterStrategy");
			return;
		}

		if(tweet.contains("Signal Factory")) {
			signalFactoryTweet(tweet);
		}else if(tweet.contains("MX investing (Forex Signals)")) {
			MXInvestingTweet(tweet);
		}else {
			SendReport.addError("wrong users tweet | executeTweet() | CopyTwitterStrategy");
			System.out.println("wrong user");
		}
	}

	/*public static void main(String[] args) {
		QueueManagmentThread queueThread = new QueueManagmentThread();
		//queueThread.start();
		
	
		
		unusedTweets.add("MX investing (Forex Signals):⬆️ Buy GBP/JPY - Bitcoin US Dollar on 1H time frame\r\n" + 
				"Current price: 130.34\r\n" + 
				"Bulls↗️ Power 58%\r\n" + 
				"Bears↘️ Power 25%\r\n" + 
				"#BTCUSD #trading #Tradecopier #cryptocurrency #cryptoTrading #investment #DayTrading");
		
		//unusedTweets.add("Signal Factory:Forex Signal | Buy USDCHF@0.94558 | SL:0.94158 | TP:0.95358 | 2020.03.16 16:06 GMT | #fx #forex #fb");
				
		//unusedTweets.add("Signal Factory:Forex Signal | Sell GBPJPY@132.746 | SL:133.146 | TP:131.946 | 2020.03.13 05:30 GMT | #fx #forex #fb");
		//Matcher m = MXInvestorPattern.matcher("MX investing (Forex Signals):⬆️ Buy BTC/USD - Bitcoin US Dollar on 1H time frame\r\n" );
		//System.out.println(m.group(0));
		//while(m.find()) {
			//System.out.println(m.group(0));
		//}
		executeTweet();
		//executeTweet();
		//executeTweet();
	}*/
	 
	/**
	 * This decides how many units to trade by using the power
	 * percentages.
	 * @param the tweet to be analysed
	 * @return true if the order is sent
	 * */
	private static Boolean MXInvestingTweet(String tweet) {
		if(!verifyMXInvesting(tweet)) {
			System.out.println("Tweet is invalid");
			SendReport.addError("the tweet is not valid | MXInvestingTweet() | CopyTwitterStrategy");
			return false;
		}
		OrderRequest request = null;
		InstrumentName instrument = null;
		String buyOrSell = null;
		Double currentPrice = Double.parseDouble(tweet.split("\n")[1].split(" ")[2].replace(",", ""));
		Double bullsPercentage = null;
		Double bearsPercentage = null;
		Double units = ConstantValues.getUnitsTraded();
		Double takeProfitPrice = null;
		Double stopLossPrice = null;
		Matcher matcher = MXInvestorPattern.matcher(tweet);
		DecimalFormat df = new DecimalFormat("#.###");
		DecimalFormat dfUnit = new DecimalFormat("#");
		df.setRoundingMode(RoundingMode.CEILING);
		dfUnit.setRoundingMode(RoundingMode.CEILING);
		try {
			while(matcher.find()) {
				
				buyOrSell = matcher.group(2);
				if(tweet.contains("Gold")) {
					instrument = new InstrumentName("XAU_USD");
				}else {
					instrument = new InstrumentName(matcher.group(3).replace("/", "_"));
					if(instrument.toString().contains("BTC")) {
						SendReport.addError("the tweet contains BTC | MXInvestingTweet() | CopyTwitterStrategy");
						return false;
					}
				}
				String timeFrame = matcher.group(6);
				bullsPercentage = Double.parseDouble(tweet.split("\n")[2].split(" ")[2].split("%")[0]) / 100;
				bearsPercentage = Double.parseDouble(tweet.split("\n")[3].split(" ")[2].split("%")[0]) / 100;
				if(timeFrame.contains("H")) {
					int num = Integer.parseInt(timeFrame.replace("H", ""));
					switch(num) {
					case 1:
						takeProfitPrice = Double.parseDouble(df.format(currentPrice + (currentPrice * ConstantValues.getCancelPercent1H())));
						stopLossPrice = Double.parseDouble(df.format(currentPrice - (currentPrice * ConstantValues.getCancelPercent1H())));
						break;
					case 4:
						takeProfitPrice = Double.parseDouble(df.format(currentPrice + (currentPrice * ConstantValues.getCancelPercent4H())));
						stopLossPrice = Double.parseDouble(df.format(currentPrice - (currentPrice * ConstantValues.getCancelPercent4H())));
						break;
					case 5:
						takeProfitPrice = Double.parseDouble(df.format(currentPrice + (currentPrice * ConstantValues.getCancelPercent5H())));
						stopLossPrice = Double.parseDouble(df.format(currentPrice - (currentPrice * ConstantValues.getCancelPercent5H())));
						break;
					default:
						takeProfitPrice = Double.parseDouble(df.format(currentPrice + (currentPrice * ConstantValues.getCancelPercent1H())));
						stopLossPrice = Double.parseDouble(df.format(currentPrice - (currentPrice * ConstantValues.getCancelPercent1H())));
					}
				}else {
					takeProfitPrice = Double.parseDouble(df.format(currentPrice + (currentPrice * ConstantValues.getCancelPercent1D())));
					stopLossPrice = Double.parseDouble(df.format(currentPrice - (currentPrice * ConstantValues.getCancelPercent1D())));
				}
				units = Double.parseDouble(dfUnit.format((ConstantValues.getUnitsTraded() * bullsPercentage)));
				
				if(buyOrSell.equals("Sell")) {
					units = Double.parseDouble(dfUnit.format((ConstantValues.getUnitsTraded() * bearsPercentage))) * -1;
					Double temp = takeProfitPrice;
					takeProfitPrice = stopLossPrice;
					stopLossPrice = temp;
				}
			}
		}catch(Exception e) {
			SendReport.addError(e.getMessage());
			e.printStackTrace();
			return false;
		}
		// Create the request once data is collected
		request = new MarketOrderRequest()
				.setUnits(units)
				.setInstrument(instrument)
				.setTakeProfitOnFill(new TakeProfitDetails()
						.setPrice(takeProfitPrice))
				.setStopLossOnFill(new StopLossDetails()
						.setPrice(stopLossPrice));
		if(sendOrder(request)) {
			return true;
		}
		SendReport.addError("the sendOrder() failed | MXInvestingTweet() | CopyTwitterStrategy");
		return false;
	}

	/**
	 * @param the tweet to be analysed
	 * @return true if the order is sent
	 * */
	private static Boolean signalFactoryTweet(String tweet) {
		String buyOrSell = null;
		InstrumentName instrument = null;
		Double entryPrice = null;
		Double stopLossPrice = null;
		Double takeProfitPrice = null;
		Double units = null;
		OrderRequest request = null;
		Matcher matcher = SignalFactoryPattern.matcher(tweet);
		
		if(!verifySignalFactory(tweet)) {
			System.out.println("Tweet is not valid");
			SendReport.addError("the tweet is not valid | signalFactoryTweet() | CopyTwitterStrategy");
			return false;
		}
		try {
			while(matcher.find()) {
				buyOrSell = matcher.group(3);
				String[] instrumentRaw = matcher.group(5).split("");
				instrument = new InstrumentName(instrumentRaw[0] + instrumentRaw[1] +
						instrumentRaw[2] + "_" + instrumentRaw[3] + instrumentRaw[4]
								+ instrumentRaw[5]);
				entryPrice = Double.parseDouble(matcher.group(7));
				stopLossPrice = Double.parseDouble(matcher.group(10));
				takeProfitPrice = Double.parseDouble(matcher.group(13));
				units = ConstantValues.getUnitsTraded() * 0.5;
				if(buyOrSell.equals("Sell")) {
					units *= -1;
				}
			}
		}catch(Exception e) {
			SendReport.addError(e.getMessage());
			e.printStackTrace();
		}
		request = new LimitOrderRequest()
				.setInstrument(instrument)
				.setUnits(units)
				.setPrice(entryPrice)
				.setStopLossOnFill(new StopLossDetails()
						.setPrice(stopLossPrice))
				.setTakeProfitOnFill(new TakeProfitDetails()
						.setPrice(takeProfitPrice));
		if(sendOrder(request)) {
			return true;
		}
		SendReport.addError("the sendOrder() failed | SignalFactoryTweet() | CopyTwitterStrategy");
		return false;
	}

	/**
	 * @param the order to be sent to the queue
	 * @return true if the order is sent correctly
	 * */
	public static Boolean sendOrder(OrderRequest requestOrder) {
		if(OrderCreateRequestQueue.addToQueue(requestOrder)) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @param the tweet to be tested
	 * @return true if the tweet is valid
	 * */
	private static Boolean verifySignalFactory(String tweet) {
		if((tweet.contains("Close(SL)") || tweet.contains("Close(PL)")) ||
				tweet.contains("Profit") || tweet.contains("Loss") || tweet.contains("SHORT") ||
				tweet.contains("LONG")) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param The tweet to be verify
	 * @return true if the tweet is valid
	 * */
	private static Boolean verifyMXInvesting(String tweet) {
		if((tweet.contains("Buy") || tweet.contains("Sell")) && tweet.contains("Bulls")
				&& tweet.contains("Bears")) {
			return true;
		}else {
			return false;
		}
	}
}
