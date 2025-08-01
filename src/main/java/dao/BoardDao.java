package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dto.BoardDto;
import util.DbcpBean;

public class BoardDao {
    private static BoardDao dao;

    static {
        dao = new BoardDao();
    }

    private BoardDao() {}

    public static BoardDao getInstance() {
        return dao;
    }
    
    public List<BoardDto> getListByType(String board_type){
    	List<BoardDto> list = new ArrayList<>();
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;

    	try {
    		conn = new DbcpBean().getConn();
    		String sql = """
    			    SELECT num, title, writer, board_type, created_at, user_id
    			    FROM board_p
    			    WHERE LOWER(board_type) = LOWER(?)
    			    ORDER BY num DESC
    			""";
    		
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setString(1, board_type);
    		
    		rs = pstmt.executeQuery();
    		while(rs.next()) {
    			BoardDto dto = new BoardDto();
    			dto.setNum(rs.getInt("NUM"));
    			dto.setTitle(rs.getString("TITLE"));
    			dto.setWriter(rs.getString("WRITER"));
    			dto.setCreated_at(rs.getTimestamp("CREATED_AT").toString());
    			dto.setBoard_type(rs.getString("BOARD_TYPE"));  
                dto.setUser_id(rs.getString("USER_ID"));        
    			list.add(dto);
    		}
    	} catch(Exception e){
    	    System.out.println("❌ SQL 실행 중 예외 발생: " + e.getMessage());
    	    e.printStackTrace();
    	} finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    	return list;
    }
    
    // 1. 글 저장 메서드
    public boolean insert(BoardDto dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowCount = 0;

        try {
            conn = new DbcpBean().getConn();
            String sql = """
                INSERT INTO board_p
                (num, writer, title, content, board_type, view_count, created_at)
                VALUES
                (board_p_seq.NEXTVAL, ?, ?, ?, ?, 0, SYSDATE)
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getWriter());
            pstmt.setString(2, dto.getTitle());
            pstmt.setString(3, dto.getContent());
            pstmt.setString(4, dto.getBoard_type()); // ✅ boardType 바인딩

            rowCount = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return rowCount > 0;
    }
    // 3. 저장할 글의 글번호를 리턴해주는 메소드
    public int getSequence() {
        int num = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = new DbcpBean().getConn();
            String sql = "SELECT board_p_seq.NEXTVAL AS num FROM DUAL";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                num = rs.getInt("num");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return num;
    }
    // 4. 글번호에 해당하는 게시글의 정보를 DB에서 조회하는 메소드
    public BoardDto getData(int num) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BoardDto dto = null;

        try {
            conn = new DbcpBean().getConn();
            String sql = """
                SELECT num, writer, title, content, view_count, created_at, board_type
                FROM board_p
                WHERE num = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new BoardDto();
                dto.setNum(rs.getInt("num"));
                dto.setWriter(rs.getString("writer"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setView_count(rs.getInt("view_count"));
                dto.setCreated_at(rs.getString("created_at"));
                dto.setBoard_type(rs.getString("board_type")); // ✅ boardType 포함
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return dto;
    }
}
