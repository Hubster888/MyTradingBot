package Threads;

import QueueManager.*;
import Strategies.CopyTwitterStrategy;

/**
 * Loops over the order queue checking if there
 * are any new requests.
 * If a new request is found it's handled by the appropriate
 * queue manager.
 * */
public class QueueManagmentThread extends Thread{
	
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
					if(orderCancelQueue.executeCancel()) {System.out.println("Order cancelled");}
				}
				if(!orderCreateQueue.isEmpty()) {
					if(orderCreateQueue.executeRequest()) {System.out.println("Order created");}else {System.out.println("create order failed");}
				}
				if(!orderChangeQueue.isEmpty()) {
					if(orderChangeQueue.executeChange()) {System.out.println("Order is changed");}
				}
				if(!tradeChangeQueues.tradesToCancelIsEmpty()) {
					if(tradeChangeQueues.executeCancel()) {System.out.println("Trade is cancelled");}
				}
				if(!tradeChangeQueues.changeStopLossQueueIsEmpty()) {
					if(tradeChangeQueues.executeStopLoss()) {System.out.println("Trade's stop loss changed");}
				}
				if(!tradeChangeQueues.changeTakeProfitQueueIsEmpty()) {
					if(tradeChangeQueues.executeTakeProfit()) {System.out.println("Trade's take profit changed");}
				}
				Thread.sleep(100);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
