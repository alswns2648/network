package thread;

public class MultithreadEx01 {

	public static void main(String[] args) {

//		for(int i = 0; i < 10; i++) {
//			System.out.print(i);
//			Thread.sleep(1000);
//		}
		
		Thread digitThread = new DigitThread();
		digitThread.start();

		for(char c = 'a'; c < 'z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
