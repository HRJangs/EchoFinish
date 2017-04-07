package uni.client;
/*
 * �ǽð� û�븦 ���� ���ν����尡 �ƴ� 
 * ������ ���� �����带 ������ ������
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;


public class ClientThread extends Thread{
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;
	
	public ClientThread(Socket socket, JTextArea area) {
		this.socket = socket;
		this.area =  area;
		try {
			buffr= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//���ϱ�,������
	public void  send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//���,�ޱ�
	public void listen(){
		String msg = null;
		try {
			msg =buffr.readLine();
			send(msg);//��ٷ� �ٽ� ������
			area.append(msg+"\n");
			send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while(true){
			listen();
		}
	}
}