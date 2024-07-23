package scoreManagement.dto;

public class StudentDto {
	private String studentId, studentName, studentPw, studentTel, studentClass;
	
	public StudentDto() {
		
	}

	public StudentDto(String studentId, String studentName, String studentPw, String studentTel, String studentClass) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
		this.studentPw = studentPw;
		this.studentTel = studentTel;
		this.studentClass = studentClass;
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

	public String getStudentPw() {
		return studentPw;
	}

	public void setStudentPw(String studentPw) {
		this.studentPw = studentPw;
	}

	public String getStudentTel() {
		return studentTel;
	}

	public void setStudentTel(String studentTel) {
		this.studentTel = studentTel;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	@Override
	public String toString() {
		return "studentId=" + studentId + ", studentName=" + studentName + ", studentPw=" + studentPw
				+ ", studentTel=" + studentTel + ", studentClass=" + studentClass;
	}
}
