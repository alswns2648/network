package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static String SERVER_IP = "192.168.1.15";
	private static int SERVER_PORT = 6000;

	public static void main(String[] args) {
		Scanner scanner = null; // 스캐너사용을 위해 변수 선언
		Socket socket = null;

		try {
			//1. Scanner 생성(표준입력, 키보드)
			scanner = new Scanner(System.in);
			
			//2. 소켓생성
			socket = new Socket();

			//3. 서버연결					
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			//log("connected");

			//4. I/O Stream 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			while(true) {
			//5. 키보드 입력 받기
				System.out.print(">>");
				String line = scanner.nextLine(); // data내용이 들어옴
				//exit 내용이 들어올 시 break(while문 종료)
				if("exit".equals(line)) {
					break;
				}
				//6. data 쓰기(전송)
				pw.println(line);
				
				//7. data 읽기(수신)
				String data = br.readLine(); // blocked
				if(data == null) {
					log(" closed by client");
					break;
				}
//				byte[] buffer = new byte[256];
//				int readByteCount = is.read(buffer); //Blocking
//				if(readByteCount == -1) {
//					System.out.println(" closed by client");
//					return;
//				}
//				line = new String(buffer, 0, readByteCount, "UTF-8");
				//8. 콘솔 출력
				System.out.println("<<" + data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(scanner != null) {
					scanner.close();
				}
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private static void log(String log) {
		System.out.println("[EchoClient]" + log);
	}
}

