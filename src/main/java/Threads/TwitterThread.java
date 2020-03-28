package Threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
<<<<<<< HEAD

import DataCollection.TweetCollector;
import Documenting.SendReport;
import MyTradingBot.Main;
=======
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import DataCollection.TweetCollector;
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import Strategies.CopyTwitterStrategy;

/**
 * This is the thread that will loop through
 * the different followed twitter users. If a new tweet is
 * found then the a new order request is created and sent to the other
 * thread. 
 * */
public class TwitterThread extends Thread {
<<<<<<< HEAD

	public void run() {
		try {
=======
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
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
			System.out.println("twitter thread is running");
			TweetCollector tweetCollector = new TweetCollector();
			tweetCollector.setSince(new Date());
			while(true) {
<<<<<<< HEAD
				while(Main.getIsOn()) {
					ArrayList<String> results = tweetCollector.getUserTimeLine();
					tweetCollector.setSince(new Date());
					System.out.println("Number of new tweets: " + results.size());
					for(String result : results) {
						if(!result.equals("Null")) {
							SendReport.addTweetRecived();
							recordTweetRecived(result);
							CopyTwitterStrategy.getQueue().add(result);
						}
					}
					Thread.sleep(60000);
				}
			}
		}catch(Exception e) {
			SendReport.addError(e.getMessage());
=======
				ArrayList<String> results = tweetCollector.getUserTiemline();
				tweetCollector.setSince(new Date());
				System.out.println("Number of new tweets: " + results.size());
				for(String result : results) {
					if(!result.equals("Null")) {
						recordTweetRecived(result);
						//logger.log(Level.INFO, result);
						CopyTwitterStrategy.getQueue().add(result);
					}
				}
				Thread.sleep(60005);
			}
		}catch(Exception e) {
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
			e.printStackTrace();
		}
	}

<<<<<<< HEAD
=======
	static public void setup() throws IOException {
		logger.setUseParentHandlers(false);

		logger.setLevel(Level.INFO);
		FileHandler fileTxt = new FileHandler("tweetLogger.txt");

		// create a TXT formatter
		SimpleFormatter formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}

>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
	public static void recordTweetRecived(String data) throws IOException {
		File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\tweetsStorage.txt");
		FileWriter fr = new FileWriter(file, true);
		try {
			fr.write(data + "\n");
			fr.close();
		} catch (IOException e) {
<<<<<<< HEAD
			SendReport.addError(e.getMessage());
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
			e.printStackTrace();
		}
	}
}