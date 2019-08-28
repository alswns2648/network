package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import echo.EchoServer;

public class ChatClientThread extends Thread {
	private BufferedReader br;
	private Socket socket;

	public ChatClientThread(Socket socket, BufferedReader br) {
		this.socket = socket;
		this.br = br;
	}

	@Override
	public void run() {
		try {
			while(true) {
				String data = br.readLine();
				if(data == null) {
					break;
				}
				System.out.println(data);
			}

		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (this.socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}

		}
	}	
}



