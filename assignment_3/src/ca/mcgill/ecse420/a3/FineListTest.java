package ca.mcgill.ecse420.a3;

public class FineListTest {
	private final static int NO_OF_THREADS = 3;
	private final static int LIST_SIZE = 10;
	
	public static void main (String args[]) throws Exception {
		FineList<Integer> fgList = new FineList<Integer>();
		for (int i = 0; i < LIST_SIZE; i++) {
			fgList.add(i);
		}
		fineListTest(fgList);
		System.out.println("Done");
	}
	
	public static void fineListTest(FineList<Integer> myList) throws Exception {
		Thread[] thread = new Thread[NO_OF_THREADS];
		//create start and stop the threads 
		for(int i = 0; i < NO_OF_THREADS; i++) {
			thread[i] = new MyThread(myList);			
		}
		for(int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].start();			
		}
		for(int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].join();			
		}
	}
	
	static class MyThread extends Thread {
		private FineList<Integer> testList;
		
		public MyThread(FineList<Integer> myList) {
			
			this.testList = myList;
		}
		
		//TEST TO CHECK INITIALIZED LIST BEHAVES AS PREDICTED
		public void test1() {
			for (int i = 0; i < 20; i++) {
				System.out.println("Test1:Is "+i+" present in the list=>"+testList.contains(i));
			}
		}
		//TEST TO CHECK IF LIST BEHAVES AS PREDICTED AFTER DOING AN add()
		public void test2() {
			testList.add(13);
			System.out.println("Test2:Is 13 present in the list=>"+testList.contains(13));			
		}		
		//TEST TO CHECK IF LIST BEHAVES AS PREDICTED AFTER DOING a remove()
		public void test3() {
			testList.remove(7);
			System.out.println("Test3:Is 7 present in the list=>"+testList.contains(7));
		}
		
		public void run() {
			test1();
			test2();
			test3();			
		}
	}
}
