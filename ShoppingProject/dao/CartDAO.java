package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.JDBConnection;
import vo.*;

public class CartDAO {

	Connection conn;
	Statement stmt;
	ResultSet rs;
	PreparedStatement pstmt;

	// 장바구니 삽입
	public int insert(String memberId,String productId,String productName) {

		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		int result = 0;

		sb.append(" INSERT INTO CART(SB_ID, MEMBER_ID, PRODUCT_ID,PRODUCT_NAME) "
				+ " VALUES(SEQ_REVIEW.NEXTVAL, ?, ?, ?) ");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, memberId);
			pstmt.setString(2, productId);
			pstmt.setString(3, productName);

			result = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(">> SQL구문 오류입니다!! <<");
		} finally {
			JDBConnection.queryClose();
		}

		return result;
	}
	
	//장바구니 삭제
	
	public int delete(int sbID) {

		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		int result = 0;

		sb.append(" DELETE FROM CART WHERE SB_ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, sbID);

			result = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			System.out.println(">> SQL구문 오류입니다!! <<");
		} finally {
			JDBConnection.queryClose();
		}

		return result;
	}
	
	//해당 제품번호의 가격의 값을 리턴
	public int selectPrice(String proId) {
		int sale = 0;

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PRICE");
		sql.append(" FROM  CART C, PRODUCT D ");
		sql.append("  WHERE C.PRODUCT_ID = D.PRODUCT_ID ");
		sql.append("AND SB_ID = ?");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, proId);
			rs = pstmt.executeQuery();


			while (rs.next()) {
				sale = rs.getInt("PRICE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return sale;
	}
	
	
	
	public int selectStock(String sbId) {
		int sale = 0;

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT STOCK");
		sql.append(" FROM  CART C, PRODUCT D ");
		sql.append("  WHERE C.PRODUCT_ID = D.PRODUCT_ID ");
		sql.append("AND SB_ID = ?");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, sbId);
			rs = pstmt.executeQuery();


			while (rs.next()) {
				sale = rs.getInt("STOCK");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return sale;
	}
	
	
	// PRODUCT_ID 리턴
		public String selectProduct(String sbId) {
			 String id = "";

			conn = JDBConnection.getDBConn();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT PRODUCT_ID");
			sql.append(" FROM  CART ");
			sql.append("  WHERE SB_ID = ?");

			try {
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setString(1, sbId);
				rs = pstmt.executeQuery();


				while (rs.next()) {
					id = rs.getString("PRODUCT_ID");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JDBConnection.queryClose();
			}
			return id;
		}
	
	
	
	//장바구니 전체 조회  모든 장바구니가 조회가됨
	
	public List<CartVO> selectAll(){
		
		
		CartVO cart = null;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();
		List<CartVO> cartList = new ArrayList<>();
		
		sb.append("SELECT * FROM CART");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				cart = new CartVO(rs.getInt(0),
								rs.getString(1),
								rs.getString(2),
								rs.getString(3));
				cartList.add(cart);
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			JDBConnection.queryClose();
		}
		return cartList;
		
	}
	
	// 장바구니 개별 선택 list반환하고 그 리스트 큐에 넣고 큐로 구매하시겠습니까? y/n 반복
	public List<CartVO> selectOne(String sbId) {
		
		CartVO cart = null;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();
		List<CartVO> cartList = new ArrayList<>();
	

		sb.append("SELECT * FROM CART WHERE MEMBER_ID = ?");
		
		try {
			pstmt = conn.prepareStatement(sb.toString());
		
			pstmt.setString(1, sbId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				cart = new CartVO(rs.getInt(1),
								rs.getString(2),
								rs.getString(3),
								rs.getString(4));
				cartList.add(cart);
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			JDBConnection.queryClose();
		}
		return cartList;
		
	}
	
	public String memIdCheck(String carId) {

		String mamId ="";
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT MEMBER_ID FROM CART WHERE SB_ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, carId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				mamId = rs.getString("MEMBER_ID");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return mamId;

	}
	
	
	


}

