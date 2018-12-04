package ca.mcgill.ecse420.a3;

public class Producer extends Thread {
	
	private final BoundedQueueArray<Integer> queue1;
	private final BoundedQueueArrayNoLocks<Integer> queue2;
	
	public Producer(BoundedQueueArray<Integer> queue) {
		this.queue1 = queue;
		this.queue2 = null;
	}
	
	public Producer(BoundedQueueArrayNoLocks<Integer> queue) {
		this.queue1 = null;
		this.queue2 = queue;
	}
	
	@Override
	public void run() {
		while(true) {
			if(queue1!=null) {
				queue1.enq(1); //add a 1 into the queue
			}else {
				queue2.enq(1);
			}
				
			try {				
				//wait(5);
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
	}
}