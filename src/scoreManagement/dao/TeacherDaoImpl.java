package scoreManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import scoreManagement.dto.TeacherDto;
import scoreManagement.exception.RecordNotFoundException;
import scoreManagement.util.JdbcUtil;

public class TeacherDaoImpl implements TeacherDao {

	@Override
	public List<TeacherDto> list() throws SQLException {
		List<TeacherDto> result = new ArrayList<TeacherDto>();
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM TEACHER order by TEACHER_ID";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("TEACHER_ID");
				String pw = rs.getString("TEACHER_PW");
				String name = rs.getString("TEACHER_NAME");
				String tClass = rs.getString("TEACHER_CLASS");
				TeacherDto dto = new TeacherDto(id, pw, name, tClass);
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
	public TeacherDto findById(String id) throws SQLException, RecordNotFoundException {
		TeacherDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM TEACHER where TEACHER_ID = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String pw = rs.getString("TEACHER_PW");
				String name = rs.getString("TEACHER_NAME");
				String tClass = rs.getString("TEACHER_CLASS");
				dto = new TeacherDto(id, pw, name, tClass);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		
		return dto;
	}
}
