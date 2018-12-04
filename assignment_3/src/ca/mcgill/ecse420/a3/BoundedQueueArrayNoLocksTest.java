package ca.mcgill.ecse420.a3;

public class BoundedQueueArrayNoLocksTest {
	
	public static void main(String[] args) throws InterruptedException {
		BoundedQueueArrayNoLocks<Integer> sharedQueue = new BoundedQueueArrayNoLocks<Integer>(10);
		
		//Create a producer and a consumer.
		Thread producer = new Producer(sharedQueue);
		Thread consumer = new Consumer(sharedQueue);
		
		//Start both threads.
		producer.start();
		consumer.start();
		
		//Wait for both threads to terminate.
		producer.join();
		consumer.join();
	}
}
