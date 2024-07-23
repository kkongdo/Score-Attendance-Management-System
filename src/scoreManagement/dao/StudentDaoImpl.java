package scoreManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import scoreManagement.dto.StudentDto;
import scoreManagement.exception.RecordNotFoundException;
import scoreManagement.util.JdbcUtil;

public class StudentDaoImpl implements StudentDao {

	@Override
	public List<StudentDto> list() throws SQLException {
		List<StudentDto> result = new ArrayList<StudentDto>();
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			
			String sql = "SELECT * FROM STUDENT ORDER BY STUDENT_ID";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("STUDENT_ID");
				String name = rs.getString("STUDENT_NAME");
				String pw = rs.getString("STUDENT_PW");
				String tel = rs.getString("STUDENT_TEL");
				String sClass = rs.getString("STUDENT_CLASS");
				StudentDto dto = new StudentDto(id, name, pw, tel, sClass);
				result.add(dto);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		
		return result;
	}

	@Override
	public StudentDto findById(String id) throws SQLException, RecordNotFoundException {
		StudentDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM STUDENT WHERE STUDENT_ID = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString("STUDENT_NAME");
				String pw = rs.getString("STUDENT_PW");
				String tel = rs.getString("STUDENT_TEL"); 
				String sClass = rs.getString("STUDENT_CLASS");
				dto = new StudentDto(id, name, pw, tel, sClass);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		
		return dto;
	}

}
