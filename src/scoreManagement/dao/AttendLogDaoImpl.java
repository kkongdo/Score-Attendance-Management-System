package scoreManagement.dao;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import scoreManagement.dto.AttendLogDto;
import scoreManagement.exception.RecordNotFoundException;
import scoreManagement.util.JdbcUtil;

public class AttendLogDaoImpl implements AttendLogDao {

	@Override
	public void add(AttendLogDto dto) throws SQLException {
		
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JdbcUtil.connect();
			String sql = "INSERT INTO ATTENDLOG(attend_num, student_id, student_name, attend_date, attend_time, status) "
					+ "VALUES(ATTENDLOG_SEQ.NEXTVAL, ?, ?, SYSDATE, ?, ?)";
			
			LocalTime now = LocalTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String formatedNow = now.format(formatter);
			String status = "출석";
			if(LocalTime.parse(formatedNow).isAfter(LocalTime.parse("09:00:00"))) status = "지각";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getStudentId());
			pstmt.setString(2, dto.getStudentName());
			pstmt.setString(3, formatedNow);
			pstmt.setString(4, status);
			pstmt.executeUpdate();
			

		} catch (ClassNotFoundException e) { 
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
	}

	@Override
	public void update(AttendLogDto dto) throws SQLException, RecordNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			if(findBySeq(dto.getAttendNum()) == null) {
				throw new RecordNotFoundException();
			}
			
			con = JdbcUtil.connect();
			String sql = "UPDATE ATTENDLOG SET attend_time = ?, status = ? WHERE attend_num = ?";
			
			String status = "출석";
			if(LocalTime.parse(dto.getAttendTime()).isAfter(LocalTime.parse("09:00:00"))) status = "지각";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getAttendTime());
			pstmt.setString(2, status);
			pstmt.setInt(3, dto.getAttendNum());
			pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
	}

	@Override
	public void delete(int seq) throws SQLException, RecordNotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			if(findBySeq(seq) == null) {
				throw new RecordNotFoundException(seq + "번 출석 정보를 찾을 수 없습니다.");
			}
			
			con = JdbcUtil.connect();
			String sql = "DELETE ATTENDLOG WHERE attend_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
	}

	@Override
	public List<AttendLogDto> listAll() throws SQLException {
		List<AttendLogDto> result = new ArrayList<AttendLogDto>();

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDLOG ORDER BY attend_num DESC";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int attend_num = rs.getInt("attend_num");
				String studentId = rs.getString("student_id");
				String studentName = rs.getString("student_name");
				Date attendDate = rs.getDate("attend_date"); 
				String attendTime = rs.getString("attend_time");
				String status = rs.getString("status");
				AttendLogDto dto = new AttendLogDto(attend_num, studentId, studentName, attendDate, attendTime, status);
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
	public List<AttendLogDto> list(String id) throws SQLException, RecordNotFoundException {
		List<AttendLogDto> result = new ArrayList<AttendLogDto>();

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDLOG WHERE student_id = ? ORDER BY attend_num DESC";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int attend_num = rs.getInt("attend_num");
				String studentId = rs.getString("student_id");
				String studentName = rs.getString("student_name");
				Date attendDate = rs.getDate("attend_date"); 
				String attendTime = rs.getString("attend_time");
				String status = rs.getString("status");
				AttendLogDto dto = new AttendLogDto(attend_num, studentId, studentName, attendDate, attendTime, status);
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
	public AttendLogDto findBySeq(int seq) throws SQLException, RecordNotFoundException {
		AttendLogDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDLOG WHERE attend_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, seq);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) { 
				String studentId = rs.getString("student_id");
				String studentName = rs.getString("student_name");
				Date attendDate = rs.getDate("attend_date"); 
				String attendTime = rs.getString("attend_time"); 
				String status = rs.getString("status");
				dto = new AttendLogDto(seq, studentId, studentName, attendDate, attendTime, status);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		
		return dto;
	}

	@Override
	public AttendLogDto findByDate(String today, String id) throws SQLException, RecordNotFoundException {
		AttendLogDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDLOG WHERE student_id = ? AND attend_date >= TO_DATE(?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, today);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) { 
				int attendNum = rs.getInt("attend_num");
				String studentName = rs.getString("student_name");
				Date attendDate = rs.getDate("attend_date"); 
				String attendTime = rs.getString("attend_time"); 
				String status = rs.getString("status");
				dto = new AttendLogDto(attendNum, id, studentName, attendDate, attendTime, status);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
		
		return dto;
	}

}
