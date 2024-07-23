package scoreManagement.dto;

public class TeacherDto {
	private String teacherId, teacherPw, teacherName, teacherClass;
	
	public TeacherDto() {
		
	}

	public TeacherDto(String teacherId, String teacherPw, String teacherName, String teacherClass) {
		super();
		this.teacherId = teacherId;
		this.teacherPw = teacherPw;
		this.teacherName = teacherName;
		this.teacherClass = teacherClass;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherPw() {
		return teacherPw;
	}

	public void setTeacherPw(String teacherPw) {
		this.teacherPw = teacherPw;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherClass() {
		return teacherClass;
	}

	public void setTeacherClass(String teacherClass) {
		this.teacherClass = teacherClass;
	}

	@Override
	public String toString() {
		return "teacherId=" + teacherId + ", teacherPw=" + teacherPw + ", teacherName=" + teacherName
				+ ", teacherClass=" + teacherClass;
	}
}
