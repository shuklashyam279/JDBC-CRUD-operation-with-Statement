package com.jspiders.jdbc_crud_with_statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteStudentController {
	
	public static void main(String[] args) {
		
		Connection connection = null;
		Statement statement = null;
		
		final String url = "jdbc:mysql://127.0.0.1:3306/jspiders";
		final String userName = "root";
		final String password = "Password@1234";
		
		try {
			
			// STEP 1 : to Load/Register the Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// STEP 2 : to establish the Connection
			connection = DriverManager.getConnection(url, userName, password);
			
			// STEP 3 : Creating the Statement to communicate with database
			statement = connection.createStatement();
			System.out.println("Successful Connection Established.");
			
			// STEP 4 : Making statement query and executing it.
			String deleteStudentQuery = "delete from student where id  =3;";
			statement.execute(deleteStudentQuery);
			

			System.out.println("Successfully Data Updated.");
		}catch(ClassNotFoundException | SQLException e){
			e.printStackTrace();
		}finally {
			try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}