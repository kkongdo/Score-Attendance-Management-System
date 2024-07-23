package scoreManagement.service;

import java.util.List;

import scoreManagement.dto.*;
import scoreManagement.exception.ManagementException;
import scoreManagement.exception.RecordNotFoundException;

public interface ManagementService {
	
	// 학생 로그인
	public StudentDto studentCheck(String id) throws ManagementException, RecordNotFoundException;
	
	// 강사 로그인
	public TeacherDto teacherCheck(String id) throws ManagementException, RecordNotFoundException;
	
	// 학생 출석체크
	public void attend(AttendLogDto dto) throws ManagementException;
	
	// 학생 출석체크여부 확인
	public boolean findTodayAttend(String today, String id) throws ManagementException;
	
	// 학생 출석확인
	public List<AttendLogDto> checkAttend(String id) throws ManagementException;
	
	// 학생 과제제출
	public boolean submit(HwLogDto dto) throws ManagementException;
	
	// 학생 과제삭제
	public boolean deleteHw(String id) throws ManagementException, RecordNotFoundException;
	
	// 학생 성적조회
	public ScoreDto checkScore(String id) throws ManagementException, RecordNotFoundException;
	
	// 강사 성적조회
	public List<ScoreDto> listScore() throws ManagementException;
	
	// 강사 성적수정
	public boolean updateScore(ScoreDto dto) throws ManagementException, RecordNotFoundException;
	
	// 강사 출결점수연동
	public void attendScoreConnect() throws ManagementException;
	
	// 강사 학생조회
	public List<StudentDto> listStudent() throws ManagementException;
	
	// 강사 학생정보연동(출결)
	public boolean studentListConnect() throws ManagementException;
	
	// 강사 출결정보연동
	public void attendListConnect(int days) throws ManagementException;
	
	// 강사 출결정보 초기화
	public void initAttendScore() throws ManagementException;
	
	// 강사 성적조회
	public List<AttendScoreDto> attendScore() throws ManagementException;
	
	// 강사 출석상세확인
	public List<AttendLogDto> attendListDetail(String id) throws ManagementException;
	
	// 강사 출석정보수정
	public boolean updateAttend(AttendLogDto dto) throws ManagementException;
	
	// 강사 학생성적입력
	public boolean addScore(ScoreDto dto) throws ManagementException, RecordNotFoundException;
	
}
