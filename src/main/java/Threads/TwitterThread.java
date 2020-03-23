package Threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import DataCollection.TweetCollector;
import Documenting.Documentor;
import Strategies.CopyTwitterStrategy;

/**
 * This is the thread that will loop through
 * the different followed twitter users. If a new tweet is
 * found then the a new order request is created and sent to the other
 * thread. 
 * */
public class TwitterThread extends Thread {
	private static Documentor documentor = new Documentor();

	public void run() {
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
						documentor.addTweetRecived();
						recordTweetRecived(result);
						CopyTwitterStrategy.getQueue().add(result);
					}
				}
				Thread.sleep(60005);
			}
		}catch(Exception e) {
			documentor.addError(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void recordTweetRecived(String data) throws IOException {
		File file = new File("C:\\Users\\rzemi\\OneDrive\\Desktop\\TradingBot\\MyTradingBot\\tweetsStorage.txt");
		FileWriter fr = new FileWriter(file, true);
		try {
			fr.write(data + "\n");
			fr.close();
		} catch (IOException e) {
			documentor.addError(e.getMessage());
			e.printStackTrace();
		}
	}
}