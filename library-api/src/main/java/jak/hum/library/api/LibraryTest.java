package jak.hum.library.api;
import java.util.List;

import jak.hum.library.crud.LibraryCrud;
import jak.hum.library.data.Book;
import jak.hum.library.data.Borrower;

public class LibraryTest {

	public static void main(String[] args) {
		LibraryCrud library = new LibraryCrud();
		
		//library.addBook(new Book("Tytulek", 2000, "First Author"));
		
		/*List<Book> books = library.findAllBooksWithAuthors();
		for(Book book : books)
		{
			System.out.println(book);
		}*/
		
		/*int status = library.removeBook(15);
		System.out.println("Status usuniecia: " + status);*/
		
		//System.out.println(library.findFullInformationById(1));
		
		
		//library.lendBook(7, new Borrower("Second Borrower"));
		
		
		/*List<String> books = library.findBooks();
		
		for(String book : books)
		{
			System.out.println(book);
		}*/
		
		
		/*List<String> booksByTitle = library.findBooksByTitle("Book1");
		
		for(String book : booksByTitle)
		{
			System.out.println(book);
		}*/
		
		/*
		List<String> bookByYear = library.findBooksByYear(1111);
		
		for(String book : bookByYear)
		{
			System.out.println(book);
		}*/
		
		List<String> booksByAuthor = library.findBooksByAuthor("First Author");
		
		for(String book : booksByAuthor)
		{
			System.out.println(book);
		}
		
	}
}
