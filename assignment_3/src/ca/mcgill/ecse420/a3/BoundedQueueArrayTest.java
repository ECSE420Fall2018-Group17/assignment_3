package ca.mcgill.ecse420.a3;

public class BoundedQueueArrayTest {

	public static void main(String[] args) throws InterruptedException {
		BoundedQueueArray<Integer> sharedQueue = new BoundedQueueArray<Integer>(10);
		
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