package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundedQueueArrayNoLocks<T> {
    private T[] arrayElements; 
    private AtomicInteger head; 
    private AtomicInteger tail; 
    private AtomicInteger size; 
    private int capacity;


    public BoundedQueueArrayNoLocks(int capacity) {        
    	this.capacity = capacity;
    	this.arrayElements = (T[]) new Object[capacity]; 
        this.size = new AtomicInteger(0);
        this.head = new AtomicInteger(0); 
        this.tail = new AtomicInteger(0);
    }

    public void enq(T x){
    	try {
    		while(size.get() == capacity) {};    	
    		arrayElements[tail.getAndIncrement()%arrayElements.length] = x;
    		System.out.println("Added to tail at Index:"+tail.get()+" and then index incremented");
    		size.getAndIncrement();
    	}catch(Exception e) {				
			e.printStackTrace();
		}
    }

    public T deq(){
        T result = null;
        try {
        	while(size.get() == 0) {};        
        	result = arrayElements[head.getAndIncrement()%arrayElements.length];
        	System.out.println("Removed from head at Index:"+head.get()+" and then index incremented");
        	size.getAndDecrement();       
        	
        }catch(Exception e) {				
			e.printStackTrace();
		}
		return result;		       
    }
}