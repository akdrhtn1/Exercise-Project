package connection;

import java.sql.*;

public class JDBConnection {
	// 싱글톤 패턴 적용(메모리 낭비를 줄이기 위해서 사용)

	// private 변수로 자기 잔자신의 클래스 인스턴스를 갖게함
	private static Connection dbConn;
	
	static Statement stmt;
	static ResultSet rs;
	static PreparedStatement pstmt;

	// 생성자를 private 처리를 해서 외부의 인스턴스화를 막음
	private JDBConnection() {
	}
	
	public static Connection getDBConn() {
		// 사용 시점에만 인스턴스화
		if (dbConn == null) {
			// dbConn 이 null이면 연결된것이 없으므로 연결
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				dbConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", 
						 "shopping", "shopping");
				dbConn.setAutoCommit(false); // 수동 commit 으로 전환
				
			} catch (ClassNotFoundException e) {
				System.out.println(">> 드라이버가 없습니다.");
			} catch (SQLException e) {
				System.out.println(">> SQL문법 오류");
				e.printStackTrace();
			}
		}

		return dbConn;
	}

	// DB를 다썻으니 자원을 반납
	public static void close() {
		try {
			if (dbConn != null)
				if (!dbConn.isClosed()) {
					dbConn.close();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryClose() {

		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
