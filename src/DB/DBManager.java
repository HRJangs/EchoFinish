package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*1.정보를 한곳에 두기
 * 데이터 베이스 계정정보를 중복해서 기재하기 않기 위함(db연동을 하는 각각의 클래스에서...)
 * 
 * 2. 인스턴스의 갯수를 한개만 둬보기
 * 어플리케이션 가동중 생성되는 connection 객체를 하나로 통일하기 위함
 * 
*/
public class DBManager {
	
	private static DBManager instance;
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="batman";
	private String password="1234";
	private Connection con;
	
	private DBManager() {
		/*
		 * 1.드라이버 로드
		 * 2.접속
		 * 3.쿼리문 수행
		 * 4.반납,해제
		 * */
		try {
			Class.forName(driver);
			con =DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	public static DBManager getInstance() {
		if(instance==null){
			instance =new DBManager();
		}
		return instance;
	}
	public Connection getConnection(){
		return con;
	}
	public void disConnection(Connection con){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
}
