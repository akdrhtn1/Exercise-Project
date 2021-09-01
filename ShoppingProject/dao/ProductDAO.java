package dao;
// SELECT : 테이블 전체 데이터 조회 - selectAll : List<ProductVO>

// SELECT : 하나의 데이터 조회(상품명) - selectOne : List<ProductVO>
// SELECT : 하나의 데이터 조회(성별) - selectOne : List<ProductVO>
// SELECT : 하나의 데이터 조회(카테고리별) - selectOne : List<ProductVO>
// INSERT : VO 객체를 받아서 입력 - insert : int
// UPDATE : VO 객체를 받아서 수정 - update : int
// DELETE : VO 객체를 받아서 삭제 - delete : int

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.JDBConnection;
import vo.ProductVO;

public class ProductDAO {

	Connection conn = null;
	PreparedStatement pstmt = null;;
	ResultSet rs = null;

	// SELECT : 테이블 전체 데이터 조회 - selectAll : List<ProductVO>
	public List<ProductVO> selectAll() {
		List<ProductVO> list = null;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT PRODUCT_ID, PRODUCT_NAME, PRICE, PRODUCT_SIZE, ");
			sql.append("DECODE(PRODUCT_GENDER,'M','남성','FM','여성') AS PRODUCT_GENDER, STOCK, ");
			sql.append("DECODE(CATEGORY,'1','상의','2','하의','3','치마','4','원피스','5','아우터') AS CATEGORY ");
			sql.append(" FROM PRODUCT ");
			sql.append(" ORDER BY TO_NUMBER(PRODUCT_ID) ");

			pstmt = conn.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO vo = new ProductVO(rs.getString("PRODUCT_ID"), rs.getString("PRODUCT_NAME"),
						rs.getInt("PRICE"), rs.getString("PRODUCT_SIZE"), rs.getInt("STOCK"), rs.getString("CATEGORY"),
						rs.getString("PRODUCT_GENDER"));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return list;
	}

	// ------------------------------------------------------
	// SELECT : 이름 조회 - selectOne : List<ProductVO> 풀네임 검색 , 줄여서 검색 가능
	public List<ProductVO> selectOne(String query, String productName) {
		List<ProductVO> listVo = null;

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT PRODUCT_ID, PRODUCT_NAME, PRICE, PRODUCT_SIZE, ");
		sql.append("DECODE(PRODUCT_GENDER,'M','남성','FM','여성') AS PRODUCT_GENDER, STOCK, ");
		sql.append("DECODE(CATEGORY,'1','상의','2','하의','3','치마','4','원피스','5','아우터') AS CATEGORY ");
		sql.append(" FROM PRODUCT ");
		sql.append(" WHERE " + query + " IN ? OR " + query + " LIKE ?");
		sql.append(" ORDER BY TO_NUMBER(PRODUCT_ID)");
		// sql.append(" WHERE "+ query + " LIKE ?");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, productName);
			pstmt.setString(2, "%" + productName + "%");
			rs = pstmt.executeQuery();

			listVo = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO vo = new ProductVO(rs.getString("PRODUCT_ID"), rs.getString("PRODUCT_NAME"),
						rs.getInt("PRICE"), rs.getString("PRODUCT_SIZE"), rs.getInt("STOCK"),
						rs.getString("PRODUCT_GENDER"), rs.getString("CATEGORY"));
				listVo.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();

		}
		return listVo;
	}

	// ------------------------------------------------------
	// SELECT : 데이터 조회 - selectOne : List<ProductVO>
	public List<ProductVO> selectProduct(String query, String value) {
		List<ProductVO> listVo = null;

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PRODUCT_ID, PRODUCT_NAME, PRICE, PRODUCT_SIZE,");
		sql.append(" DECODE(PRODUCT_GENDER,'M','남성','FM','여성') AS PRODUCT_GENDER, STOCK, ");
		sql.append("DECODE(CATEGORY,'1','상의','2','하의','3','치마','4','원피스','5','아우터') AS CATEGORY");
		sql.append(" FROM PRODUCT ");
		sql.append("  WHERE " + query + " = ? ");
		sql.append(" ORDER BY TO_NUMBER(PRODUCT_ID)");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();

			listVo = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO vo = new ProductVO(rs.getString("PRODUCT_ID"), rs.getString("PRODUCT_NAME"),
						rs.getInt("PRICE"), rs.getString("PRODUCT_SIZE"), rs.getInt("STOCK"),
						rs.getString("PRODUCT_GENDER"), rs.getString("CATEGORY"));
				listVo.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return listVo;
	}

	// ------------------------------------------------------
	// INSERT : VO 객체를 받아서 입력 - insert : int
	public int insert(ProductVO vo) {
		int result = 0;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO PRODUCT ");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?) ");
			pstmt = conn.prepareStatement(sql.toString());
			int idx = 1;
			pstmt.setString(idx++, vo.getProductId());
			pstmt.setString(idx++, vo.getProductName());
			pstmt.setInt(idx++, vo.getPrice());
			pstmt.setString(idx++, vo.getProductSize());
			pstmt.setInt(idx++, vo.getStock());
			pstmt.setString(idx++, vo.getProductGender());
			pstmt.setString(idx++, vo.getCategory());

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
	public int update(ProductVO vo) {
		int result = 0;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE PRODUCT ");
			sql.append("SET PRODUCT_NAME = ? ");
			sql.append("    ,PRICE = ? ");
			sql.append("    ,PRODUCT_SIZE = ? ");
			sql.append("    ,STOCK = ? ");
			sql.append("    ,PRODUCT_GENDER = ? ");
			sql.append("    ,CATEGORY = ? ");
			sql.append("WHERE PRODUCT_ID = ? ");

			pstmt = conn.prepareStatement(sql.toString());

			int idx = 1; /* 변수 설정 */
			pstmt.setString(idx++, vo.getProductName());
			pstmt.setInt(idx++, vo.getPrice());
			pstmt.setString(idx++, vo.getProductSize());
			pstmt.setInt(idx++, vo.getStock());
			pstmt.setString(idx++, vo.getProductGender());
			pstmt.setString(idx++, vo.getCategory());
			pstmt.setString(idx++, vo.getProductId());

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
	public int delete(String productId) {
		int result = 0;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM PRODUCT WHERE PRODUCT_ID = ? ");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, productId);
			result = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}

	// 프로덕트dao 에서 상품번호를 받고 그 가격을 추출
	public int selectPrice(String pro) {

		int sale = 0;

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PRICE");
		sql.append(" FROM PRODUCT ");
		sql.append("  WHERE PRODUCT_ID = ? ");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, pro);
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
	
	//상품번호를 입력받아 이름 반환
	public String selectName(String proID) {

		String name = "";

		conn = JDBConnection.getDBConn();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PRODUCT_NAME");
		sql.append(" FROM PRODUCT ");
		sql.append("  WHERE PRODUCT_ID = ? ");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, proID);
			rs = pstmt.executeQuery();


			while (rs.next()) {
				name = rs.getString("PRODUCT_NAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return name;
	}
	//재고 변경 메서드
	//NC : 노커밋
	public int updateNCStock(int stock, String productId) {
		int result = 0;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE PRODUCT ");
			sql.append("SET STOCK = ? ");
			sql.append("WHERE PRODUCT_ID = ? ");

			pstmt = conn.prepareStatement(sql.toString());

			int idx = 1; /* 변수 설정 */
			pstmt.setInt(idx++, stock);
			pstmt.setString(idx++, productId);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return result;
	}
	
	//재고 조회 메서드도 해야함
	public int stockView(String productId) {

		int stock =0;
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT STOCK FROM PRODUCT WHERE PRODUCT_ID = ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, productId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				stock = rs.getInt("STOCK");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return stock;

	}
	
	// 재고 n개 이하인것들 출력
	public List<ProductVO> stockNumber(int count) {

		List<ProductVO> list = null;
		
		StringBuilder sb = new StringBuilder();
		conn = JDBConnection.getDBConn();

		sb.append("SELECT PRODUCT_ID, PRODUCT_NAME , STOCK ");
		sb.append("FROM PRODUCT WHERE STOCK <= ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, count);

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			
			while (rs.next()) {
				ProductVO vo = new ProductVO();
				vo.setProductId(rs.getString("PRODUCT_ID"));
				vo.setProductName(rs.getString("PRODUCT_NAME"));
				vo.setStock(rs.getInt("STOCK"));
				
				list.add(vo);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBConnection.queryClose();
		}
		return list;

	}
	

	//재고 변경 메서드인데 이건 commit 필요해서 만듬
	
	public int updateStock(int stock, String productId) {
		int result = 0;

		conn = JDBConnection.getDBConn();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE PRODUCT ");
			sql.append("SET STOCK = ? ");
			sql.append("WHERE PRODUCT_ID = ? ");

			pstmt = conn.prepareStatement(sql.toString());

			int idx = 1; /* 변수 설정 */
			pstmt.setInt(idx++, stock);
			pstmt.setString(idx++, productId);

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


