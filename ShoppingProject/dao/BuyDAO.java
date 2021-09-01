package dao;

import java.sql.*;
import java.util.*;

import connection.JDBConnection;
import vo.MemberVO;

// SELECT : 구매내역 조회
// INSERT : 구매
// : 환불
// 베스트아이템 이름으로 그룹바이 카운트 많은 순서대로 
public class BuyDAO {

	Connection conn = null;
	PreparedStatement pstmt = null;;
	ResultSet rs = null;

	// SELECT : 구매내역 조회
	public List<Map<String, String>> selectAll(MemberVO vo) {
		List<Map<String, String>> list = new ArrayList<>();
		Map<String, String> mapList = new HashMap<>();

		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append(
					"SELECT P.PRODUCT_ID, P.PRODUCT_NAME, P.PRODUCT_SIZE, B.B_DATE FROM PRODUCT P, MEMBER M, BUY B ");
			sql.append("WHERE P.PRODUCT_ID = B.PRODUCT_ID ");
			sql.append("AND B.MEMBER_ID = M.ID ");
			sql.append("AND B.MEMBER_ID = ? ");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, vo.getId());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				mapList.put("PRODUCT_ID", rs.getString("PRODUCT_ID"));
				mapList.put("PRODUCT_NAME", rs.getString("PRODUCT_NAME"));
				mapList.put("PRODUCT_SIZE", rs.getString("PRODUCT_SIZE"));
				mapList.put("B_DATE", rs.getString("B_DATE"));

				list.add(mapList);
				mapList = new HashMap<>();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return list;
	}

	// ------------------------------------------------------
	// SELECT : 구매한 제품을 선택 후 가격 확인
	public int selectPrice(MemberVO vo, String productId) {

		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();
		int price = 0;
		try {
			sql.append("SELECT P.PRICE FROM PRODUCT P, MEMBER M, BUY B ");
			sql.append("WHERE P.PRODUCT_ID = B.PRODUCT_ID ");
			sql.append("AND B.MEMBER_ID = M.ID ");
			sql.append("AND B.MEMBER_ID = ? ");
			sql.append("AND P.PRODUCT_ID = ? ");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, productId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				price = rs.getInt("PRICE");
				if (price < 30000) {
					System.out.println("가격이 30000원 이하여서 배송비가 추가되었습니다.");
					price += 3000;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return price;
	}

	// --가장 많이 팔리는 옷
	// select product_id, sum(count) from buy group by product_id;

	// ------------------------------------------------------
	// SELECT : 베스트아이템 이름으로 그룹바이 카운트 많은 순서대로
	public List<Map<String, String>> bestProduct() {
		List<Map<String, String>> list = new ArrayList<>();
		Map<String, String> mapList = new HashMap<>();

		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append("SELECT * FROM (SELECT P.PRODUCT_NAME , SUM(count)AS COUNT ");
			sql.append(" FROM PRODUCT P, BUY B ");
			sql.append(" WHERE P.PRODUCT_ID = B.PRODUCT_ID ");
			sql.append(" GROUP BY P.PRODUCT_NAME) ");
			sql.append(" WHERE ROWNUM <= 10 ");
			sql.append(" ORDER BY COUNT DESC ");
			pstmt = conn.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				mapList.put("P.PRODUCT_NAME", rs.getString("PRODUCT_NAME"));
				mapList.put("COUNT", rs.getString("COUNT"));

				list.add(mapList);
				mapList = new HashMap<>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return list;
	}

	// ------------------------------------------------------
	// INSERT : 구매 상품번호랑 회원아이디
	public int insert(String productId, String memberId, int count) {
		int result = 0;
		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append("INSERT INTO BUY(ID, PRODUCT_ID, MEMBER_ID, COUNT) ");
			sql.append("VALUES (SEQ_REVIEW.NEXTVAL, ?, ?, ?) ");
			pstmt = conn.prepareStatement(sql.toString());

			int idx = 1;
			pstmt.setString(idx++, productId);
			pstmt.setString(idx++, memberId);
			pstmt.setInt(idx++, count);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}

	// ------------------------------------------------------
	// : 환불
	public int refund(String buyId) {
		int result = 0;
		conn = JDBConnection.getDBConn();
		StringBuilder sql = new StringBuilder();

		try {

			sql.append("DELETE FROM BUY WHERE ID = ?");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, buyId);

			result = pstmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}

	// 선택한 조인 값 반환
	public Map<String,String> proRefund(String productId, String memberId) {

		Map<String,String> map = new HashMap<>();
		
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT B.ID, B.PRODUCT_ID, PRODUCT_NAME, MEMBER_ID, COUNT,PRICE  ");
		sb.append("FROM BUY B , PRODUCT P ");
		sb.append("WHERE B.PRODUCT_ID = P.PRODUCT_ID ");
		sb.append("AND B.PRODUCT_ID = ? ");
		sb.append("AND MEMBER_ID = ? ");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, productId);
			pstmt.setString(2, memberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				map.put("B.ID", String.valueOf(rs.getInt("ID")));
				map.put("B.PRODUCT_ID", rs.getString("PRODUCT_ID")); 
				map.put("PRODUCT_NAME", rs.getString("PRODUCT_NAME")); 
				map.put("MEMBER_ID", rs.getString("MEMBER_ID")); 
				map.put("COUNT", String.valueOf(rs.getInt("COUNT"))); 
				map.put("PRICE", String.valueOf(rs.getInt("PRICE")));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return map;

	}
	
	
	public String memIdCheck(String productId) {

		String mamId ="";
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT MEMBER_ID FROM BUY WHERE PRODUCT_ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, productId);

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