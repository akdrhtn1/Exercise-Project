package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.JDBConnection;
import vo.*;

public class ManagerDAO {
	Connection conn;
	Statement stmt;
	ResultSet rs;
	PreparedStatement pstmt;
	
	// 매니저 등록
	public int insert(ManagerVO vo) {

		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		int result = 0;

		sb.append(" INSERT INTO MANAGER(ID,PASSWORD,NAME,PHONE) ");
		sb.append(" VALUES( ?, ?, ?, ?)");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getPhone());

			result = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			if (e.getErrorCode() == 1) {
				System.out.println(">> 아이디가 중복되었습니다. \n");
				result = -1;
			} else {
				System.out.println(">> SQL구문 오류입니다!! <<");
				e.printStackTrace();
				result = -2;
			}
		} finally {
			JDBConnection.queryClose();
		}

		return result;
	}
	
	// 로그인---------------------------------------
	public ManagerVO logIn(String id, String password) {
		
		StringBuilder sb = new StringBuilder();
		ManagerVO mana = null;

		conn = JDBConnection.getDBConn();

		try {

			sb.append("SELECT ID, PASSWORD, NAME, PHONE ");
			sb.append("FROM MANAGER ");
			sb.append("WHERE ID = ? AND PASSWORD = ?");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mana = new ManagerVO(rs.getString("ID"), 
									rs.getString("PASSWORD"), 
									rs.getString("NAME"), 
									rs.getString("PHONE")); 
			sb.setLength(0);

			} 
		}catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return mana;

	}
	
	//update 수정 --------------------------
	
	public int update(ManagerVO vo) {
		conn = JDBConnection.getDBConn();
		StringBuilder sql = new StringBuilder();
		int result = 0;
		try {

			sql.append("UPDATE MANAGER ");
			sql.append("SET PASSWORD = ?, NAME = ?, PHONE = ? ");
			sql.append("WHERE id = ?");
			
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, vo.getPassword());
			pstmt.setString(2, vo.getName());
			pstmt.setString(3, vo.getPhone());
			pstmt.setString(4, vo.getId());
		
			
			result = pstmt.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
			
		}
		return result;
	}
		
	
	   //admin 전화번호 반환
	   public String managerPhone(String str) {
	      String phone = null;
	      
	      StringBuilder sb = new StringBuilder();
	      conn = JDBConnection.getDBConn();
	      
	      try {
	         sb.append("SELECT PHONE FROM MANAGER ");
	         sb.append(" WHERE ID = ? ");
	         pstmt = conn.prepareStatement(sb.toString());
	         pstmt.setString(1, str);
	         
	         rs = pstmt.executeQuery();
	         
	         while(rs.next()) {
	            phone = rs.getString("PHONE");
	         }
	         
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         JDBConnection.queryClose();
	      }
	      return phone;
	   }
	   
	//관리자 돈받고 뺴는 메소드 만들기 
	
	public int moneyModify(int money,String managerId) {
		conn = JDBConnection.getDBConn();
		StringBuilder sql = new StringBuilder();
		int result = 0;
		try {

			sql.append("UPDATE MANAGER ");
			sql.append("SET MONEY = ?");
			sql.append("WHERE ID = ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setInt(1, money);
			pstmt.setString(2, managerId);;
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
			
		}
		return result;
	}

	//admin 돈을 조회 갖고옴
	public int moneySearch(String adminId) {

		StringBuilder sb = new StringBuilder();
		int money = 0;
		conn = JDBConnection.getDBConn();

		try {

			sb.append("SELECT MONEY ");
			sb.append("FROM MANAGER ");
			sb.append("WHERE ID = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, adminId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				money = rs.getInt("MONEY");
			sb.setLength(0);

			} 
		}catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return money;

	}
}
	
	



	

