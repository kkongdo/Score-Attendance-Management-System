package scoreManagement.dao;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dto.TeacherDto;
import scoreManagement.exception.RecordNotFoundException;

public interface TeacherDao {

	// 강사 목록 조회
	public List<TeacherDto> list() throws SQLException;
	
	//검색
	public TeacherDto findById(String id) throws SQLException, RecordNotFoundException;
	
}
