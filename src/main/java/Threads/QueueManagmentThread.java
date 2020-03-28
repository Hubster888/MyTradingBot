package Threads;

<<<<<<< HEAD
import java.util.Date;

import Documenting.SendReport;
import MyTradingBot.Main;
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
import QueueManager.*;
import Strategies.CopyTwitterStrategy;

/**
 * Loops over the order queue checking if there
 * are any new requests.
 * If a new request is found it's handled by the appropriate
 * queue manager.
 * */
public class QueueManagmentThread extends Thread{
<<<<<<< HEAD

=======
	
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
	public void run() {
		try {
			System.out.println("Queue managment thread is running"); 
			// All the queue managers are initiated
			OrderCreateRequestQueue orderCreateQueue = new OrderCreateRequestQueue();
			OrderCancelRequestQueue orderCancelQueue = new OrderCancelRequestQueue();
			OrderChangeRequestQueue orderChangeQueue = new OrderChangeRequestQueue();
			TradeChangeRequestQueue tradeChangeQueues = new TradeChangeRequestQueue();
<<<<<<< HEAD
			
			while(true) {
				while(Main.getIsOn()) { //The while loop will check if any of the queues is not empty and then execute the next task in the queue
					if(!CopyTwitterStrategy.getQueue().isEmpty()) {
						CopyTwitterStrategy.executeTweet();
					}
					if(!orderCancelQueue.isEmpty()) {
						if(orderCancelQueue.executeCancel()) {System.out.println("Order cancelled");} else {SendReport.addError("order cancel failed at " + new Date());}
					}
					if(!orderCreateQueue.isEmpty()) {
						if(orderCreateQueue.executeRequest()) {System.out.println("Order created");} else {System.out.println("order not created"); SendReport.addError("order crate failed at " + new Date());}
					}
					if(!orderChangeQueue.isEmpty()) {
						if(orderChangeQueue.executeChange()) {System.out.println("Order is changed");} else {SendReport.addError("order change failed at " + new Date());}
					}
					if(!tradeChangeQueues.tradesToCancelIsEmpty()) {
						if(tradeChangeQueues.executeCancel()) {System.out.println("Trade is cancelled");} else {SendReport.addError("trade cancel failed at " + new Date());}
					}
					if(!tradeChangeQueues.changeStopLossQueueIsEmpty()) {
						if(tradeChangeQueues.executeStopLoss()) {System.out.println("Trade's stop loss changed");} else {SendReport.addError("trade change stop loss failed at " + new Date());}
					}
					if(!tradeChangeQueues.changeTakeProfitQueueIsEmpty()) {
						if(tradeChangeQueues.executeTakeProfit()) {System.out.println("Trade's take profit changed");} else {SendReport.addError("trade change take profit failed at " + new Date());}
					}
					Thread.sleep(1000);
				}
			}
		}catch(Exception e) {
			SendReport.addError(e.getMessage());
=======
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
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
			e.printStackTrace();
		}
	}
}
