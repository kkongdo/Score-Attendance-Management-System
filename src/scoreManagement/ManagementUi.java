package scoreManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import scoreManagement.dto.*;
import scoreManagement.exception.*;
import scoreManagement.service.*;

public class ManagementUi {
	public static void main(String[] args) {
		new ManagementUi().go();
	}

	private void go() {
		System.out.println("[KB 아카데미 출결/성적관리 프로그램]");
		init();
		int status = 0;
		while(status == 0) {
			status = loginMenu();
		}
		
		if(status == 1) {
			while(true) {
				studentMenu();
			}
		} else if(status == 2) {
			while(true) {
				teacherMenu();
			}
		}
	}

	private ManagementService mmSvc;
	private Scanner sc;
	private String id = "";
	private String name = "";
	
	private void init() {
		mmSvc = new ManagementServiceImpl();
		sc = new Scanner(System.in);
	}
	
	private int loginMenu() {
		int status = 0;
		
		System.out.println("메뉴 정보 : (1)학생 (2)교수 (3)종료");
		System.out.print("메뉴 선택 : ");
		int menu = Integer.parseInt(sc.nextLine());
		if(menu == 1) status = goStduent();
		else if(menu == 2) status = goTeacher();
		else if(menu == 3) System.exit(0);
		
		return status;
	}
	
	private void studentMenu() {
		System.out.println("메뉴 정보 : (1)출석체크 (2)출석확인 (3)과제제출 (4)과제삭제 (5)성적확인 (6)종료");
		System.out.print("메뉴 선택 : ");
		int menu = Integer.parseInt(sc.nextLine());
		if(menu == 1) attend();
		else if(menu == 2) checkMyattend();
		else if(menu == 3) uploadHw();
		else if(menu == 4) deleteHw();
		else if(menu == 5) checkMyScore();
		else if(menu == 6) System.exit(0);
	}

	private void teacherMenu() {
		System.out.println("메뉴 정보 : (1)학생관리 (2)성적관리 (3)출석관리 (4)종료");
		System.out.print("메뉴 선택 : ");
		int menu = Integer.parseInt(sc.nextLine());
		while(true) {
			if(menu == 1) {
				System.out.println("상세메뉴 정보 : (1)학생조회 (2)메뉴선택 (3)종료");
				System.out.print("메뉴 선택 : ");
				int menuDetail = Integer.parseInt(sc.nextLine());
				if(menuDetail == 1) StudentList();
				else if(menuDetail == 2) {
					System.out.println();
					break;
				}
				else if(menuDetail == 3) System.exit(0);
				
			} else if(menu == 2) {
				System.out.println("상세메뉴 정보 : (1)성적입력 (2)성적수정 (3)성적조회 (4)출석점수연동 (5)메뉴선택 (6)종료");
				System.out.print("메뉴 선택 : ");
				int menuDetail = Integer.parseInt(sc.nextLine());
				if(menuDetail == 1) inputScore();
				else if(menuDetail == 2) updateScore();
				else if(menuDetail == 3) ScoreList();
				else if(menuDetail == 4) attendScoreConnect();
				else if(menuDetail == 5) {
					System.out.println();
					break;
				}
				else if(menuDetail == 6) System.exit(0);
			} else if(menu == 3) {
				System.out.println("상세메뉴 정보 : (1)학생정보연동 (2)출석정보연동 (3)출석정보초기화 (4)출석점수조회 (5)출석상세조회 (6)출석수정 (7)메뉴선택 (8)종료");
				System.out.print("메뉴 선택 : ");
				int menuDetail = Integer.parseInt(sc.nextLine());
				if(menuDetail == 1) studentListConnect();
				else if(menuDetail == 2) attendListConnect();
				else if(menuDetail == 3) initAttendScore();
				else if(menuDetail == 4) attendList();
				else if(menuDetail == 5) attendListDetail();
				else if(menuDetail == 6) updateAttend();
				else if(menuDetail == 7) {
					System.out.println();
					break;
				}
				else if(menuDetail == 8) System.exit(0);
			}
		}
	}

	private int goTeacher() {
		int status = 0;
		System.out.print("아이디를 입력하세요: ");
		id = sc.nextLine();
		System.out.print("비밀번호를 입력하세요: ");
		String pw = sc.nextLine();
		try {
			TeacherDto dto = mmSvc.teacherCheck(id);
			if(dto == null) throw new RecordNotFoundException();
			if(pw.equals(dto.getTeacherPw())) {
				name = dto.getTeacherName();
				System.out.println(name + "님 환영합니다.");
				System.out.println();
				status = 2;
			} else {
				System.out.println("비밀번호 입력 오류입니다.");
				System.out.println();
			}
		} catch (ManagementException e) {
			e.printStackTrace();
			System.err.println("DBMS 오류 발생");
		} catch (RecordNotFoundException e) {
			System.out.println("존재하지 않는 아이디입니다.");
		}
		
		return status;
	}

	private int goStduent() {
		int status = 0;
		
		System.out.print("아이디를 입력하세요: ");
		id = sc.nextLine();
		System.out.print("비밀번호를 입력하세요: ");
		String pw = sc.nextLine();
		try {
			StudentDto dto = mmSvc.studentCheck(id);
			if(dto == null) throw new RecordNotFoundException();
			if(pw.equals(dto.getStudentPw())) {
				name = dto.getStudentName();
				System.out.println(name + "님 환영합니다.");
				System.out.println();
				status = 1;
			} else {
				System.out.println("비밀번호 입력 오류입니다.");
				System.out.println();
			}
		} catch (ManagementException e) {
			e.printStackTrace();
			System.err.println("DBMS 오류 발생");
		} catch (RecordNotFoundException e) {
			System.out.println("존재하지 않는 아이디입니다.");
		}
		
		return status;
	}
	
	private void attend() {
		Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
        String today = format.format(date);
        try {
			if(mmSvc.findTodayAttend(today, id)) {
				System.out.println("이미 출석체크를 완료하였습니다.");
				System.out.println();
			} else {
				mmSvc.attend(new AttendLogDto(0, id, name, null, null, null));
				System.out.println("출석체크가 완료되었습니다.");
				System.out.println();
			}
		} catch (ManagementException e) {
			System.out.println("정보 없음");
		}
	}
	
	private void checkMyattend() {
		try {
			List<AttendLogDto> list = mmSvc.checkAttend(id);
			if(list.size() == 0) throw new ManagementException();
			System.out.println("[" + name + "님의 출결 목록 조회]");
			System.out.printf("%-5s%-14s%-10s%-5s \n", "번호", "출석일자", "출석시간", "상태");
			
			for(AttendLogDto dto : list) {
				System.out.printf("%-6s%-15s%-12s%-5s \n", dto.getAttendNum(), dto.getAttendDate(), dto.getAttendTime(), dto.getStatus());
			}
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("정보 없음");
		}
	}
	
	private void uploadHw() {
		System.out.print("제출할 과제명을 입력하세요 : ");
		String content =sc.nextLine();
		HwLogDto hwDto=new HwLogDto(id, name, content, null);
		boolean checkHw;
		try {
			checkHw = mmSvc.submit(hwDto);
			if(!checkHw) {
				System.out.println("해당 학생은 이미 제출하였습니다");
				System.out.println();
			} else {
				System.out.println("과제 제출이 완료되었습니다");
				System.out.println();
			}
		} catch (ManagementException e) {
			System.out.println("---  서버 오류입니다 ---");
		} 
	}
	
	private void deleteHw() {
		try {
			boolean checkDelete = mmSvc.deleteHw(id);
			if(!checkDelete) {
				throw new RecordNotFoundException();
			}
			System.out.println("과제를 삭제하였습니다");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("---  서버 오류입니다 ---");
		} catch (RecordNotFoundException e) {
			System.out.println("과제를 제출하지 않았습니다");
		}
	}
	
	private void checkMyScore() {
		ScoreDto dto = null;
		try {
			dto = mmSvc.checkScore(id);
			if(dto == null) {
				System.out.println("성적 확인 기간이 아닙니다.");
				System.out.println();
			} else {
				System.out.println("---------------------------------------------------------------------------");
				System.out.println("                             " + name + "님 성적 조회                         ");
				System.out.println("---------------------------------------------------------------------------");
				System.out.println("  아이디      이름      반      중간     과제     기말      출석      총점     등수  ");
				System.out.println("---------------------------------------------------------------------------");
				System.out.println("  " + dto.getStudentId() + "    " + dto.getStudentName() + "    " + dto.getStudentClass() +
						"     " + dto.getMidTerm() + "      " + dto.getHw() + "      " + dto.getFinals() + "       " +
						dto.getAttend() + "       " + dto.getTotalScore() + "     " + dto.getRank() + "  ");
				System.out.println("---------------------------------------------------------------------------");
	            System.out.println();
			}
		} catch (ManagementException e) {
			e.printStackTrace();
			System.err.println("DBMS 오류 발생");
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			System.err.println("성적정보가 없습니다.");
		}
	}
	
	private void StudentList() {
		System.out.println("---------------------------------------------");
		System.out.println("                 학생 목록 조회                 ");
		System.out.println("---------------------------------------------");
		System.out.println("  아이디       이름       전화번호           분반  ");
		System.out.println("---------------------------------------------");
		List<StudentDto> list;
		try {
			list = mmSvc.listStudent();
			for (StudentDto dto : list) {
				System.out.println("  " + dto.getStudentId() + "     " + dto.getStudentName() + "     " + dto.getStudentTel() +
								"    " + dto.getStudentClass() + "  ");
			}
			System.out.println("---------------------------------------------");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("서버 오류 발생");
		}
	}
	
	private void inputScore() {
		try {
			System.out.print("학생의 ID를 입력하세요 : "); 
			String studentId = sc.nextLine();
			StudentDto dto = mmSvc.studentCheck(studentId); 
			
			if(dto == null) {
				throw new RecordNotFoundException();
			} else if(mmSvc.checkScore(studentId)!= null) {
				throw new DuplicatedIdException();
			}
			
			System.out.println(dto.getStudentName()+" 학생의 점수를 입력하세요.");
			System.out.print("중간점수를 입력해주세요 : ");
			int minterm=Integer.parseInt(sc.nextLine());
			
			System.out.print("과제점수를 입력해주세요 : ");
			int hw=Integer.parseInt(sc.nextLine());
			
			System.out.print("기말점수를 입력해주세요 : ");
			int finals=Integer.parseInt(sc.nextLine());
			
			int total = minterm + hw + finals ;
			
			ScoreDto studentdto = new ScoreDto(studentId, dto.getStudentName(), dto.getStudentClass(), minterm, hw, finals, 0, total,0);
			mmSvc.addScore(studentdto);
			System.out.println("성적 입력을 완료하였습니다.");
			System.out.println();
		} catch (ManagementException e) {
			e.printStackTrace();
		} catch (RecordNotFoundException e) {
			System.out.println("해당 학생은 없습니다.");
		} catch (DuplicatedIdException e) {
			System.out.println("해당 학생은 이미 있습니다.");
		}
	}
	
	private void updateScore() {
		try {
			System.out.print("수정할 학생의 ID를 입력하세요 : ");
			String studentId = sc.nextLine();
			ScoreDto dto = mmSvc.checkScore(studentId);
			if(dto == null) {
				throw new RecordNotFoundException();
			}
			System.out.println("수정 선택 : (1)중간 (2)과제 (3)기말");
			System.out.print("메뉴 선택 : ");
			int num= Integer.parseInt(sc.nextLine());
			
			int minterm = dto.getMidTerm();
			int hw = dto.getHw();
			int finals=dto.getFinals();
			
			switch(num) {
				case 1:
					System.out.print("수정할 중간 점수를 입력하세요 : ");
					minterm=Integer.parseInt(sc.nextLine());
					break;
				case 2:
					System.out.print("수정할 과제 점수를 입력하세요 : ");
					hw=Integer.parseInt(sc.nextLine());
					break;
				case 3:
					System.out.print("수정할 기말 점수를 입력하세요 : ");
					finals=Integer.parseInt(sc.nextLine());
					break;
				default :
					throw new ManagementException();
			}
			int total = minterm + hw + finals;
			
			ScoreDto studentdto = new ScoreDto(studentId," "," ",minterm, hw, finals, 0, total, 0);
			mmSvc.updateScore(studentdto);
			System.out.println("점수가 수정되었습니다.");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("수정 오류입니다");
		} catch (RecordNotFoundException e) {
			System.out.println("수정할 학생이 없습니다");
		}
	}
	
	private void attendScoreConnect() {
		try {
			mmSvc.attendScoreConnect();
			System.out.println("출석점수가 연동되었습니다.");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("연동 오류입니다");
		}
	}
	
	private void ScoreList() {
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("                                  성적 조회                                  ");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("  아이디        이름        반       중간      과제      기말       출석      총점  ");
		System.out.println("---------------------------------------------------------------------------");
		List<ScoreDto> list;
		try {
			list = mmSvc.listScore();
			for (ScoreDto dto : list) {
				System.out.println("  " + dto.getStudentId() + "      " + dto.getStudentName() + "      " + dto.getStudentClass() +
						"      " + dto.getMidTerm() + "       " + dto.getHw() + "       " + dto.getFinals() + "        " +
						dto.getAttend() + "       " + dto.getTotalScore() + "  ");
			}
			System.out.println("---------------------------------------------------------------------------");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("서버 오류 발생");
		}
	}
	
	private void studentListConnect() {
		try {
			boolean check = mmSvc.studentListConnect();
			if(check) System.out.println("학생정보가 연동되었습니다.");
			else System.out.println("학생정보가 이미 연동되었습니다.");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("서버 오류 발생");
		}
	}
	
	private void attendListConnect() {
		System.out.print("출석 연동이 필요한 일수를 입력하세요 : ");
		int days = Integer.parseInt(sc.nextLine());
		try {
			mmSvc.attendListConnect(days);
			System.out.println(days + "일의 출결정보가 연동되었습니다.");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("서버 오류 발생");
		}
	}
	
	private void initAttendScore() {
		try {
			mmSvc.initAttendScore();
			System.out.println("출석정보가 초기화되었습니다.");
			System.out.println();
		} catch (ManagementException e) {
			System.out.println("서버 오류 발생");
		}
	}
	
	private void attendList() {
		try {
			List<AttendScoreDto> list = mmSvc.attendScore();
            if(list.size() == 0) throw new ManagementException();
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                              전체 출격 목록 조회                                  ");
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("     아이디        이름        출석일수       지각일수      결석일수      출석점수      ");
            System.out.println("---------------------------------------------------------------------------");
            
            for(AttendScoreDto dto : list) { 
                System.out.println("     "+dto.getStudentId() + "      " + dto.getStudentName() + "         " + dto.getAttendDay() + "            " + dto.getLateDay() + "          " + dto.getMissDay() 
                                    + "          " + dto.getAttendScore());
            }
            System.out.println("---------------------------------------------------------------------------");
            System.out.println();
		} catch (ManagementException e) {
			System.out.println("학생정보를 먼저 연동해주세요.");
			System.out.println();
		}
	}
	
	private void attendListDetail() {
		try {
			System.out.print("출석 상세정보를 확인할 학생의 아이디를 입력하세요 : ");
            String studentId = sc.nextLine();
            List<AttendLogDto> list = mmSvc.checkAttend(studentId);
            if(list.size() == 0) throw new ManagementException();
            System.out.println("-----------------------------------------------------------------");
            System.out.println("                     [" + studentId + "님의 출결 목록 조회]                   ");
            System.out.println("-----------------------------------------------------------------");
            System.out.println("     번호        출석일자        출석시간      상태     ");
            System.out.println("-----------------------------------------------------------------");
            
            for(AttendLogDto dto : list) {
                System.out.println("      "+dto.getAttendNum() + "         "+ dto.getAttendDate()+"   " + dto.getAttendTime()+"     "+ dto.getStatus());
            }
            System.out.println("-----------------------------------------------------------------");
            System.out.println();
		} catch (ManagementException e) {
			System.out.println("해당 학생의 출석정보가 존재하지 않습니다.");
			System.out.println();
		}
	}
	
	private void updateAttend() {
		System.out.print("출석정보를 수정할 출석관리번호를 입력하세요 : ");
		int attendSeq = Integer.parseInt(sc.nextLine());
		
		System.out.print("수정할 시간을 입력하세요(HH:MI:SS) : ");
		String time = sc.nextLine();
		
		try {
			if(mmSvc.updateAttend(new AttendLogDto(attendSeq, null, null, null, time, null))) {
				System.out.println("수정이 완료되었습니다.");
				System.out.println();
			}
		} catch (ManagementException e) {
			System.out.println("서버 오류 발생");
		}
	}
}
