package thread;

public class MultithreadEx03 {
	
	public static void main(String[] args) {
		
		Thread thread1 = new DigitThread(); // thread 객체생성(숫자)
		Thread thread2 = new AlphabetThread(); // thread 객체생성(알파벳)
		Thread thread3 = new Thread(new UppercaseAlphabetRunnableImpl()); // runnable 인터페이스를 구현한것(run을 override)이 들어오면 thread에 탐
		
		thread1.start();
		thread2.start();
		thread3.start();
		
	}
}
