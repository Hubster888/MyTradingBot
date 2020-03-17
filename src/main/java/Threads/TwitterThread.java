package Threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import DataCollection.TweetCollector;
import Strategies.CopyTwitterStrategy;

/**
 * This is the thread that will loop through
 * the different followed twitter users. If a new tweet is
 * found then the a new order request is created and sent to the other
 * thread. 
 * */
public class TwitterThread extends Thread {
	private static final Logger logger =
			Logger.getLogger(TwitterThread.class.getName());

	public void run() {
		try {
			setup();
		} catch (IOException e1) {
			System.out.println("There is something wrong with the logg setup");
			e1.printStackTrace();
		}
		try {
			System.out.println("twitter thread is running");
			TweetCollector tweetCollector = new TweetCollector();
			tweetCollector.setSince(new Date());
			while(true) {
				ArrayList<String> results = tweetCollector.getUserTiemline();
				tweetCollector.setSince(new Date());
				System.out.println("Number of new tweets: " + results.size());
				for(String result : results) {
					if(!result.equals("Null")) {
						recordTweetRecived(result);
						logger.log(Level.INFO, result);
						CopyTwitterStrategy.getQueue().add(result);
					}
				}
				Thread.sleep(60005);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	static public void setup() throws IOException {
		logger.setUseParentHandlers(false);

		logger.setLevel(Level.INFO);
		FileHandler fileTxt = new FileHandler("tweetLogger.txt");

		// create a TXT formatter
		SimpleFormatter formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}

	public static void recordTweetRecived(String data) throws IOException {
		File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\tweetsStorage.txt");
		FileWriter fr = new FileWriter(file, true);
		try {
			fr.write(data + "\n");
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}