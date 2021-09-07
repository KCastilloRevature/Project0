package utils;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntryPoint {
	public static void main(String[] args) {
		Connection connection = ConnectionUtil.getConnection();
		
		String sqlStatement = "SELECT * FROM Clients";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			
            System.out.println("================ test_table =================");
            while(resultSet.next()) {
                System.out.println("name: ["
                        + resultSet.getString("name")
                        + "]   clientID: ["
                        + resultSet.getInt("clientID")
                        + "]");
            }
            System.out.println("=============== /test_table =================");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
