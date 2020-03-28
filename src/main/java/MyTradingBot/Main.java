package MyTradingBot;

<<<<<<< HEAD
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountContext;
import com.oanda.v20.account.AccountGetResponse;
import com.oanda.v20.transaction.TransactionID;

import Documenting.FailedTweet;
import Documenting.SendReport;
import MyTradingBot.ConstantValues;
import Threads.QueueManagmentThread;
import Threads.TwitterThread;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application
{
	private static TwitterThread twitterThread = new TwitterThread();
	private static QueueManagmentThread queueThread = new QueueManagmentThread();
	private static AtomicBoolean isOn = new AtomicBoolean(true);

	public static Boolean getIsOn() {
		return isOn.get();
	}

	public static void go()
	{
		AccountGetResponse response = null;
		AccountContext accountCtx = new AccountContext(ConstantValues.getCtx());
		try {
			response = accountCtx.get(ConstantValues.getAccountId());
		} catch (RequestException e) {
			e.printStackTrace();
		} catch (ExecuteException e) {
			e.printStackTrace();
		}
		TransactionID startOfDayID = response.getAccount().getLastTransactionID();
		@SuppressWarnings("unused")
		SendReport sendReport = new SendReport(response, 0, 0,
				0, response.getAccount().getNAV().doubleValue(), new LinkedList<FailedTweet>(), new LinkedList<String>(), startOfDayID);


		Timer time = new Timer(); // Instantiate Timer Object
		SendReport st = new SendReport(); // Instantiate SheduledTask class
		time.schedule(st, 0, 1000 * 60 * 60 * 1);

		twitterThread.start();
		queueThread.start();
	}


	@Override
	public void start(Stage stage) {
		isOn.set(true);
		Button on = new Button("ON");

		Button off = new Button("OFF");

		Button exit = new Button("EXIT");
		exit.setOnAction(e -> {
			System.exit(0);
		});
		Label keyTitle = new Label("Take profit / Stop loss:");
		Label key1 = new Label("1) 5 sec to 1 min");
		Label key2 = new Label("2) 5 to 15 mins");
		Label key3 = new Label("3) 30 mins to 1 hour");
		Label key4 = new Label("4) 4 hours to 1 day");
		Label key5 = new Label("5) 1 week");
		Label key6 = new Label("6) 1 month");
		Label url = new Label("Broker url:");
		url.setFont(new Font("Arial", 21));
		Label accessToken = new Label("Access Token:");
		accessToken.setFont(new Font("Arial", 21));
		Label accountId = new Label("Account ID:");
		accountId.setFont(new Font("Arial", 21));
		Label maxUnitsInTrade = new Label("Max units for single trade:");
		maxUnitsInTrade.setFont(new Font("Arial", 21));

		Label stopLoss1 = new Label("Stop loss - 1:");
		stopLoss1.setFont(new Font("Arial", 21));
		Label stopLoss2 = new Label("Stop loss - 2:");
		stopLoss2.setFont(new Font("Arial", 21));
		Label stopLoss3 = new Label("Stop loss - 3:");
		stopLoss3.setFont(new Font("Arial", 21));
		Label stopLoss4 = new Label("Stop loss - 4:");
		stopLoss4.setFont(new Font("Arial", 21));
		Label stopLoss5 = new Label("Stop loss - 5:");
		stopLoss5.setFont(new Font("Arial", 21));
		Label stopLoss6 = new Label("Stop loss - 6:");
		stopLoss6.setFont(new Font("Arial", 21));

		Label takeProfit1 = new Label("Take profit - 1:");
		takeProfit1.setFont(new Font("Arial", 21));
		Label takeProfit2 = new Label("Take profit - 2:");
		takeProfit2.setFont(new Font("Arial", 21));
		Label takeProfit3 = new Label("Take profit - 3:");
		takeProfit3.setFont(new Font("Arial", 21));
		Label takeProfit4 = new Label("Take profit - 4:");
		takeProfit4.setFont(new Font("Arial", 21));
		Label takeProfit5 = new Label("Take profit - 5:");
		takeProfit5.setFont(new Font("Arial", 21));
		Label takeProfit6 = new Label("Take profit - 6:");
		takeProfit6.setFont(new Font("Arial", 21));

		Label failMargin = new Label("Fail margin:");
		failMargin.setFont(new Font("Arial", 21));
		Label minMargin = new Label("Min margin:");
		minMargin.setFont(new Font("Arial", 21));
		Label maxOpenTrades = new Label("Max open trades:");
		maxOpenTrades.setFont(new Font("Arial", 21));
		Label minBalance = new Label("Min balance:");
		minBalance.setFont(new Font("Arial", 21));

		TextField urlT = new TextField();
		TextField accessTokenT  = new TextField();
		TextField accountIdT  = new TextField();

		TextField maxUnitsInTradeT  = new TextField();
		TextField stopLoss1T  = new TextField();
		TextField stopLoss2T  = new TextField();
		TextField stopLoss3T  = new TextField();
		TextField stopLoss4T  = new TextField();
		TextField stopLoss5T  = new TextField();
		TextField stopLoss6T  = new TextField();

		TextField takeProfit1T  = new TextField();
		TextField takeProfit2T  = new TextField();
		TextField takeProfit3T  = new TextField();
		TextField takeProfit4T  = new TextField();
		TextField takeProfit5T  = new TextField();
		TextField takeProfit6T  = new TextField();

		TextField failMarginT  = new TextField();
		TextField minMarginT  = new TextField();
		TextField maxOpenTradesT  = new TextField();
		TextField minBalanceT  = new TextField();

		GridPane root = new GridPane();
		GridPane left = new GridPane();
		GridPane right = new GridPane();
		root.add(left, 0, 0);
		root.add(right, 1, 0);

		left.add(url, 0, 0);
		left.add(accessToken, 0, 1);
		left.add(accountId, 0, 2);
		left.add(maxUnitsInTrade, 0, 3);
		left.add(stopLoss1, 0, 4);
		left.add(stopLoss2, 0, 5);
		left.add(stopLoss3, 0, 6);
		left.add(stopLoss4, 0, 7);
		left.add(stopLoss5, 0, 8);
		left.add(stopLoss6, 0, 9);
		left.add(takeProfit1, 0, 10);
		left.add(takeProfit2, 0, 11);
		left.add(takeProfit3, 0, 12);
		left.add(takeProfit4, 0, 13);
		left.add(takeProfit5, 0, 14);
		left.add(takeProfit6, 0, 15);
		left.add(failMargin, 0, 16);
		left.add(minMargin, 0, 17);
		left.add(maxOpenTrades, 0, 18);
		left.add(minBalance, 0, 19);

		right.add(urlT, 0, 0);
		right.add(accessTokenT, 0, 1);
		right.add(accountIdT, 0, 2);
		right.add(maxUnitsInTradeT, 0, 3);
		right.add(stopLoss1T, 0, 4);
		right.add(stopLoss2T, 0, 5);
		right.add(stopLoss3T, 0, 6);
		right.add(stopLoss4T, 0, 7);
		right.add(stopLoss5T, 0, 8);
		right.add(stopLoss6T, 0, 9);
		right.add(takeProfit1T, 0, 10);
		right.add(takeProfit2T, 0, 11);
		right.add(takeProfit3T, 0, 12);
		right.add(takeProfit4T, 0, 13);
		right.add(takeProfit5T, 0, 14);
		right.add(takeProfit6T, 0, 15);
		right.add(failMarginT, 0, 16);
		right.add(minMarginT, 0, 17);
		right.add(maxOpenTradesT, 0, 18);
		right.add(minBalanceT, 0, 19);

		GridPane keyGrid = new GridPane();
		root.add(keyGrid, 2, 0);
		keyGrid.add(keyTitle, 0, 0);
		keyGrid.add(key1, 0, 1);
		keyGrid.add(key2, 0, 2);
		keyGrid.add(key3, 0, 3);
		keyGrid.add(key4, 0, 4);
		keyGrid.add(key5, 0, 5);
		keyGrid.add(key6, 0, 6);

		GridPane buttonGrid = new GridPane();
		root.add(buttonGrid, 3, 0);
		buttonGrid.add(on, 0, 0);
		buttonGrid.add(off, 0, 1);
		buttonGrid.add(exit, 0, 2);

		File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\src\\main\\java\\SavedValues");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		urlT.setText(sc.nextLine());
		accessTokenT.setText(sc.nextLine());
		accountIdT.setText(sc.nextLine());
		maxUnitsInTradeT.setText(sc.nextLine());
		stopLoss1T.setText(sc.nextLine());
		stopLoss2T.setText(sc.nextLine());
		stopLoss3T.setText(sc.nextLine());
		stopLoss4T.setText(sc.nextLine());
		stopLoss5T.setText(sc.nextLine());
		stopLoss6T.setText(sc.nextLine());
		takeProfit1T.setText(sc.nextLine());
		takeProfit2T.setText(sc.nextLine());
		takeProfit3T.setText(sc.nextLine());
		takeProfit4T.setText(sc.nextLine());
		takeProfit5T.setText(sc.nextLine());
		takeProfit6T.setText(sc.nextLine());
		failMarginT.setText(sc.nextLine());
		minMarginT.setText(sc.nextLine());
		maxOpenTradesT.setText(sc.nextLine());
		minBalanceT.setText(sc.nextLine());
		sc.close();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setWidth(1500.);
		stage.setHeight(900.0);
		stage.setTitle("MTradingBot");
		stage.show();

		off.setOnAction(e -> {
			if(isOn.get()) {
				isOn.set(false);
				System.out.println("System is now off");
			}else {
				System.out.println("The system is already off");
			}
		});

		on.setOnAction(e -> {
			if(isOn.get()) {
				System.out.println("Already on!!");
			}else {
				isOn.set(true);
				System.out.println("System is now on");
				setValues(urlT.getText(), accessTokenT.getText(), accountIdT.getText(),
						maxUnitsInTradeT.getText(), stopLoss1T.getText(), stopLoss2T.getText(),
						stopLoss3T.getText(), stopLoss4T.getText(), stopLoss5T.getText(),
						stopLoss6T.getText(), takeProfit1T.getText(), takeProfit2T.getText(),
						takeProfit3T.getText(), takeProfit4T.getText(), takeProfit5T.getText(),
						takeProfit6T.getText(), failMarginT.getText(), minMarginT.getText(),
						maxOpenTradesT.getText(), minBalanceT.getText());
				saveValues(urlT.getText(), accessTokenT.getText(), accountIdT.getText(),
						maxUnitsInTradeT.getText(), stopLoss1T.getText(), stopLoss2T.getText(),
						stopLoss3T.getText(), stopLoss4T.getText(), stopLoss5T.getText(),
						stopLoss6T.getText(), takeProfit1T.getText(), takeProfit2T.getText(),
						takeProfit3T.getText(), takeProfit4T.getText(), takeProfit5T.getText(),
						takeProfit6T.getText(), failMarginT.getText(), minMarginT.getText(),
						maxOpenTradesT.getText(), minBalanceT.getText());
			}
		});
		go();
	}

	public static void main(String[] args) {
		launch();
	}

	private static void setValues(String url, String accessToken, String accountId, String maxUnitsInTrade,
			String stopLoss1, String stopLoss2, String stopLoss3, String stopLoss4, String stopLoss5,
			String stopLoss6, String takeProfit1, String takeProfit2, String takeProfit3, String takeProfit4,
			String takeProfit5, String takeProfit6, String failMargin, String minMargin, String maxOpenTrades,
			String minBalance) {
		ConstantValues.setUrl(url);
		ConstantValues.setAccessToken(accessToken); 
		ConstantValues.setAccountID(accountId);
		ConstantValues.setUnitsTraded(Double.parseDouble(maxUnitsInTrade));
		ConstantValues.setStopLoss1(Double.parseDouble(stopLoss1));
		ConstantValues.setStopLoss2(Double.parseDouble(stopLoss2));
		ConstantValues.setStopLoss3(Double.parseDouble(stopLoss3));
		ConstantValues.setStopLoss4(Double.parseDouble(stopLoss4));
		ConstantValues.setStopLoss5(Double.parseDouble(stopLoss5));
		ConstantValues.setStopLoss6(Double.parseDouble(stopLoss6));
		ConstantValues.SetTakeProfit1(Double.parseDouble(takeProfit1));
		ConstantValues.setTakeProfit2(Double.parseDouble(takeProfit2));
		ConstantValues.setTakeProfit3(Double.parseDouble(takeProfit3));
		ConstantValues.setTakeProfit4(Double.parseDouble(takeProfit4));
		ConstantValues.setTakeProfit5(Double.parseDouble(takeProfit5));
		ConstantValues.setTakeProfit6(Double.parseDouble(takeProfit6));
		ConstantValues.setBadMargin(Double.parseDouble(failMargin));
		ConstantValues.setMinMargin(Double.parseDouble(minMargin));
		ConstantValues.setMaxOpenTrades(Double.parseDouble(maxOpenTrades));
		ConstantValues.setMinBalance(Double.parseDouble(minBalance));
	}

	private static void saveValues(String url, String accessToken, String accountId, String maxUnitsInTrade,
			String stopLoss1, String stopLoss2, String stopLoss3, String stopLoss4, String stopLoss5,
			String stopLoss6, String takeProfit1, String takeProfit2, String takeProfit3, String takeProfit4,
			String takeProfit5, String takeProfit6, String failMargin, String minMargin, String maxOpenTrades,
			String minBalance) {
		String data = url + "\n" + accessToken + "\n" + accountId + "\n" + maxUnitsInTrade + "\n" + stopLoss1 + "\n" +
				stopLoss2 + "\n" + stopLoss3 + "\n" + stopLoss4 + "\n" + stopLoss5 + "\n" + stopLoss6 + "\n" + takeProfit1 + "\n" +
				takeProfit2 + "\n" + takeProfit3 + "\n" + takeProfit4 + "\n" + takeProfit5 + "\n" + takeProfit6 + "\n" +
				failMargin + "\n" + minMargin + "\n" + maxOpenTrades + "\n" + minBalance;
		File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\src\\main\\java\\SavedValues");
		FileWriter fr = null;
		try {
			fr = new FileWriter(file);
			fr.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
=======

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountChangesRequest;
import com.oanda.v20.account.AccountChangesResponse;
import com.oanda.v20.trade.TradeSummary;
import com.oanda.v20.transaction.TransactionID;

import MyTradingBot.ConstantValues;
import Threads.QueueManagmentThread;
import Threads.TwitterThread;

/**
 * Hello world!
 *
 */
public class Main 
{
	
    public static void main(String[] args) throws RequestException, ExecuteException
    {
    	
    	TransactionID since = null;
    	TwitterThread twitterThread = new TwitterThread();
    	QueueManagmentThread queueThread = new QueueManagmentThread();
        twitterThread.start();
        queueThread.start();
        
        while(true) {
        	if(since == null) {
        		since = new TransactionID("1");
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
        		since = accountResponse.getLastTransactionID();
        		List<TradeSummary> closedTrades = accountResponse.getChanges().getTradesClosed();
        		for(TradeSummary trade : closedTrades) {
        			try {
						recordClosedTrade(trade);
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}else {
        		AccountChangesRequest accountRequest = new AccountChangesRequest(ConstantValues.getAccountId()).setSinceTransactionID(since);
        		AccountChangesResponse accountResponse = ConstantValues.getCtx().account.changes(accountRequest);
        		since = accountResponse.getLastTransactionID();
        		List<TradeSummary> closedTrades = accountResponse.getChanges().getTradesClosed();
        		for(TradeSummary trade : closedTrades) {
        			try {
						recordClosedTrade(trade);
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}
        	
        	
        	try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
    }
    
    public static void recordClosedTrade(TradeSummary trade) throws IOException {
	    
	    File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\closedTradesStorage.txt");
	    FileWriter fr = new FileWriter(file, true);
		try {
			fr.write(trade.toString() + "\n");
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
		}
	}
}
