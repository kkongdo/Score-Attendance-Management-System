package scoreManagement.dao;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dto.StudentDto;
import scoreManagement.exception.RecordNotFoundException;

public interface StudentDao {
	
	// 학생목록 조회
	public List<StudentDto> list() throws SQLException;
	
	// 검색
	public StudentDto findById(String id) throws SQLException, RecordNotFoundException;
	
}
