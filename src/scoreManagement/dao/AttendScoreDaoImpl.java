package scoreManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import scoreManagement.dto.AttendScoreDto;
import scoreManagement.exception.RecordNotFoundException;
import scoreManagement.util.JdbcUtil;

public class AttendScoreDaoImpl implements AttendScoreDao {

	@Override
	public boolean studentList() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM STUDENT";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String studentId = rs.getString("student_id");
				String studentName = rs.getString("student_name");
				
				sql = "SELECT COUNT(student_id) FROM ATTENDSCORE WHERE student_id = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, studentId);
				ResultSet rs_check = pstmt.executeQuery();
				rs_check.next();
				int check = rs_check.getInt(1);
				if(check == 0) {
					sql = "INSERT INTO ATTENDSCORE(student_id, student_name, late_day, miss_day, attend_day, attend_score) "
							+ "VALUES(?, ?, ?, ?, ?, ?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, studentId);
					pstmt.setString(2, studentName);
					pstmt.setInt(3, 0);
					pstmt.setInt(4, 0);
					pstmt.setInt(5, 0);
					pstmt.setInt(6, 0);
					pstmt.executeUpdate();
					flag = true;
				}
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		return flag;
	}
	
	@Override
	public void init() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDSCORE";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String studentId = rs.getString("student_id");
				sql = "UPDATE ATTENDSCORE SET late_day = 0, miss_day = 0, attend_day = 0, attend_score = 0 "
						+ "WHERE student_id = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, studentId);
				pstmt.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
	}

	@Override
	public void connect(int days) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDSCORE";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String studentId = rs.getString("student_id");
				sql = "SELECT COUNT(*) FROM ATTENDLOG WHERE student_id = ? AND status = '지각'";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, studentId);
				ResultSet rs_count = pstmt.executeQuery();
				rs_count.next();
				int lateCount = rs_count.getInt(1);
				
				sql = "SELECT COUNT(*) FROM ATTENDLOG WHERE student_id = ? AND status = '출석'";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, studentId);
				rs_count = pstmt.executeQuery();
				rs_count.next();
				int attendCount = rs_count.getInt(1) + lateCount;
				int missCount = (days - attendCount) + lateCount/3;
				
				int score = 20;
				if(missCount > 3) score = 0;
				else {
					score -= missCount;
				}
				
				sql = "UPDATE ATTENDSCORE SET late_day = ?, miss_day = ?, attend_day = ?, attend_score = ?"
						+ "WHERE student_id = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, lateCount);
				pstmt.setInt(2, missCount);
				pstmt.setInt(3, attendCount);
				pstmt.setInt(4, score);
				pstmt.setString(5, studentId);
				pstmt.executeUpdate();
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
	}

	@Override
	public List<AttendScoreDto> listAll() throws SQLException {
		List<AttendScoreDto> result = new ArrayList<AttendScoreDto>();

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDSCORE ORDER BY student_name";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String studentId = rs.getString("student_id");
				String studentName = rs.getString("student_name");
				int lateDay = rs.getInt("late_day");
				int missDay = rs.getInt("miss_day");
				int attendDay = rs.getInt("attend_day");
				int attendScore = rs.getInt("attend_score");
				AttendScoreDto dto = new AttendScoreDto(studentId, studentName, lateDay, missDay, attendDay, attendScore);
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
	public AttendScoreDto list(String id) throws SQLException, RecordNotFoundException {
		AttendScoreDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDSCORE WHERE student_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String studentName = rs.getString("student_name");
				int lateDay = rs.getInt("late_day");
				int missDay = rs.getInt("miss_day");
				int attendDay = rs.getInt("attend_day");
				int attendScore = rs.getInt("attend_score");
				dto = new AttendScoreDto(id, studentName, lateDay, missDay, attendDay, attendScore);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		
		return dto;
	}
}
