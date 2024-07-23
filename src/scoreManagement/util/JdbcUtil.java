package scoreManagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcUtil {

	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		String url = "jdbc:oracle:thin:@127.0.0.1:1521/xe";
		String user = "hr";
		String password = "HR";
		
		return DriverManager.getConnection(url, user, password);
	}

	public static void close(PreparedStatement pstmt, Connection con) {
		try {
			if(pstmt != null) pstmt.close();
		} catch (SQLException e) {}
		
		try {
			if(con != null) con.close();
		} catch (SQLException e) {}
	}

}
