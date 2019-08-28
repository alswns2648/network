package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class ChatServerThread extends Thread {
	private String nickname;
	private Socket socket;
	private List<Writer> listWriters;


	public ChatServerThread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;

	}

	@Override
	public void run() {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8" ));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);

			//요청 처리
			while(true) {
				String request = br.readLine();

				if(request == null ) {
					ChatServer.log("클라이언트로 부터 연결 끊김");
					doQuit(pw);
					break;
				}

				String[] tokens = request.split(":");

				if("join".equals(tokens[0])) {
					doJoin(tokens[1], pw);
				}else if("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])) {
					doQuit(pw);
					break;
				}else {
					ChatServer.log("에러 : 알 수 없는 요청( " + tokens[0] + " )" );
				}

			}

		}catch(SocketException e) {
			ChatServer.log("abnormal closed");
		}
		catch(IOException e) {
			e.printStackTrace();
		} finally { 
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private void doQuit(Writer writer) {
		removeWriter(writer);
		String data = nickname + "님이 퇴장하셨습니다.";
		broadcast(data);
	}


	private void doMessage(String message) {
		broadcast(this.nickname + ":" + message);
	}

	private void doJoin(String nickName, Writer writer) {
		this.nickname = nickName;
		String data = nickName + "님이 참여하였습니다.";
		broadcast(data);
		PrintWriter pw = (PrintWriter)writer;
		addWriter(writer);
		pw.println("입장완료");
		pw.flush();
	}

	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}

	private void removeWriter(Writer writer) {
		synchronized(listWriters) {
			listWriters.remove(writer);
		}

	}

	private void broadcast(String data) {
		synchronized(listWriters) {
			for(Writer writer : listWriters) {
				PrintWriter pw = (PrintWriter)writer;
				pw.println(data);
				pw.flush();
			}
		}
	}

}

