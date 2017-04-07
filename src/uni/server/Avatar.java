package uni.server;
/*
 * 소켓과 스트림을 서버에 한개만 두었더니
 * 접속자마다 소켓과 스트림을 빼앗아 버려 쟁탈전이 벌어진다.
 * 소켓과 스트림이 유지가 되지 않고 있다.
 * 각 사용자 마다 자신만의 소켓과 스트림이 각각 필요하므로, 접속자마다 인스턴스를 생성하여
 * 그 인스턴스 안에 각각의 소켓과 스트림들을 보관해놓자.
 * 별도의 독립된 동작을 일으켜야 하기때문에 쓰레드로 정의하자.
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class Avatar extends Thread{
	Socket socket;
	BufferedReader  buffr;
	BufferedWriter buffw;
	JTextArea area;
	
	public Avatar(Socket socket,JTextArea area) {
		this.socket= socket;
		this.area = area;
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//프로그램이 종료될때까지 끝없이 클라이언트에 메세지를 받아와서 다시 보낸다.
	public void run() {
		while(true){
			listen();
		}
	}
	//듣고
	public void listen(){
		String msg = null;
		try {
			msg =buffr.readLine();
			send(msg);//곧바로 다시 보내기
			area.append(msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//말하고
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
