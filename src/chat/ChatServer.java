package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 7000;
	private static List<Writer> listWriters ;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			//1. 서버소켓 생성
			serverSocket = new ServerSocket();
			listWriters = new ArrayList<Writer>();

			//2.바인딩
			//String inetAddress = InetAddress.getLocalHost().getHostAddress();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
			serverSocket.bind(inetSocketAddress);
			log("연결 기다림 " + ChatClient.SERVER_IP + ":" + PORT);


			//3.요청 대기
			while(true) {
				Socket socket = serverSocket.accept();
				new ChatServerThread( socket, listWriters ).start();
			}

		}catch (IOException e) {
			e.printStackTrace();

		}finally {
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(String log) {
		System.out.println("[ChatServer]" + log);
	}
}

