package scoreManagement.dto;

public class AttendScoreDto {
	private String studentId, studentName;
	private int lateDay, missDay, attendDay, attendScore;
	
	// Constructor
	public AttendScoreDto() {
		
	}

	public AttendScoreDto(String studentId, String studentName, int lateDay, int missDay, int attendDay, int attendScore) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
		this.lateDay = lateDay;
		this.missDay = missDay;
		this.attendDay = attendDay;
		this.attendScore = attendScore;
	}

	// Getter & Setter
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

	public int getLateDay() {
		return lateDay;
	}

	public void setLateDay(int lateDay) {
		this.lateDay = lateDay;
	}

	public int getMissDay() {
		return missDay;
	}

	public void setMissDay(int missDay) {
		this.missDay = missDay;
	}

	public int getAttendDay() {
		return attendDay;
	}

	public void setAttendDay(int attendDay) {
		this.attendDay = attendDay;
	}

	public int getAttendScore() {
		return attendScore;
	}

	public void setAttendScore(int attendScore) {
		this.attendScore = attendScore;
	}

	// toString()
	@Override
	public String toString() {
		return "studentId=" + studentId + ", studentName=" + studentName + ", lateDay=" + lateDay + ", missDay=" + missDay + ", attendDay=" + attendDay + ", attendScore=" + attendScore;
	}
}
