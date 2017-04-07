package echo.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerMain extends JFrame implements ActionListener{
	JPanel p_north;
	JTextField t_port;
	JButton bt_start;
	JTextArea area;
	JScrollPane scroll;
	ServerSocket server;//접속 감지용 소켓
	Socket socket;
	Thread thread;  //서버 가동용 스레드
	BufferedReader buffr;
	BufferedWriter buffw;
	
	int port=7777;
	
	public ServerMain() {
		p_north = new JPanel();
		t_port =new JTextField(Integer.toString(port) ,17);
		bt_start= new JButton("가동");
		area =new JTextArea();
		scroll = new JScrollPane(area);
	
		p_north.add(t_port);
		p_north.add(bt_start);
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		
		bt_start.addActionListener(this);
		setBounds(1100,200,300,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	//서버생성및 가동
	public void startServer(){
		bt_start.setEnabled(false);//불량유저를 방지하기 위해
		//예외의 종류 checked exception, -예외처리 강요
		//runtime exception(unchecked라고도 한데) - 예외처리 안강요 ,강요안한다고 예외처리 안하면 종료됨
		try {
			port = Integer.parseInt(t_port.getText());
			server =new ServerSocket(port);
			area.append("서버 준비됨...\n");
			//가동
			//실행부라 불리는 메인쓰레드는 절대 무한루프나 ,대기,지연 상태로 빠뜨리면 안된다.
			//와이? 실행부는 유저들의 이벤트를 감지한다던가 프로그램을 운영해야하기때문에 
			//무한루프빠지면 지 업무를 못해여
			//스마트폰 개발분야에서는 이코드자체가 허용이 안됨 컴파일부터 에러일으킴
			socket = server.accept();
			area.append("서버 가동\n");
			//클라이언트는 대화를 하기 위해 접속한것이므로
			//접속이 되는 순간 stream을 뽑아내야한다.
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			//클라이언트의 메세지 받기
			String data;
			while(true){
				data =buffr.readLine(); //클라이언트의 메세지 받기
				area.append("클라이언트의 말: "+data+"\n");
				buffw.write(data+"\n");//보내기
				buffw.flush();
			}
			
		} catch (NumberFormatException e) {  //runtime exception
			JOptionPane.showMessageDialog(this, "포트번호는 숫자로 입력해주세여");
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		thread  =  new Thread(){
			public void run() {
				startServer();
			}
		};
		thread.start();
	}
	
	public static void main(String[] args) {
		new ServerMain();

	}
	
}
