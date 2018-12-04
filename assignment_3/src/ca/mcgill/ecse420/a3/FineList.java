package ca.mcgill.ecse420.a3;
import java.util.concurrent.locks.ReentrantLock;

public class FineList<T> {	
	//Node class (Implementation of Fig 9.2 of book)
	//making each node a RentrantLock
	//So adding a lock to each node (fine grained)
	//as opposed to having one lock for entire list (coarse grained)
	private class Node<T> extends ReentrantLock {
		private T item;
		int key;
		private Node next;		 
		//private Lock l = new ReentrantLock();
		
		public Node(T item) {
			this.item = item;
			this.key = item.hashCode();
			this.next = null;
		}
	}

	private Node head;	
	
	//constructor
	public FineList() {
		head = new Node<Integer>(Integer.MIN_VALUE);
		head.next = new Node<Integer>(Integer.MAX_VALUE);	
	}
	
	//add method. From book pg-202 Fig 9.6
	public boolean add(T item) {		
		int key = item.hashCode();
		head.lock();		
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();				
				}
				if(curr.key == key) {
					return false;
				}
				Node newNode = new Node(item);
				newNode.next = curr;
				pred.next = newNode;
				return true;
			}
			finally {
				curr.unlock();
			}
		}
		finally {
			pred.unlock();
		}
	}
	
	//remove method. From book pg-203 Fig 9.7
	public boolean remove(T item) {
		Node pred = null, curr = null;
		int key = item.hashCode();
		head.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock();
			try {
				while(curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if(curr.key == key) {
					pred.next = curr.next;
					return true;
				}
				return false;
			}
			finally {
				curr.unlock();
			}
		}
		finally {
			pred.unlock();
		}
	}
	
	//****contains method for Q2.1****
	//using hand over hand locking
	public boolean contains(T item) {
		Node pred = null, curr = null;
		int key = item.hashCode();
		head.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock();
			try {
				while(curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				//returns true if keys match (item is in list)
				//false otherwise
				return (curr.key == key);
			}
			finally {
				curr.unlock();
			}
		}
		finally {
			pred.unlock();
		}
	}
}
