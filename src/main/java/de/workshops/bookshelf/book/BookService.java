package de.workshops.bookshelf.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    private final List<Book> books = new ArrayList<>();

    @PostConstruct
    public void init() {
        bookRepository.findAll().forEach(books::add);
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getSingleBook(String isbn) throws BookException {
        return books.stream().filter(book -> hasIsbn(book, isbn)).findFirst().orElseThrow(BookException::new);
    }

    public Book searchBookByAuthor(String author) throws BookException {
        return books.stream().filter(book -> hasAuthor(book, author)).findFirst().orElseThrow(BookException::new);
    }

    public List<Book> searchBooks(BookSearchRequest request) {
        return books.stream()
                .filter(book -> hasAuthor(book, request.getAuthor()))
                .filter(book -> hasIsbn(book, request.getIsbn()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Book createBook(Book book) {
        books.add(book);

        return book;
    }

    private boolean hasIsbn(Book book, String isbn) {
        return book.getIsbn().equals(isbn);
    }

    private boolean hasAuthor(Book book, String author) {
        return book.getAuthor().contains(author);
    }
}
