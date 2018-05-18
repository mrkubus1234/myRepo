package jak.hum.library.crud;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jak.hum.library.data.*;

public class LibraryCrud {
	
	private Properties databaseProperties;
	
	public LibraryCrud() {
		try {
			readDatabaseProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Book> findAllBooksWithAuthors(){
		List<Book> books = new ArrayList<>();
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()) {
			String query = "SELECT books.id, books.title, books.year, authors.name "
					+ "FROM books, authors WHERE books.author_Id = authors.id";
			try(ResultSet rs = stat.executeQuery(query)){
				while(rs.next()) {
					int bookId = rs.getInt("id");
					String bookTitle = rs.getString("books.title");
					int bookYear = rs.getInt("year");
					String bookAuthor = rs.getString("authors.name");
					Book book = new Book(bookTitle, bookYear, bookAuthor);
					book.setId(bookId);
					books.add(book);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	public void addBook(Book book) {
		int authorId = 0;
		ResultSet rs;
		
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
			String query = "SELECT * FROM authors WHERE authors.name = '" + book.getAuthor() + "'";
			if (!stat.executeQuery(query).next()) {
				query = "INSERT INTO authors (name) VALUES ('" + book.getAuthor() + "')";
				stat.executeUpdate(query);
				rs = stat.executeQuery("SELECT id FROM authors ORDER BY id DESC LIMIT 1");
				if (rs.next())
					authorId = rs.getInt("id");
			} else {
				rs = stat.executeQuery("SELECT id FROM authors WHERE authors.name = '" + book.getAuthor() + "'");
				if (rs.next())
					authorId = rs.getInt("id");
			}
			query = "INSERT INTO books (title, year, author_Id) VALUES ('" + book.getTitle() + "', '" + book.getYear() + "', '" + authorId + "')";
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks the book by given Id is lent
	 * @param bookId
	 * @return -1 when book by given ID don't exists,
	 * 			0 when book is lent, 
	 * 			1 if book by given ID has successfully removed
	 */
	public int removeBook(int bookId) {
		
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
			int bookStatus = checkBookStatus(bookId, stat);
			if (bookStatus == -1)
				return -1;
			else if (bookStatus == 1)
				return 0;
			else {
				String query = "DELETE FROM books WHERE books.id = " + bookId;
				stat.executeUpdate(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public List<String> findBooks() {
		List<String> results = new ArrayList<>();
		
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
			String query = "SELECT books.id, books.title, books.year, authors.name,"
					+ " (CASE WHEN lends.book_Id IS NULL THEN 'available' ELSE 'lent' END) AS Avaibility,"
					+ " (CASE WHEN borrowers.borrower_name IS NULL THEN '' ELSE borrowers.borrower_name END) AS Borrower_Name"
					+ " FROM books LEFT JOIN authors ON authors.id=books.author_Id"
					+ " LEFT JOIN lends ON lends.book_Id = books.id"
					+ " LEFT JOIN borrowers ON lends.borrower_Id=borrowers.id"
					+ " ORDER BY books.id";
			ResultSet rs = stat.executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()) {
				StringBuilder builder = new StringBuilder();
				for (int i = 1; i <= meta.getColumnCount()-1; i++) {
					builder.append(meta.getColumnLabel(i) + " = " + rs.getString(i) + "; ");
				}
				if (rs.getString(6).equals("")) {
					results.add(builder.toString());
				} else {
					builder.append(meta.getColumnLabel(6) + " = " + rs.getString(6) + "; ");
					results.add(builder.toString());
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public void lendBook(int bookId, Borrower borrower) {
		ResultSet rs;
		int borrowerId = 0;
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
			int bookStatus = checkBookStatus(bookId, stat);
			if (bookStatus == -1)
				return;
			else if (bookStatus == 1)
				return;
			else {
				String query = "SELECT * FROM borrowers WHERE borrowers.borrower_name = '" + borrower.getName() + "'";
				if (!stat.executeQuery(query).next()) {
					query = "INSERT INTO borrowers (borrower_name) VALUES ('" + borrower.getName() + "')";
					stat.executeUpdate(query);
					rs = stat.executeQuery("SELECT id FROM borrowers ORDER BY id DESC LIMIT 1");
					if (rs.next())
						borrowerId = rs.getInt("id");
				} else {
					rs = stat.executeQuery("SELECT id FROM borrowers WHERE borrowers.borrower_name = '" + borrower.getName() + "'");
					if (rs.next())
						borrowerId = rs.getInt("id");
				}
				query = "INSERT INTO lends (book_Id, borrower_Id) VALUES ('" + bookId + "','" + borrowerId + "')";
				stat.executeUpdate(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> findBooksByAuthor(String author){
		List<String> data = findBooksByArg("author", author);
		return data;
	}
	
	public List<String> findBooksByTitle(String title){
		List<String> data = findBooksByArg("title", title);
		return data;
	}
	
	public List<String> findBooksByYear(int year){
		String yearString = String.valueOf(year);
		List<String> data = findBooksByArg("year", yearString);
		return data;
	}
	
	private List<String> findBooksByArg(String columnName, String arg){
		List<String> results = new ArrayList<>();
		String query = "";
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
			if (columnName.equals("title"))
				query = "SELECT books.id, books.title, books.year, authors.name FROM books, authors WHERE "
					+ "books." + columnName + " = " + "'" + arg + "' AND"
					+ " books.author_Id = authors.id";
			else if (columnName.equals("author"))
				query = "SELECT books.id, books.title, books.year, authors.name FROM books, authors WHERE "
						+ "authors.name = " + "'" + arg + "' AND"
						+ " books.author_Id = authors.id";
			else if (columnName.equals("year"))
				query = "SELECT books.id, books.title, books.year, authors.name FROM books, authors WHERE "
						+ "books." + columnName + " = " + arg + " AND"
						+ " books.author_Id = authors.id";
			//System.out.println(query);
			ResultSet rs = stat.executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			while(rs.next()){
				StringBuilder builder = new StringBuilder();
				for (int i = 1; i <= columnCount; i ++) {
					builder.append(meta.getColumnName(i) + " = " + rs.getString(i) + "; ");
				}
				results.add(builder.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public List<String> findBooksByAuthorAndYear(String author, int year)
	{
		String yearString = String.valueOf(year);
		return findBooksByArguments("author", author, "year", yearString);
	}
	
	public List<String> findBooksByAuthorAndTitle(String author, String title)
	{
		return findBooksByArguments("author", author, "title", title);
	}
	
	public List<String> findBooksByTitleAndYear(String title, int year)
	{
		String yearString = String.valueOf(year);
		return findBooksByArguments("title", title, "year", yearString);
	}
	
	
	private List<String> findBooksByArguments(String firstColumnName, String firstArgument, String secondColumnName, String secondArgument){
		List<String> results = new ArrayList<>();
		String query = "";
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
				if (firstColumnName.equals("author") && (secondColumnName.equals("year") || secondColumnName.equals("title"))) {
					query = "SELECT books.id, books.title, books.year, authors.name FROM books, authors WHERE "
						+ "books." + secondColumnName + " = " + "'" + secondArgument + "' AND authors.name='" + firstArgument + "' AND"
						+ " books.author_Id = authors.id";
					}
				else if (firstColumnName.equals("title")){
					query = "SELECT books.id, books.title, books.year, authors.name FROM books, authors WHERE "
							+ "books.title = " + "'" + firstArgument + "' AND books.year='" + secondArgument + "' AND"
							+ " books.author_Id = authors.id";
				}
				System.out.println(query);
				ResultSet rs = stat.executeQuery(query);
				ResultSetMetaData meta = rs.getMetaData();
				while(rs.next()){
					StringBuilder builder = new StringBuilder();
					for (int i = 1; i <= meta.getColumnCount(); i ++) {
						builder.append(meta.getColumnName(i) + " = " + rs.getString(i) + "; ");
					}
					results.add(builder.toString());
				}
			} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public String findFullInformationById(int id) {
		String query = "";
		String result = "";
		try (Connection connection = getConnection();
				Statement stat = connection.createStatement()){
			query = "SELECT books.id, books.title, books.year, authors.name, "
					+ "(CASE WHEN lends.book_Id IS NULL THEN 'available' ELSE 'lent' END) AS Avaibility, "
					+ "(CASE WHEN borrowers.borrower_name IS NULL THEN '' ELSE borrowers.borrower_name END) AS Borrower_Name"
					+ " FROM books LEFT JOIN authors ON authors.id=books.author_Id "
					+ "LEFT JOIN lends ON lends.book_Id = books.id "
					+ "LEFT JOIN borrowers ON lends.borrower_Id=borrowers.id"
					+ " WHERE books.id = " + id;
			ResultSet rs = stat.executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			StringBuilder builder = new StringBuilder();
			if(rs.next()) {
				for (int i = 1; i <= meta.getColumnCount()-1; i++) {
					builder.append(meta.getColumnLabel(i) + " = " + rs.getString(i) + "; ");
				}
				if (rs.getString(6).equals("")) {
					result = builder.toString();
					return result;
				} else {
					builder.append(meta.getColumnLabel(6) + " = " + rs.getString(6) + "; ");
					result = builder.toString();
					return result;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Checks the book by given Id is lent
	 * @param bookId
	 * @param stat Statement Object
	 * @return -1 when book by given ID don't exists,
	 * 			0 when book is NOT lent,
	 * 			1 when book is lent
	 * @throws SQLException
	 */
	private int checkBookStatus(int bookId, Statement stat) throws SQLException {
		int lendsQuantity;
		ResultSet rs;
		int bookQuantity = checkBookQuantity(bookId, stat);
		if (bookQuantity == 0)
			return -1;
		rs = stat.executeQuery("SELECT COUNT(*) FROM lends WHERE lends.book_Id = " + bookId);
		if (rs.next())
			lendsQuantity = rs.getInt("COUNT(*)");
		else
			lendsQuantity = 0;
		return bookQuantity - lendsQuantity == 0 ? 1 : 0;
	}
	
	private int checkBookQuantity(int bookId, Statement stat) throws SQLException {
		
		int bookQuantity = 0;
		//String bookName = "";
		ResultSet rs;
		rs = stat.executeQuery("SELECT books.id, COUNT(*) FROM books GROUP BY books.title");
		while(rs.next()) {
			if (rs.getInt("id") == bookId) {
				bookQuantity = rs.getInt("COUNT(*)");
				return bookQuantity;
			}
		}
		return bookQuantity;
	}
	
	/*private int readLastBookId(Statement stat) throws SQLException {
		return stat.executeQuery("SELECT id FROM books ORDER BY id DESC LIMIT 1").getInt("id");
	}
	
	private int readLastAuthorId(Statement stat) throws SQLException {
		return stat.executeQuery("SELECT id FROM authors ORDER BY id DESC LIMIT 1").getInt("id");
	}*/
	
	private Connection getConnection() throws SQLException
	{
		String url = databaseProperties.getProperty("jdbc.url");
	    String username = databaseProperties.getProperty("jdbc.username");
	    String password = databaseProperties.getProperty("jdbc.password");
	    
	    return DriverManager.getConnection(url, username, password);
	}
	
	private void readDatabaseProperties() throws IOException
	{
      databaseProperties = new Properties();
      try (InputStream in = Files.newInputStream(Paths.get("database.properties")))
      {
         databaseProperties.load(in);
      }
      String drivers = databaseProperties.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);
	}
}