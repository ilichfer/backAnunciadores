package  com.anunciadores.dto;
import java.io.Serializable;
import java.util.List;
public class BooksCreationDto
implements Serializable
{
private static final long serialVersionUID = 1L;
private List<Object> books;
public void addBook(Object book) {
this.books.add(book);
}
}
