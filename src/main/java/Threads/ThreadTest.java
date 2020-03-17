package Threads;

/**
 * Test the threads if they are running correctly
 * */
public class ThreadTest {
	public static void main(String[] args) {
		QueueManagmentThread thread = new QueueManagmentThread();
		thread.run();
		TwitterThread threadTwitter = new TwitterThread();
		threadTwitter.run();
	}
}
