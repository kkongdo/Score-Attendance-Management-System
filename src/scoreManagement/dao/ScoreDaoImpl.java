package scoreManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import scoreManagement.dto.ScoreDto;
import scoreManagement.exception.DuplicatedIdException;
import scoreManagement.exception.RecordNotFoundException;
import scoreManagement.util.JdbcUtil;

public class ScoreDaoImpl implements ScoreDao {

	@Override
	public void add(ScoreDto dto) throws SQLException, DuplicatedIdException {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			if(findById(dto.getStudentId())!=null) {
				throw new DuplicatedIdException(dto.getStudentId()+"에 해당하는 학생은 이미 있습니다.");
			}
			
			conn=JdbcUtil.connect();
			String sql="INSERT INTO SCORE(STUDENT_ID, STUDENT_NAME, STUDENT_CLASS, MIDTERM, HW, FINALS, TOTAL_SCORE ) ";
			sql+="VALUES(?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getStudentId());
			pstmt.setString(2, dto.getStudentName());
			pstmt.setString(3, dto.getStudentClass());
			pstmt.setInt(4, dto.getMidTerm());
			pstmt.setInt(5, dto.getHw());
			pstmt.setInt(6, dto.getFinals());
			pstmt.setInt(7, dto.getTotalScore());
			pstmt.executeUpdate(); 
		} catch (ClassNotFoundException e) {
			throw new SQLException(e); 
		} finally {
			JdbcUtil.close(pstmt,conn);
		}
	}

	@Override
	public void update(ScoreDto dto) throws SQLException, RecordNotFoundException {
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			if(findById(dto.getStudentId())==null) {
				throw new RecordNotFoundException(dto.getStudentId()+"에 해당하는 학생은 없습니다.");
			}
			
			conn = JdbcUtil.connect();
			String sql="UPDATE SCORE SET MIDTERM = ?, HW = ?, FINALS = ?, TOTAL_SCORE = ?";
			sql+="WHERE STUDENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getMidTerm());
			pstmt.setInt(2, dto.getHw());
			pstmt.setInt(3, dto.getFinals());
			pstmt.setInt(4, dto.getTotalScore());
			pstmt.setString(5, dto.getStudentId());
			pstmt.executeUpdate(); 
			
			total(dto.getStudentId());
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
				throw new RecordNotFoundException(id+"에 해당하는 학생은 없습니다");
			}
			
			conn = JdbcUtil.connect();
			String sql="delete from SCORE ";
			sql+="WHERE STUDENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate(); 	
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, conn);
		}
	}

	@Override
	public List<ScoreDto> list() throws SQLException {
		List<ScoreDto> scorelist= new ArrayList<ScoreDto>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		try { 
			conn=JdbcUtil.connect();
			String sql="SELECT STUDENT_ID, STUDENT_NAME, STUDENT_CLASS, MIDTERM, HW, FINALS, ATTEND, TOTAL_SCORE, rownum as RANK FROM (SELECT * FROM SCORE Order By TOTAL_SCORE DESC)"; 
			pstmt = conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery(); 
			while(rs.next()) {
				String studentId=rs.getString("STUDENT_ID");
				String studentName=rs.getString("STUDENT_NAME");
				String studentClass=rs.getString("STUDENT_CLASS");
				int midterm=rs.getInt("MIDTERM");
				int hw=rs.getInt("HW");
				int finals=rs.getInt("FINALS");
				int attend=rs.getInt("ATTEND");
				int totalScore=rs.getInt("TOTAL_SCORE");
				int rank=rs.getInt("RANK");
				scorelist.add(new ScoreDto(studentId,studentName,studentClass,midterm,hw,  finals, attend, totalScore, rank)); 
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e); 
		} finally {
			JdbcUtil.close(pstmt,conn);
		}
		return scorelist;
	}

	@Override
	public ScoreDto findById(String id) throws SQLException {
        ScoreDto scoreDto=null;
        Connection conn=null;
        PreparedStatement pstmt=null;
        try { 
            conn=JdbcUtil.connect();
            String sql="SELECT STUDENT_ID, STUDENT_NAME, STUDENT_CLASS, MIDTERM, HW, FINALS, ATTEND, TOTAL_SCORE, rank FROM (SELECT STUDENT_ID, STUDENT_NAME, STUDENT_CLASS, MIDTERM, HW, FINALS, ATTEND, TOTAL_SCORE,  Rank() over(partition by STUDENT_CLASS order by total_score desc) as rank FROM SCORE)";
            sql+=" where student_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id); 
            ResultSet rs=pstmt.executeQuery(); 
            if(rs.next()) {
                String studentName=rs.getString("STUDENT_NAME");
                String studentClass=rs.getString("STUDENT_CLASS");
                int midterm=rs.getInt("MIDTERM");
                int hw=rs.getInt("HW");
                int finals=rs.getInt("FINALS");
                int attend=rs.getInt("ATTEND");
                int totalScore=rs.getInt("TOTAL_SCORE");
                int rank=rs.getInt("RANK");
                scoreDto=new ScoreDto(id,studentName,studentClass,midterm,hw, finals, attend, totalScore, rank);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException(e); 
        } finally {
            JdbcUtil.close(pstmt,conn);
        }
        return scoreDto;
	}

	@Override
	public void connect() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = JdbcUtil.connect();
			String sql = "SELECT * FROM ATTENDSCORE";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String studentId = rs.getString("student_id");
				int attendScore = rs.getInt("attend_score");
				sql = "UPDATE SCORE SET attend = ? WHERE student_id = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, attendScore);
				pstmt.setString(2, studentId);
				pstmt.executeUpdate();
				
				total(studentId);
			}
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, con);
		}
	}

	@Override
	public void total(String id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			conn = JdbcUtil.connect();
			int total = 0;
			
			String sql = "SELECT * FROM SCORE WHERE STUDENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int mid = rs.getInt("midterm");
				int hw = rs.getInt("hw");
				int fin = rs.getInt("finals");
				int att = rs.getInt("attend");
				total = mid + hw + fin + att;
			}
			
			sql="UPDATE SCORE SET TOTAL_SCORE = ? WHERE STUDENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, total);
			pstmt.setString(2, id);
			pstmt.executeUpdate(); 	
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			JdbcUtil.close(pstmt, conn);
		}
	}

}
