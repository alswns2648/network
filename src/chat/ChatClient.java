package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient {
	//private static final String SERVER_IP = "192.168.1.15";
	public static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		Scanner scanner = null;
		Socket socket = null;

		try {
			//1키보드 연결
			scanner = new Scanner(System.in);
			//2소켓생성
			socket = new Socket();
			//3연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			//reader/writer 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);

			//5 join 프로토콜
			System.out.print("닉네임>>");
			String nickname = scanner.nextLine();
			if("".equals(nickname)) {
				nickname = " ";
			}
			pw.println("join:" + nickname);
			pw.flush();

			//thread 시작
			new ChatClientThread(socket, br).start();


			//키보드 입력처리
			while(true) {
				String input = scanner.nextLine();

				if("quit".equals(input)) {
					pw.println("quit:");
					pw.flush();
					break;
				}
				if("".equals(input)) {
					input = " ";
				}
				pw.println("message:" + input);
				pw.flush();


			}

		}catch(IOException e) {
			log("error : " + e );


		}finally {
			if(scanner!=null)
				scanner.close();

		}
	}


	public static void log(String log) {
		System.out.println("[ChatClient]" + log);

	}
}
