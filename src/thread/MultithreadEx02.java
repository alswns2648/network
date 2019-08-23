package thread;

public class MultithreadEx02 {

	public static void main(String[] args) {
		
		Thread thread1 = new DigitThread(); // thread 객체생성(숫자)
		Thread thread2 = new AlphabetThread(); // thread 객체생성(알파벳)
		
		thread1.start();
		thread2.start();
	}

}
