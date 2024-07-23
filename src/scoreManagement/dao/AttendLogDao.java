package scoreManagement.dao;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dto.AttendLogDto;
import scoreManagement.exception.RecordNotFoundException;

public interface AttendLogDao {
	
	// 출결정보 등록(학생/강사)
	public void add(AttendLogDto dto) throws SQLException;
	
	// 출석정보 수정(강사)
	public void update(AttendLogDto dto) throws SQLException, RecordNotFoundException;
	
	// 출결정보 삭제(강사)
	public void delete(int seq) throws SQLException, RecordNotFoundException;
	
	// 출결정보 전체조회(강사)
	public List<AttendLogDto> listAll() throws SQLException;
	
	// 특정 학생의 출결 조회(학생/강사)
	public List<AttendLogDto> list(String id) throws SQLException, RecordNotFoundException;
	
	// 출석관리번호로 검색
	public AttendLogDto findBySeq(int seq) throws SQLException, RecordNotFoundException;

	// 날짜로 검색
	public AttendLogDto findByDate(String today, String id) throws SQLException, RecordNotFoundException;
	
}
