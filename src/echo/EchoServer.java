package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 6000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			//1. 서버소켓 생성
			serverSocket = new ServerSocket();

			//2. Binding:
			//   Socket에 SocketAddress(IPAddress + Port)
			InetAddress inetAddress = InetAddress.getLocalHost();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			EchoServer.log(" 연결 기다림 ");

			//3. accept:
			//   클라이언트로 부터 연결요청(Connect)을 기다린다.
			while(true) {
				Socket socket = serverSocket.accept(); // Blocking
				new EchoServerReceiveThread(socket).start();
			}

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			//8. Server Socket 자원정리
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
	}
	public static void log(String log) {
		System.out.println("[EchoServer#" + Thread.currentThread().getId() + "]" + log);
	}
}
