package connect;

import java.sql.*;

public class SqliteConnection {
	public static Connection connect() {

        String url = "jdbc:sqlite:EmployeeDB.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}
