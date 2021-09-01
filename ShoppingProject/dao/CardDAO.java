package dao;

import java.sql.*;
import java.util.Scanner;

import connection.JDBConnection;
import vo.*;


public class CardDAO {
	// ----------------------------카드 생성(insert)

	Connection conn;
	Statement stmt;
	ResultSet rs;
	PreparedStatement pstmt;

	public int cardCreate(MemberVO mem, String acc ) {
		int result = 0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append(" INSERT INTO CARD(ID, MEMBER_ID, BALANCE) VALUES(?, ?, ?)");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, acc);
			pstmt.setString(2, mem.getId());
			pstmt.setInt(3, 0);

			result = pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}

		return result;
	}

	// ----------------------------카드 삭제
	public int delete(String cardId, Scanner sc) {

		int result = 0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append(" DELETE FROM CARD WHERE ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, cardId);

			result = pstmt.executeUpdate();
			if (result > 0) {
				String isCheck = sc.nextLine();
				System.out.println("정말 카드를 삭제 하시겠습니까? (y/n)");
				if ("y".equalsIgnoreCase(isCheck)) {
					conn.commit();
				} else {
					System.out.println("삭제가 취소 되었습니다.");
					result = 0;
				}
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}

		return result;
	}

	// ------------------------------카드 잔고 조회 logIn.setCardPay(balanceOne(logIn.getCardNumber))
	public int balanceOne(String cardId) {

		int balance = 0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT BALANCE FROM CARD WHERE ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, cardId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				balance += rs.getInt(1);

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return balance;

	}

	//회원의 카드 번호 조회 메인에서     login.setCard_number(cardid메소드(login.getid));
	public String cardId(String memId) {

		String card = "";
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT ID FROM CARD WHERE MEMBER_ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, memId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				card = rs.getString(1);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return card;

	}
	

	// 카드 돈 입금 money 입글할 이거 밖에서 user.get잔고 + 입금scan  = money  , -뺴는것도 똑같음
	
	//카드 입금 출금
	public int deposit(int money, String cardId) {
	
		
		int result = 0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append(" UPDATE CARD SET BALANCE = ? WHERE ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, money);
			pstmt.setString(2, cardId);

			result = pstmt.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}


}
