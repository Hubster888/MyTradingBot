package Threads;

/**
 * Test the threads if they are running correctly
 * */
public class ThreadTest {
	public static void main(String[] args) {
		QueueManagmentThread thread = new QueueManagmentThread();
<<<<<<< HEAD
		thread.start();
		TwitterThread threadTwitter = new TwitterThread();
		threadTwitter.start();
=======
		thread.run();
		TwitterThread threadTwitter = new TwitterThread();
		threadTwitter.run();
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
	}
}
