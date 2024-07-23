package scoreManagement.dto;

import java.util.Date;

public class AttendLogDto {
	private int attendNum;
	private String studentId, studentName;
	private Date attendDate;
	private String attendTime, status;
	
	// Constructor
	public AttendLogDto() {
		
	}

	public AttendLogDto(int attendNum, String studentId, String studentName, Date attendDate, String attendTime,
			String status) {
		super();
		this.attendNum = attendNum;
		this.studentId = studentId;
		this.studentName = studentName;
		this.attendDate = attendDate;
		this.attendTime = attendTime;
		this.status = status;
	}

	// Getter & Setter
	public int getAttendNum() {
		return attendNum;
	}

	public void setAttendNum(int attendNum) {
		this.attendNum = attendNum;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Date getAttendDate() {
		return attendDate;
	}

	public void setAttendDate(Date attendDate) {
		this.attendDate = attendDate;
	}

	public String getAttendTime() {
		return attendTime;
	}

	public void setAttendTime(String attendTime) {
		this.attendTime = attendTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// toString()
	@Override
	public String toString() {
		return "attendNum=" + attendNum + ", studentId=" + studentId + ", studentName=" + studentName
				+ ", attendDate=" + attendDate + ", attendTime=" + attendTime + ", status=" + status;
	}
}
