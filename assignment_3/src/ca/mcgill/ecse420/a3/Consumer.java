package ca.mcgill.ecse420.a3;

public class Consumer extends Thread {
	private final BoundedQueueArray<Integer> queue1;
	private final BoundedQueueArrayNoLocks<Integer> queue2;
	
	public Consumer(BoundedQueueArray<Integer> queue) {
		this.queue1 = queue;
		this.queue2 = null;
	}
	
	public Consumer(BoundedQueueArrayNoLocks<Integer> queue) {
		this.queue1 = null;
		this.queue2 = queue;
	}
	
	@Override
	public void run() {
		while(true) {
			if(queue1!=null) {
				queue1.deq(); //remove element from queue
			}else {
				queue2.deq();
			}
			try {				
				//wait(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
