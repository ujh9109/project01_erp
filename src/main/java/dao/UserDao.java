package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.UserDto;
import util.DbcpBean;

public class UserDao {

private static UserDao dao;
	
	static {
		dao=new UserDao();
	}
	private UserDao() {}
	
	public static UserDao getInstance() {
		return dao;
	}
	
	// user_id 를 이용해서 정보 불러오기
	public UserDto getByUserId(String user_id) {
		UserDto dto = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			conn = new DbcpBean().getConn();
			String sql = """
					SELECT num, branch_id, password, user_name, location, phone, profile_image, role, updated_at, created_at
					FROM users_p
					WHERE user_id = ?
					""";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, user_id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				dto = new UserDto();
				dto.setNum(rs.getLong("num"));
				dto.setUser_id(user_id);
				dto.setBranch_id(rs.getString("branch_id"));
				dto.setPassword(rs.getString("password"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setLocation(rs.getString("location"));
				dto.setPhone(rs.getString("phone"));
				dto.setProfile_image(rs.getString("profile_image"));
				dto.setRole(rs.getString("role"));
				dto.setUpdated_at(rs.getString("updated_at"));
				dto.setCreated_at(rs.getString("created_at"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (psmt != null) psmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	
	// userp 정보 추가
	public boolean insert(UserDto dto) {
		Connection conn = null;
		PreparedStatement psmt = null;
		int rowCount = 0;
		try {
			conn = new DbcpBean().getConn();
			String sql = """
					INSERT INTO users_p
					(num, branch_id, user_id, password, user_name, updated_at, created_at)
					VALUES(user_p_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, SYSDATE)
					""";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, dto.getBranch_id());
			psmt.setString(2, dto.getUser_id());
			psmt.setString(3, dto.getPassword());
			psmt.setString(4, dto.getUser_name());
			rowCount = psmt.executeUpdate();

		} catch (SQLException e) {
			// FK 위반(존재하지 않는 branch_id)
	        if (e.getErrorCode() == 2291) { // ORA-02291
	            System.out.println("존재하지 않는 지점번호 입력하셨습니다.");
	        } else if (e.getErrorCode() == 1) { // ORA-00001: Unique 위반 등
	            System.out.println("아이디 중복입니다");
	        }
			e.printStackTrace();
			
		} catch (Exception e){
			e.printStackTrace();
		}
			finally {
			try {
				if (psmt != null) psmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (rowCount > 0) {
			return true; // 작업 성공이라는 의미에서 true 리턴하기
		} else {
			return false; // 작업 실패라는 의미에서 false 리턴하기
		}
	}
}