package scoreManagement.dao;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dto.HwLogDto;
import scoreManagement.exception.DuplicatedIdException;
import scoreManagement.exception.RecordNotFoundException;

public interface HwLogDao {
	
	//등록
	public void add(HwLogDto dto) throws SQLException, DuplicatedIdException;
	
	//삭제
	public void delete(String id) throws SQLException, RecordNotFoundException;
	
	//목록
	public List<HwLogDto> list() throws SQLException;
	
	//id검색
	public HwLogDto findById(String id) throws SQLException;
	
}
