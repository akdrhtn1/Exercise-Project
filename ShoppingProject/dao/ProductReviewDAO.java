package dao;
// SELECT : 전체 데이터 조회 selectAll 

// SELECT : 하나의 데이터 조회(상품이름) selectOne 
// INSERT : VO 객체를 받아서 입력 - insert : int
// UPDATE : VO 객체를 받아서 수정 - update : int
// DELETE : VO 객체를 받아서 삭제 - delete : int

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connection.JDBConnection;
import vo.ProductReviewsVO;
import vo.ProductVO;

public class ProductReviewDAO {

	Connection conn = null;
	PreparedStatement pstmt = null;;
	ResultSet rs = null;

	// SELECT : 전체 데이터 조회 selectAll
	public List<Map<String, String>> selectAll() {
		List<Map<String, String>> list = new ArrayList<>();

		Map<String, String> mapList = new HashMap<>();
		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append(
					"R.ID, P.PRODUCT_ID, P.PRODUCT_NAME, R.WRITER, R.CONTENT, R.W_DATE FROM PRODUCT P , PRODUCT_REVIEWS R , BUY B");
			sql.append(" WHERE P.PRODUCT_ID = R.PRO_ID ");
			sql.append(" AND P.PRODUCT_ID = B.PRODUCT_ID ");

			pstmt = conn.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				mapList.put("R.ID", rs.getString("ID"));
				mapList.put("P.PRODUCT_ID", rs.getString("PRODUCT_ID"));
				mapList.put("P.PRODUCT_NAME", rs.getString("PRODUCT_NAME"));
				mapList.put("R.WRITER", rs.getString("WRITER"));
				mapList.put("R.CONTENT", rs.getString("CONTENT"));
				mapList.put("B.W_DATE", rs.getString("W_DATE"));

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

	// 제품번호를 받고 상품평(조인에서 프로젝트 명까지) 보기 !!!@
	public List<Map<String, String>> selectOne(String proId) {
		List<Map<String, String>> list = new ArrayList<>();

		Map<String, String> mapList = new HashMap<>();
		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append("SELECT R.ID, P.PRODUCT_NAME, R.WRITER, R.CONTENT, R.W_DATE  ");
			sql.append(" FROM PRODUCT P , PRODUCT_REVIEWS R ");
			sql.append(" WHERE P.PRODUCT_ID = R.PRODUCT_ID ");
			sql.append(" AND P.PRODUCT_ID = ? ");

			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, proId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				mapList.put("R.ID", rs.getString("ID"));
				mapList.put("P.PRODUCT_NAME", rs.getString("PRODUCT_NAME"));
				mapList.put("R.WRITER", rs.getString("WRITER"));
				mapList.put("R.CONTENT", rs.getString("CONTENT"));
				mapList.put("R.W_DATE", rs.getString("W_DATE"));

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

	// INSERT : VO 객체를 받아서 입력 - insert : int
	public int insert(ProductReviewsVO vo) {
		int result = 0;

		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append("INSERT INTO PRODUCT_REVIEWS(ID, PRODUCT_ID, CONTENT, WRITER) ");
			sql.append("VALUES (SEQ_REVIEW.NEXTVAL, ?, ?, ?) ");
			pstmt = conn.prepareStatement(sql.toString());
			int idx = 1;
			pstmt.setString(idx++, vo.getProduct_id());
			pstmt.setString(idx++, vo.getContent());
			pstmt.setString(idx++, vo.getWriter());

			result = pstmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}

	// ------------------------------------------------------
	// UPDATE : VO 객체를 받아서 수정 - update : int
	public int update(String content, int num) {
		int result = 0;

		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append("UPDATE PRODUCT_REVIEWS ");
			sql.append("SET CONTENT = ? ");
			sql.append("    ,W_DATE = SYSDATE "); //수정시간으로 변경
			sql.append("WHERE ID = ? ");
			pstmt = conn.prepareStatement(sql.toString());
			int idx = 1;
			pstmt.setString(idx++, content);
			pstmt.setInt(idx++, num);

			result = pstmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}

	// ------------------------------------------------------
	// DELETE : VO 객체를 받아서 삭제 - delete : int
	public int delete(int rivewNum) {
		int result = 0;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM PRODUCT_REVIEWS WHERE ID = ? ");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, rivewNum);

			result = pstmt.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}

	// ------------------------------------------------------
	// 본인 아이디에 대한 상품평만 조회

	// 제품번호를 받고 상품평(조인에서 프로젝트 명까지) 보기 !!!@
	public List<Map<String, String>> selectId(String memId) {
		List<Map<String, String>> list = new ArrayList<>();

		Map<String, String> mapList = new HashMap<>();
		StringBuilder sql = new StringBuilder();

		conn = JDBConnection.getDBConn();

		try {
			sql.append("SELECT R.ID, P.PRODUCT_NAME, R.WRITER, R.CONTENT, R.W_DATE  ");
			sql.append(" FROM PRODUCT P , PRODUCT_REVIEWS R ");
			sql.append(" WHERE P.PRODUCT_ID = R.PRODUCT_ID ");
			sql.append(" AND WRITER = ? ");

			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, memId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				mapList.put("R.ID", rs.getString("ID"));
				mapList.put("P.PRODUCT_NAME", rs.getString("PRODUCT_NAME"));
				mapList.put("R.WRITER", rs.getString("WRITER"));
				mapList.put("R.CONTENT", rs.getString("CONTENT"));
				mapList.put("R.W_DATE", rs.getString("W_DATE"));

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

	// 리뷰 번호를 보내서 아이디 만드는게 나오게끔
	public String selectUser(String rivId) {

		String writer = "";

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT WRITER");
		sql.append(" FROM PRODUCT_REVIEWS ");
		sql.append("  WHERE ID = ? ");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rivId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				writer = rs.getString("WRITER");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return writer;
	}

}