package scoreManagement.dao;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dto.ScoreDto;
import scoreManagement.exception.DuplicatedIdException;
import scoreManagement.exception.RecordNotFoundException;

public interface ScoreDao {

	//등록
	public void add(ScoreDto dto) throws SQLException, DuplicatedIdException;
	
	//수정
	public void update(ScoreDto dto) throws SQLException, RecordNotFoundException;
	
	//삭제
	public void delete(String id) throws SQLException, RecordNotFoundException;
	
	//목록
	public List<ScoreDto> list() throws SQLException;
	
	//id검색
	public ScoreDto findById(String id) throws SQLException;
	
	// 출결정보 연동(강사)
	public void connect() throws SQLException;
	
	// 점수 합산
	public void total(String id) throws SQLException;
	
}
