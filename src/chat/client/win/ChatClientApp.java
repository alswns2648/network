package chat.client.win;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.1.15";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		String name = null;
		Scanner scanner = new Scanner(System.in);

		while( true ) {

			System.out.println("대화명을 입력하세요.");
			System.out.print(">>> ");
			name = scanner.nextLine();

			if (name.isEmpty() == false ) {
				break;
			}

			System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
		}

		scanner.close();

		//1. create socket
		Socket socket = new Socket();

		//2. connect to server
		try {
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("채팅방에 입장하였습니다.");

			//3. create iostream
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			String request = "join : " + name + "\r\n" ;
			pw.println(request);
		}catch(IOException e) {
			e.printStackTrace();
		}



		//4. join protocol


		new ChatWindow(name).show();
	}
	public static void log(String log) {
		System.out.println("[ChatServer]" + log);
	}
}
