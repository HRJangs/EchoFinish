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
	ServerSocket server;//���� ������ ����
	Socket socket;
	Thread thread;  //���� ������ ������
	BufferedReader buffr;
	BufferedWriter buffw;
	
	int port=7777;
	
	public ServerMain() {
		p_north = new JPanel();
		t_port =new JTextField(Integer.toString(port) ,17);
		bt_start= new JButton("����");
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
	//���������� ����
	public void startServer(){
		bt_start.setEnabled(false);//�ҷ������� �����ϱ� ����
		//������ ���� checked exception, -����ó�� ����
		//runtime exception(unchecked��� �ѵ�) - ����ó�� �Ȱ��� ,������Ѵٰ� ����ó�� ���ϸ� �����
		try {
			port = Integer.parseInt(t_port.getText());
			server =new ServerSocket(port);
			area.append("���� �غ��...\n");
			//����
			//����ζ� �Ҹ��� ���ξ������ ���� ���ѷ����� ,���,���� ���·� ���߸��� �ȵȴ�.
			//����? ����δ� �������� �̺�Ʈ�� �����Ѵٴ��� ���α׷��� ��ؾ��ϱ⶧���� 
			//���ѷ��������� �� ������ ���ؿ�
			//����Ʈ�� ���ߺо߿����� ���ڵ���ü�� ����� �ȵ� �����Ϻ��� ��������Ŵ
			socket = server.accept();
			area.append("���� ����\n");
			//Ŭ���̾�Ʈ�� ��ȭ�� �ϱ� ���� �����Ѱ��̹Ƿ�
			//������ �Ǵ� ���� stream�� �̾Ƴ����Ѵ�.
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			//Ŭ���̾�Ʈ�� �޼��� �ޱ�
			String data;
			while(true){
				data =buffr.readLine(); //Ŭ���̾�Ʈ�� �޼��� �ޱ�
				area.append("Ŭ���̾�Ʈ�� ��: "+data+"\n");
				buffw.write(data+"\n");//������
				buffw.flush();
			}
			
		} catch (NumberFormatException e) {  //runtime exception
			JOptionPane.showMessageDialog(this, "��Ʈ��ȣ�� ���ڷ� �Է����ּ���");
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
