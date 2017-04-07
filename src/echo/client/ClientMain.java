package echo.client;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DB.DBManager;

public class ClientMain extends JFrame implements ItemListener,ActionListener{

	JPanel p_north;
	Choice choice;
	JTextField t_port,t_input;
	JButton bt_connect;
	JTextArea area;
	JScrollPane scroll;
	DBManager manager;
	int port =7777;
	ArrayList<Chat> list=new ArrayList<Chat>(); 
	Socket socket;//대화용 소켓 따라서 스트림을 뽑아 낼거임
	String ip;
	BufferedWriter buffw;
	BufferedReader buffr;
	ClientThread clientThread;
	public ClientMain() {
		p_north=new JPanel();
		choice =new Choice();
		t_input = new JTextField(15);
		t_port =new JTextField(Integer.toString(port),5);
		bt_connect =new JButton("접속");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		manager = DBManager.getInstance();
		
		p_north.add(choice);
		p_north.add(t_port);
		p_north.add(bt_connect);
		
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		add(t_input,BorderLayout.SOUTH);
		
		loadIP();
		for(int i=0;i<list.size();i++){
			choice.add(list.get(i).getName());
		}
		
		
		choice.addItemListener(this);
		bt_connect.addActionListener(this);
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				
				if(key==KeyEvent.VK_ENTER){
					String msg =t_input.getText();
					System.out.println(msg);
					clientThread.send(msg);
					t_input.setText("");//입력한 글씨 지우기				
				}
			}
		});
		
		setTitle(list.get(0).getIp());
		setVisible(true);
		setBounds(600,200,300,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	//데이터 베이스 가져오기
	public void loadIP(){
		Connection con = manager.getConnection();
		PreparedStatement pstmt= null;
		ResultSet rs =null;
		String sql = "select * from chat order by chat_id asc";
		
		try {
			pstmt=  con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			//rs의 모든 데이터를 dto로 옮기는 과정(mapping)
			while(rs.next()){
				Chat chat = new Chat();
				chat.setChat_id(rs.getInt(1));
				chat.setName(rs.getString(2));
				chat.setIp(rs.getString(3));
				list.add(chat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			manager.disConnection(con);
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
	
		Choice ch =(Choice) e.getSource();
		int index = ch.getSelectedIndex();
		Chat chat = list.get(index);
		
	
		setTitle(chat.getIp());
		ip=chat.getIp();
	}
	//서버의 접속을 시도한다
	public void connect(){
		System.out.println(ip);
		try {
			port = Integer.parseInt(t_port.getText());
			socket = new Socket(ip, port);
			
			//실시간으로 서버의 메세지를 청취하기 위해, 스레드를 생성하여
			//대회업무를 다 맡겨버리자
			//따라서 종이컵&실의 보유자는 동생
			clientThread = new ClientThread(socket,area);
			clientThread.start();
			clientThread.send("안녕?");
	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	
	
	
	public void actionPerformed(ActionEvent e) {
		connect();
	}
	
	
	public static void main(String[] args) {
		new ClientMain();

	}



}
