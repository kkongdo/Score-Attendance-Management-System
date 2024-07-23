package scoreManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import scoreManagement.dto.HwLogDto;
import scoreManagement.exception.DuplicatedIdException;
import scoreManagement.exception.RecordNotFoundException;
import scoreManagement.util.JdbcUtil;

public class HwLogDaoImpl implements HwLogDao {

	@Override
	public void add(HwLogDto dto) throws SQLException, DuplicatedIdException {
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			if(findById(dto.getStudentId())!=null) {
				throw new DuplicatedIdException(dto.getStudentId()+"에 해당하는 학생은 이미 제출하였습니다.");
			}
			
			conn = JdbcUtil.connect();
			String sql="INSERT INTO HWLOG(STUDENT_ID, STUDENT_NAME, UPLOAD_DATE, FILE_NAME)";
			sql+="VALUES(?, ?, sysdate, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getStudentId());
			pstmt.setString(2, dto.getStudentName());
			pstmt.setString(3, dto.getFileName());
			pstmt.executeUpdate(); 	
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, conn);
		}
	}

	@Override
	public void delete(String id) throws SQLException, RecordNotFoundException {
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			if(findById(id)==null) {
				throw new RecordNotFoundException(id+"학생에 해당하는 과제는 없습니다.");
			}
			
			conn = JdbcUtil.connect();
			String sql="delete from HWLOG WHERE STUDENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int count = pstmt.executeUpdate(); 	
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, conn);
		}
	}

	@Override
	public List<HwLogDto> list() throws SQLException {
		List<HwLogDto> hwlist= new ArrayList<HwLogDto>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		try { 
			conn=JdbcUtil.connect();
			String sql="SELECT * FROM HWLOG"; 
			pstmt = conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery(); 
			while(rs.next()) {
				String studentId=rs.getString("STUDENT_ID");
				String studentName=rs.getString("STUDENT_NAME");
				Date uploadDate=rs.getDate("UPLOAD_DATE");
				String fileName=rs.getString("FILE_NAME");
				hwlist.add(new HwLogDto(studentId,studentName,fileName,uploadDate)); 
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e); 
		} finally {
			JdbcUtil.close(pstmt,conn);
		}
		
		return hwlist;
	}

	@Override
	public HwLogDto findById(String id) throws SQLException {
		HwLogDto hwDto=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try { 
			conn=JdbcUtil.connect();
			String sql="SELECT * FROM HWLOG WHERE STUDENT_ID = ?"; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id); 
			ResultSet rs=pstmt.executeQuery(); 
			if(rs.next()) {
				String studentName=rs.getString("STUDENT_NAME");
				Date uploadDate=rs.getDate("UPLOAD_DATE");
				String fileName=rs.getString("FILE_NAME");
				hwDto=new HwLogDto(id,studentName,fileName,uploadDate);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e); 
		} finally {
			JdbcUtil.close(pstmt,conn);
		}
		
		return hwDto;
	}

}
