package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;

	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// get IOStream
			OutputStream outputStream = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );

			String request = null;

			while(true) {
				String line = br.readLine(); // 한 라인씩 읽는다

				//브라우저가 연결을 끊으면
				if(line == null) {
					break;
				}

				//브라우저가 연결 안끊으면
				//header만 읽음
				if("".equals(line)) { // 비어있으면 while 탈출
					break;
				}

				//첫번쨰 라인만 남김
				if(request == null) {
					request = line;
					break;
				}
			}

			String[] tokens = request.split(" ");
			if("GET".equals(tokens[0])) {
				consoleLog("request : " + request);
				responseStaticResource(outputStream, tokens[1], tokens[2]);
			}else {
				//POST, PUT, DELETE 명령들은 무시
				consoleLog("bad request : " + request);
				response400Error(outputStream,  tokens[2]);
			}

			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			//			outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			//			outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			//			outputStream.write( "\r\n".getBytes() );
			//			outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}

			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}


	private void responseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException {

		if("/".equals(url)) {
			url = "/index.html";
		}

		File file = new File("./webapp" + url);
		if(file.exists() == false) {
			consoleLog("File Not Found : " + url);
			response404Error(outputStream, protocol);
			return;
		}

		//n(new)io
		byte[] body = Files.readAllBytes(file.toPath());//한번에 읽어낼 수 있는 함수
		String contentType = Files.probeContentType(file.toPath());

		//응답
		outputStream.write( (protocol +  " 200 OK\r\n").getBytes( "UTF-8" ) ); 
		outputStream.write( ("Content-Type: " + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() ); //까지 해더(/r/n 두번연속 이후로 body)
		outputStream.write( body ); // body 출력
	}

	private void response400Error(OutputStream outputStream, String protocol) throws IOException {

		File file = new File("./webapp/error/400.html");

		//n(new)io
		byte[] body = Files.readAllBytes(file.toPath());//한번에 읽어낼 수 있는 함수
		String contentType = Files.probeContentType(file.toPath());

		//응답
		outputStream.write( (protocol +  " 400 Bad Request\r\n").getBytes( "UTF-8" ) ); 
		outputStream.write( ("Content-Type: " + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() ); //까지 해더(/r/n 두번연속 이후로 body)
		outputStream.write( body ); // body 출력

	}

	private void response404Error(OutputStream outputStream, String protocol) throws IOException {

		File file = new File("./webapp/error/404.html");

		//n(new)io
		byte[] body = Files.readAllBytes(file.toPath());//한번에 읽어낼 수 있는 함수
		String contentType = Files.probeContentType(file.toPath());

		//응답
		outputStream.write( (protocol +  " 404 File Not Found\r\n").getBytes( "UTF-8" ) ); 
		outputStream.write( ("Content-Type: " + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() ); //까지 해더(/r/n 두번연속 이후로 body)
		outputStream.write( body ); // body 출력

	}

	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
