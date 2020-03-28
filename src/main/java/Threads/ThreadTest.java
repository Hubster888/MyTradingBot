package Threads;

/**
 * Test the threads if they are running correctly
 * */
public class ThreadTest {
	public static void main(String[] args) {
		QueueManagmentThread thread = new QueueManagmentThread();
		thread.start();
		TwitterThread threadTwitter = new TwitterThread();
		threadTwitter.start();
	}
}
