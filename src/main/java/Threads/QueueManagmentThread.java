package Threads;

import java.util.Date;

import Documenting.Documentor;
import QueueManager.*;
import Strategies.CopyTwitterStrategy;

/**
 * Loops over the order queue checking if there
 * are any new requests.
 * If a new request is found it's handled by the appropriate
 * queue manager.
 * */
public class QueueManagmentThread extends Thread{
	private final Documentor documentor = new Documentor();
	
	public void run() {
		try {
			System.out.println("Queue managment thread is running"); 
			// All the queue managers are initiated
			OrderCreateRequestQueue orderCreateQueue = new OrderCreateRequestQueue();
			OrderCancelRequestQueue orderCancelQueue = new OrderCancelRequestQueue();
			OrderChangeRequestQueue orderChangeQueue = new OrderChangeRequestQueue();
			TradeChangeRequestQueue tradeChangeQueues = new TradeChangeRequestQueue();
			while(true) { //The while loop will check if any of the queues is not empty and then execute the next task in the queue
				if(!CopyTwitterStrategy.getQueue().isEmpty()) {
					CopyTwitterStrategy.executeTweet();
				}
				if(!orderCancelQueue.isEmpty()) {
					if(orderCancelQueue.executeCancel()) {System.out.println("Order cancelled");} else {documentor.addError("order cancel failed at " + new Date());}
				}
				if(!orderCreateQueue.isEmpty()) {
					if(orderCreateQueue.executeRequest()) {System.out.println("Order created");} else {documentor.addError("order crate failed at " + new Date());}
				}
				if(!orderChangeQueue.isEmpty()) {
					if(orderChangeQueue.executeChange()) {System.out.println("Order is changed");} else {documentor.addError("order change failed at " + new Date());}
				}
				if(!tradeChangeQueues.tradesToCancelIsEmpty()) {
					if(tradeChangeQueues.executeCancel()) {System.out.println("Trade is cancelled");} else {documentor.addError("trade cancel failed at " + new Date());}
				}
				if(!tradeChangeQueues.changeStopLossQueueIsEmpty()) {
					if(tradeChangeQueues.executeStopLoss()) {System.out.println("Trade's stop loss changed");} else {documentor.addError("trade change stop loss failed at " + new Date());}
				}
				if(!tradeChangeQueues.changeTakeProfitQueueIsEmpty()) {
					if(tradeChangeQueues.executeTakeProfit()) {System.out.println("Trade's take profit changed");} else {documentor.addError("trade change take profit failed at " + new Date());}
				}
				Thread.sleep(1000);
			}
		}catch(Exception e) {
			documentor.addError(e.getMessage());
			e.printStackTrace();
		}
	}
}
