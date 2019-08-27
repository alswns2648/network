package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}


	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		EchoServer.log(" 연결됨 from " + inetRemoteSocketAddress.getAddress().getHostAddress() + ":" + inetRemoteSocketAddress.getPort());

		try {
			//4. I/O Stream 생성
			//InputStream is = socket.getInputStream() >> 변수화 안시키고 바로 bufferreader 삽입
			//OutputStream os = socket.getOutputStream(); 

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); // true : buffer가 꽉 찰때가 아니라 쓰는즉시 보내라

			while(true) {

				//5. 데이터 읽기(수신)
				String data = br.readLine();
				if(data == null) {
					EchoServer.log(" 클라이언트로 부터 연결 끊김");
					break;
				}
				
				//				byte[] buffer = new byte[256];
				//				int readByteCount = is.read(buffer); //Blocking
				//				if(readByteCount == -1) {
				//					// 정상종료: remote socket이 close()
				//					//         메소드를 통해서 정상적으로 소켓을 닫은 경우
				//					break;
				//				}
				//				String data = new String(buffer, 0, readByteCount, "UTF-8");
				EchoServer.log(" 데이터 수신 : " + data);

				//6. 데이터 쓰기 (송신)
				pw.println(data);
			}

		} catch(SocketException e) {
			EchoServer.log(" abnormal closed by client");
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			//7. Socket 자원정리
			if(socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} 
}


