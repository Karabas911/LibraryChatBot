package client;

import java.sql.*;

//Working with database
public class Database {
	private static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String IP = "192.168.1.56";
	private static String DB_CONNECTION = "jdbc:mysql://"+IP+":3306/library?autoReconnect=true&useSSL=false";
	// I am not sure if it will connect to my MySQL Server remotely form your computer, 
	// I couldn't check it, so if it will not, please change IP variable to "localhost"
	// and create database called library on your MySQlserver and it will work correct	
	private static String DB_USER = "new_user";
	private static String DB_PASSWORD = "password";
	
	
    private static Connection dbConnection = null;
    private static Statement statement = null;
	private static Statement removeStatement = null;

	public static void main(String[] args) {
		try {
	        createDbLibraryTable();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	private static Connection getDBConnection() {
	    Connection dbConnection = null;
	    try {
	        Class.forName(DB_DRIVER);
	    } catch (ClassNotFoundException e) {
	        System.out.println(e.getMessage());
	    }
	    try {
	        dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
	        return dbConnection;
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	    return dbConnection;
	}
	
	public static void createDbLibraryTable() throws SQLException {

	 
	    String createTableSQL = "CREATE TABLE library.books (" + 
	    		"  id INT NOT NULL AUTO_INCREMENT," + 
	    		"  book_author VARCHAR(20) NOT NULL," + 
	    		"  book_name VARCHAR(45) NOT NULL," + 
	    		"  PRIMARY KEY (id))";
	 
	    try {
	        dbConnection = getDBConnection();
	        statement = dbConnection.createStatement();
	        statement.execute(createTableSQL);
	        System.out.println("Table \"books\" is created!");
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        if (statement != null) {
	            statement.close();
	        }
	        if (dbConnection != null) {
	            dbConnection.close();
	        }
	    }
	}
	
	public static void addBook(String author,String bookName) throws SQLException {
		String insert = "INSERT INTO library.books "
				+ "VALUES (null,\'"+author+"\',\'"+bookName+"\');";

	    try {
	        dbConnection = getDBConnection();
	        statement = dbConnection.createStatement();
	        statement.executeUpdate(insert);
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        if (statement != null) {
	            statement.close();
	        }
	        if (dbConnection != null) {
	            dbConnection.close();
	        }
	    }
	    System.out.println("One book added");
	}

	public static ResultSet findBookByName(String bookName) throws SQLException {
		String findBook  = "SELECT * FROM library.books "
				+ "WHERE book_name = \'"+bookName+"\' ;";
		ResultSet resSet = null;
		 try {
		        dbConnection = getDBConnection();
		        statement = dbConnection.createStatement();
		        resSet = statement.executeQuery(findBook);
		        return resSet; 
		    } catch (SQLException e) {
		        System.out.println(e.getMessage());
		    } finally {
//		        if (statement != null) {
//		            statement.close();
//		        }
//		        if (dbConnection != null) {
//		            dbConnection.close();
//		        }
		    }
		 return null;
	}

	public static void removeBook(String bookName) throws SQLException {
		String removeBook  = "DELETE FROM library.books "
				+ "WHERE book_name = \'"+bookName+"\' ;";
		try {
	        removeStatement = dbConnection.createStatement();
	        removeStatement.executeUpdate(removeBook);
	        System.out.println("One book was removed");
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        if (statement != null) {
	            statement.close();
	        }
	        if (dbConnection != null) {
	            dbConnection.close();
	        }
	        if (removeStatement  != null) {
	        	removeStatement.close();
	        }
	    }
	}

	public static void removeBook(String author ,String bookName) throws SQLException {
		String removeBook  = "DELETE FROM library.books "
				+ "WHERE book_name = \'"+bookName+"\' AND book_author = \'"+author+"\' ;";
		try {
			removeStatement = dbConnection.createStatement();
	        removeStatement.executeUpdate(removeBook);
	        System.out.println("One book was removed ");
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        if (statement != null) {
	            statement.close();
	        }
	        if (dbConnection != null) {
	            dbConnection.close();
	        }
	        if (removeStatement  != null) {
	        	removeStatement.close();
	        }
	    }
	}

	public static ResultSet getAllBooks() {
		String allBooks  = "SELECT * FROM library.books "
				+ "ORDER BY book_name ;";
		ResultSet resSet = null;
		 try {
			  dbConnection = getDBConnection();
		      statement = dbConnection.createStatement();
		      resSet = statement.executeQuery(allBooks);
		      return resSet;
		 } catch (SQLException e) {
		        System.out.println(e.getMessage());
		 }
		 return resSet;
	}

	public static void closeStatement() throws SQLException {
		 if (statement != null) {
	            statement.close();
	        }
	     if (dbConnection != null) {
	            dbConnection.close();
	        }
	     if (removeStatement  != null) {
	        	removeStatement.close();
	        }
	}

	public static void updateBookName(String editBookName, String newBookName) throws SQLException {
		String updateBook  = "UPDATE library.books "
				+ "SET book_name = \'"+newBookName+"\'"
						+ "WHERE book_name = \'"+editBookName+"\';";
		try {
	        removeStatement = dbConnection.createStatement();
	        removeStatement.executeUpdate(updateBook);
	        System.out.println("One book was removed");
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        if (statement != null) {
	            statement.close();
	        }
	        if (dbConnection != null) {
	            dbConnection.close();
	        }
	        if (removeStatement  != null) {
	        	removeStatement.close();
	        }
	    }
	}
	
}
