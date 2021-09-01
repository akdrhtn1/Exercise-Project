package dao;

import java.sql.*;
import java.util.Scanner;

import connection.JDBConnection;
import vo.*;

public class MemberDAO {
	Connection conn;
	Statement stmt;
	ResultSet rs;
	PreparedStatement pstmt;
	
	
	// 회원가입 int insert(MemberVO){ }
	public int insert(MemberVO vo) {
		
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();
	
		int result = 0;

		sb.append(" INSERT INTO MEMBER(ID, PASSWORD, NAME, BIRTH, GENDER, PHONE, EMAIL, ADDRESS) " 
						+ " VALUES(?, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?) ");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getBirth());
			pstmt.setInt(5, vo.getGender());
			pstmt.setString(6, vo.getPhone());
			pstmt.setString(7, vo.getEmail());
			pstmt.setString(8, vo.getAddress());

			result = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				System.out.println(">> 아이디가 중복되었습니다. \n");
				result = -1;
			}
			else {
				System.out.println(">> SQL구문 오류입니다!! <<");
				e.printStackTrace();
				result = -2;
			}
		} finally {
			JDBConnection.queryClose();
		}

		return result;
	}
	

	// 로그인 MemberVO login(MemberVO)
	public MemberVO logIn(String id,String password) {
		
		StringBuilder sb = new StringBuilder();
		MemberVO mber = null;

		conn = JDBConnection.getDBConn();

		try {

			sb.append("SELECT ID, PASSWORD, NAME, BIRTH, GENDER, PHONE, EMAIL, ADDRESS, POINT ");
			sb.append("FROM MEMBER ");
			sb.append("WHERE ID = ? AND PASSWORD = ?");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mber = new MemberVO(rs.getString("ID"), 
									rs.getString("PASSWORD"), 
									rs.getString("NAME"), 
									rs.getString("BIRTH"), 
									rs.getInt("GENDER"), 
									rs.getString("PHONE"), 
									rs.getString("EMAIL"), 
									rs.getString("ADDRESS"),
									rs.getInt("POINT"));
			}
			sb.setLength(0);
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return mber;

	}
		
	
	// 회원 정보 수정 int UPDATE(MemberVO)
	public int update(MemberVO vo) {
		conn = JDBConnection.getDBConn();
		StringBuilder sql = new StringBuilder();
		int result = 0;
		try {

			sql.append("UPDATE MEMBER ");
			sql.append("SET PASSWORD = ?,PHONE = ?, "
					+ "EMAIL = ?, ADDRESS = ?");
			sql.append("WHERE id = ?");
			
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, vo.getPassword());
			pstmt.setString(2, vo.getPhone());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getAddress());
			pstmt.setString(5, vo.getId());
			
			result = pstmt.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}
		
	
	// 딜렉트 쿼리문
	public int delete(String id,Scanner sc) {
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();
		int result = 0;
		try {
			sb.append("DELETE FROM MEMBER WHERE ID = ?");
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, id);

			result = pstmt.executeUpdate();

			conn.commit();
			

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}
	
	// 포인트 조회 void point(MemberVO)
	public int point(MemberVO vo) {
		conn = JDBConnection.getDBConn();
		StringBuilder sql = new StringBuilder();
		int point = 0;
		try {
			sql.append("SELECT POINT FROM MEMBER WHERE ID = ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, vo.getId());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				point = rs.getInt("POINT");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBConnection.queryClose();
		}
		return point;
	}
	
	// 구매내역 조회 id입력 => 구매테이블 조회 끝
	
	
	//포인트 사용  + 포인트 구매시 적립(트랜잭션 처리 용도)
	public int pointUpdate(int point,String memId) {
	
		
		int result = 0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append(" UPDATE MEMBER SET POINT = ? WHERE ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, point);
			pstmt.setString(2, memId);

			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}
	
	

	public int pointUpdating(int point,String memId) {
	
		
		int result = 0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append(" UPDATE MEMBER SET POINT = ? WHERE ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, point);
			pstmt.setString(2, memId);

			result = pstmt.executeUpdate();
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}
	
	
	public boolean transection(int t1, int t2, int t3, int t4) {
		boolean check = false;
		if(t1>0 && t2>0 && t3 >0 && t4>0) {
			try {
				check = true;
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return check;
		
	}
	

	
}
