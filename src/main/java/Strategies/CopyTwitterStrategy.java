package Strategies;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
=======
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

<<<<<<< HEAD
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountInstrumentsRequest;
import com.oanda.v20.account.AccountInstrumentsResponse;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderRequest;
import com.oanda.v20.primitives.Instrument;
=======
import com.oanda.v20.order.MarketIfTouchedOrderRequest;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderRequest;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.transaction.StopLossDetails;
import com.oanda.v20.transaction.TakeProfitDetails;

<<<<<<< HEAD
import Documenting.SendReport;
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import MyTradingBot.ConstantValues;
import QueueManager.OrderCreateRequestQueue;

public class CopyTwitterStrategy {
<<<<<<< HEAD
=======
	//TODO improve the accuracy of the tweets by analysing historical tweets
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
		if(tweet.equals("")) { 
			System.out.println("tweet is empty");
			SendReport.addError("the tweet is empty | executeTweet() | CopyTwitterStrategy " + tweet);
			return;
		}
=======
		if(tweet.equals("")) { System.out.println("tweet is empty");}
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7

		if(tweet.contains("Signal Factory")) {
			signalFactoryTweet(tweet);
		}else if(tweet.contains("MX investing (Forex Signals)")) {
			MXInvestingTweet(tweet);
		}else {
<<<<<<< HEAD
			SendReport.addError("wrong users tweet | executeTweet() | CopyTwitterStrategy " + tweet);
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
			SendReport.addError("the tweet is not valid | MXInvestingTweet() | CopyTwitterStrategy " + tweet);
			return false;
		}
		MarketOrderRequest request = null;
=======
			return false;
		}
		OrderRequest request = null;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
						SendReport.addError("the tweet contains BTC | MXInvestingTweet() | CopyTwitterStrategy " + tweet);
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
						return false;
					}
				}
				String timeFrame = matcher.group(6);
				bullsPercentage = Double.parseDouble(tweet.split("\n")[2].split(" ")[2].split("%")[0]) / 100;
				bearsPercentage = Double.parseDouble(tweet.split("\n")[3].split(" ")[2].split("%")[0]) / 100;
<<<<<<< HEAD
				if(timeFrame.contains("H")) {
					int num = Integer.parseInt(timeFrame.replace("H", ""));
					switch(num) {
					case 1:	
						stopLossPrice = calculateStopLoss(currentPrice, 1, instrument);
						takeProfitPrice = calculateTakeProfit(currentPrice, 1, instrument);
						break;
					case 4:
						stopLossPrice = calculateStopLoss(currentPrice, 4, instrument);
						takeProfitPrice = calculateTakeProfit(currentPrice, 4, instrument);
						break;
					case 5:
						stopLossPrice = calculateStopLoss(currentPrice, 5, instrument);
						takeProfitPrice = calculateTakeProfit(currentPrice, 5, instrument);
						break;
					default:
						stopLossPrice = calculateStopLoss(currentPrice, 1, instrument);
						takeProfitPrice = calculateTakeProfit(currentPrice, 1, instrument);
					}
				}else {
					stopLossPrice = calculateStopLoss(currentPrice, 10, instrument);
					takeProfitPrice = calculateTakeProfit(currentPrice, 10, instrument);
=======
				//Long instrumentPip = getPipForInstrument(instrument);
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
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
			SendReport.addError(e.getMessage() + " " + tweet);
			e.printStackTrace();
			return false;
		}
		
		// Create the request once data is collected
		System.out.println("take proft at :" + takeProfitPrice);
		System.out.println("stop loss at : " + stopLossPrice);
=======
			e.printStackTrace();
			return false;
		}
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
		SendReport.addError("the sendOrder() failed | MXInvestingTweet() | CopyTwitterStrategy " + tweet);
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
		return false;
	}

	/**
	 * @param the tweet to be analysed
	 * @return true if the order is sent
	 * */
	private static Boolean signalFactoryTweet(String tweet) {
		String buyOrSell = null;
		InstrumentName instrument = null;
<<<<<<< HEAD
		Double stopLossPrice = null;
		Double takeProfitPrice = null;
		Double units = null;
		MarketOrderRequest request = null;
=======
		Double entryPrice = null;
		Double stopLossPrice = null;
		Double takeProfitPrice = null;
		Double units = null;
		OrderRequest request = null;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
		Matcher matcher = SignalFactoryPattern.matcher(tweet);
		
		if(!verifySignalFactory(tweet)) {
			System.out.println("Tweet is not valid");
<<<<<<< HEAD
			SendReport.addError("the tweet is not valid | signalFactoryTweet() | CopyTwitterStrategy " + tweet);
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
			return false;
		}
		try {
			while(matcher.find()) {
				buyOrSell = matcher.group(3);
				String[] instrumentRaw = matcher.group(5).split("");
				instrument = new InstrumentName(instrumentRaw[0] + instrumentRaw[1] +
						instrumentRaw[2] + "_" + instrumentRaw[3] + instrumentRaw[4]
								+ instrumentRaw[5]);
<<<<<<< HEAD
=======
				entryPrice = Double.parseDouble(matcher.group(7));
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
				stopLossPrice = Double.parseDouble(matcher.group(10));
				takeProfitPrice = Double.parseDouble(matcher.group(13));
				units = ConstantValues.getUnitsTraded() * 0.5;
				if(buyOrSell.equals("Sell")) {
					units *= -1;
				}
			}
		}catch(Exception e) {
<<<<<<< HEAD
			SendReport.addError(e.getMessage() + " " + tweet);
			e.printStackTrace();
		}
		request = new MarketOrderRequest()
				.setInstrument(instrument)
				.setUnits(units)
=======
			e.printStackTrace();
		}
		request = new MarketIfTouchedOrderRequest()
				.setInstrument(instrument)
				.setUnits(units)
				.setPrice(entryPrice)
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
				.setStopLossOnFill(new StopLossDetails()
						.setPrice(stopLossPrice))
				.setTakeProfitOnFill(new TakeProfitDetails()
						.setPrice(takeProfitPrice));
		if(sendOrder(request)) {
			return true;
		}
<<<<<<< HEAD
		System.out.println("the sendOrder() failed | SignalFactoryTweet() | CopyTwitterStrategy " + tweet);
		SendReport.addError("the sendOrder() failed | SignalFactoryTweet() | CopyTwitterStrategy " + tweet);
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
<<<<<<< HEAD
	
	private static Double calculateTakeProfit(Double currentPrice, int timeFrame, InstrumentName instrumentName) throws RequestException, ExecuteException {
		AccountInstrumentsRequest request = new AccountInstrumentsRequest(ConstantValues.getAccountId());
		AccountInstrumentsResponse response = ConstantValues.getCtx().account.instruments(request);
		List<Instrument> instruments = response.getInstruments();
		double pipLocation = 0.0;
		MathContext precision = null;
		for(Instrument instrument : instruments) {
			String instru = instrumentName.toString().replace("_", "/");
			if(instrument.getDisplayName().equals(instru)) {
				pipLocation = Math.pow(10, instrument.getPipLocation());
				precision = new MathContext((int) Math.abs(instrument.getPipLocation()));
			}
		}
		BigDecimal price = new BigDecimal(0);
		Double takeProfitPrice = 0.0;
		switch(timeFrame) {
		case 1:
			price = new BigDecimal((ConstantValues.getTakeProfit2() * pipLocation) + currentPrice);
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		case 2:
			price = new BigDecimal((ConstantValues.getTakeProfit3() * pipLocation) + currentPrice);
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		case 4:
			price = new BigDecimal((ConstantValues.getTakeProfit4() * pipLocation) + currentPrice);
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		case 10:
			price = new BigDecimal((ConstantValues.getTakeProfit5() * pipLocation) + currentPrice);
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		default:
			price = new BigDecimal((ConstantValues.getTakeProfit1() * pipLocation) + currentPrice);
			takeProfitPrice = price.round(precision).doubleValue();
			break;
		}
		return 0.0;
	}
	
	private static Double calculateStopLoss(Double currentPrice, int timeFrame, InstrumentName instrumentName) throws RequestException, ExecuteException {
		AccountInstrumentsRequest request = new AccountInstrumentsRequest(ConstantValues.getAccountId());
		AccountInstrumentsResponse response = ConstantValues.getCtx().account.instruments(request);
		List<Instrument> instruments = response.getInstruments();
		double pipLocation = 0.0;
		MathContext precision = new MathContext(1);
		for(Instrument instrument : instruments) {
			String instru = instrumentName.toString().replace("_", "/");
			if(instrument.getDisplayName().equals(instru)) {
				pipLocation = Math.pow(10, instrument.getPipLocation());
				precision = new MathContext((int) Math.abs(instrument.getPipLocation()));
			}
		}
		Double takeProfitPrice = 0.0;
		BigDecimal price = null;
		switch(timeFrame) {
		case 1:
			price = new BigDecimal(currentPrice - (ConstantValues.getStopLoss1() * pipLocation));
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		case 2:
			price = new BigDecimal(currentPrice - (ConstantValues.getStopLoss3() * pipLocation));
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		case 4:
			price = new BigDecimal(currentPrice - (ConstantValues.getStopLoss4() * pipLocation));
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		case 10:
			price = new BigDecimal(currentPrice - (ConstantValues.getStopLoss5() * pipLocation));
			takeProfitPrice = price.round(precision).doubleValue();
			return takeProfitPrice;
		default:
			price = new BigDecimal(currentPrice - (ConstantValues.getStopLoss1() * pipLocation));
			takeProfitPrice = price.round(precision).doubleValue();
			break;
		}
		return 0.0;
	}
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
}
