package ca.mcgill.ecse420.a3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

//the code is modeled after the code given in book for the list
//implementation on pages 225-229(Section 10.3, Fig:10.2 to 10.5)
//variable names have been kept the same where applicable
public class BoundedQueueArray<T> {
	private T[] arrayElements;
	private int head;
	private int tail;
	//Two locks to allow parallelism. 
	//using two locks instead of one ensures that enqueuer does not lock
	//out dequeuer unnecessarily and vice versa	
	private Lock enqLock, deqLock;
	private Condition notFullCondition, notEmptyCondition;
	private AtomicInteger size;
	private int capacity;
	
	//Constructor
	public BoundedQueueArray(int capacity) {
		this.capacity = capacity;
		this.head = 0;
		this.tail = 0;
		this.size = new AtomicInteger(0);
		this.arrayElements = (T[]) new Object[capacity];		
		this.enqLock = new ReentrantLock();
		this.notFullCondition = enqLock.newCondition();
		this.deqLock = new ReentrantLock();
		this.notEmptyCondition = deqLock.newCondition();		
	}
	
	public void enq(T x) {
		boolean mustWakeDequeuers = false;
		enqLock.lock();
		try {			
			while(size.get() == capacity) {			
				try {
					System.out.println(Thread.currentThread().getName()+":Queue is full, waiting");
					notFullCondition.await(); //wait until queue is Not Full
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//enqueue new element at the end (modulo since its a bounded array)
			arrayElements[tail % arrayElements.length] = x;
			System.out.println("Added to tail at Index:"+(tail% arrayElements.length));
			tail++;
			System.out.println("Tail Index Incremented to:"+(tail%arrayElements.length));
			if(size.getAndIncrement() == 0) { //Atomically increments size, returns previous value
				mustWakeDequeuers = true;
			}			
		}finally {
			enqLock.unlock();
		}
		if(mustWakeDequeuers) {
			deqLock.lock();
			try {
				System.out.println(Thread.currentThread().getName()+":Signalling Consumers:Queue not Empty");
				notEmptyCondition.signalAll();
			}finally {
				deqLock.unlock();
			}
		}
	}
	
	public T deq() {
		T result;
		boolean mustWakeEnqueuers = false;
		deqLock.lock();
		try {
			while(size.get() == 0) {
			//while(tail-head == 0) {
				try {
					System.out.println(Thread.currentThread().getName()+":Queue is empty, waiting");
					notEmptyCondition.await();
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
			}
			//get item to dequeue and increment head
			result = arrayElements[head % arrayElements.length];
			System.out.println("Removed from head at Index:"+(head%arrayElements.length));
			head++;
			System.out.println("Changed Head Index:"+(head%arrayElements.length));
			if(size.getAndDecrement() == capacity){
			//if(tail-head == arrayElements.length-1) {
				mustWakeEnqueuers = true;
			}						
		}finally {
			deqLock.unlock();
		}
		if (mustWakeEnqueuers) {
			enqLock.lock();
			try {
				System.out.println(Thread.currentThread().getName()+":Signalling Producers:Queue not Full");
				notFullCondition.signalAll();
			}finally {
				enqLock.unlock();
			}
		}
		return result;
	}	
}