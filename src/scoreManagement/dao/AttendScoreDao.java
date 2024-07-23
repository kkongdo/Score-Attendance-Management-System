package scoreManagement.dao;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dto.AttendScoreDto;
import scoreManagement.exception.RecordNotFoundException;

public interface AttendScoreDao {

	// 출결정보 학생정보 연동(강사)
	public boolean studentList() throws SQLException;
	
	// 출결정보 초기화(강사)
	public void init() throws SQLException;
	
	// 출결정보 연동(강사)
	public void connect(int days) throws SQLException;
	
	// 출결점수 전체조회(강사)
	public List<AttendScoreDto> listAll() throws SQLException;
	
	// 특정 학생의 출결점수 조회(강사)
	public AttendScoreDto list(String id) throws SQLException, RecordNotFoundException;
	
}
