package scoreManagement.service;

import java.sql.SQLException;
import java.util.List;

import scoreManagement.dao.*;
import scoreManagement.dto.*;
import scoreManagement.exception.*;

public class ManagementServiceImpl implements ManagementService {

	private StudentDao studentDao = new StudentDaoImpl();
	private TeacherDao teacherDao = new TeacherDaoImpl();
	private AttendLogDao attendLogDao = new AttendLogDaoImpl();
	private AttendScoreDao attendScoreDao = new AttendScoreDaoImpl();
	private ScoreDao scoreDao = new ScoreDaoImpl();
	private HwLogDao hwlogDao = new HwLogDaoImpl();
	
	@Override
	public StudentDto studentCheck(String id) throws ManagementException, RecordNotFoundException {
		StudentDto dto = null;
		try {
			dto = studentDao.findById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e.getMessage());
		}
		return dto;
	}

	@Override
	public TeacherDto teacherCheck(String id) throws ManagementException, RecordNotFoundException {
		TeacherDto dto = null;
		try {
			dto = teacherDao.findById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e.getMessage());
		}
		return dto;
	}

	@Override
	public void attend(AttendLogDto dto) throws ManagementException {
		try {
			attendLogDao.add(dto);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("DBMS 오류 발생");
		}
	}

	@Override
	public List<AttendLogDto> checkAttend(String id) throws ManagementException {
		List<AttendLogDto> list = null;
		
		try {
			list = attendLogDao.list(id);
			if(list.size() == 0) throw new RecordNotFoundException();
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		} catch (RecordNotFoundException e) {
			throw new ManagementException(e.getMessage());
		}
		
		return list;
	}

	@Override
	public List<ScoreDto> listScore() throws ManagementException {
		List<ScoreDto> list = null;
		try {
			list = scoreDao.list();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e.getMessage());
		}
		
		return list;
	}

	@Override
	public List<StudentDto> listStudent() throws ManagementException {
		List<StudentDto> list = null;
		try {
			list = studentDao.list();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e.getMessage());
		}
		
		return list;
	}

	@Override
	public boolean submit(HwLogDto dto) throws ManagementException {
		try {
			hwlogDao.add(dto);
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		} catch (DuplicatedIdException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteHw(String id) throws ManagementException, RecordNotFoundException {
		try {
			hwlogDao.delete(id);
		} catch (SQLException e) {
			throw new ManagementException();
		} catch (RecordNotFoundException e) {
			return false;
		}
		return true;
	}

	@Override
	public ScoreDto checkScore(String id) throws ManagementException, RecordNotFoundException {
		ScoreDto dto=null;
		try {
			 dto = scoreDao.findById(id);
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		}
		
		return dto;
	}

	@Override
	public boolean findTodayAttend(String today, String id) throws ManagementException {
		boolean flag = false;
		AttendLogDto dto = null;
		try {
			dto = attendLogDao.findByDate(today, id);
			if(dto != null) flag = true;
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			System.err.println("정보를 찾을 수 없습니다.");
		}
		return flag;
	}

	@Override
	public boolean studentListConnect() throws ManagementException {
		boolean flag = false;
		try {
			flag = attendScoreDao.studentList();
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		}
		return flag;
	}

	@Override
	public void attendListConnect(int days) throws ManagementException {
		try {
			attendScoreDao.connect(days);
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		}
	}

	@Override
	public void initAttendScore() throws ManagementException {
		try {
			attendScoreDao.init();
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		}
	}

	@Override
	public List<AttendScoreDto> attendScore() throws ManagementException {
		List<AttendScoreDto> list = null;
		try {
			list = attendScoreDao.listAll();
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		}
		
		return list;
	}

	@Override
	public List<AttendLogDto> attendListDetail(String id) throws ManagementException {
		List<AttendLogDto> list = null;
		
		try {
			list = attendLogDao.list(id);
			if(list.size() == 0) throw new RecordNotFoundException();
		} catch (SQLException e) {
			throw new ManagementException(e.getMessage());
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			System.err.println("정보를 찾을 수 없습니다.");
		}
		
		return list;
	}

	@Override
	public boolean updateAttend(AttendLogDto dto) throws ManagementException {
		boolean flag = false;
		
		try {
			attendLogDao.update(dto);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("DBMS 오류 발생");
		} catch (RecordNotFoundException e) {
			System.out.println("해당 출석정보를 찾을 수 없습니다.");
			System.out.println();
		}
		
		return flag;
	}

	@Override
	public boolean addScore(ScoreDto dto) throws ManagementException, RecordNotFoundException {
		try {
			scoreDao.add(dto);
		} catch (SQLException e) {
			throw new ManagementException();
		} catch (DuplicatedIdException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean updateScore(ScoreDto dto) throws ManagementException, RecordNotFoundException {
		try {
			scoreDao.update(dto);
		} catch (SQLException e) {
			System.out.println("SQL오류발생");
		} 
		return true;
	}

	@Override
	public void attendScoreConnect() throws ManagementException {
		try {
			scoreDao.connect();
		} catch (SQLException e) {
			System.out.println("SQL오류발생");
		}
	}
}
