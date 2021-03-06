package com.epam.university.java.project.service;

import com.epam.university.java.project.core.cdi.impl.io.XmlResource;
import com.epam.university.java.project.core.cdi.io.Resource;
import com.epam.university.java.project.core.state.machine.domain.StateMachineDefinition;
import com.epam.university.java.project.core.state.machine.manager.StateMachineManager;
import com.epam.university.java.project.domain.Book;
import com.epam.university.java.project.domain.BookEvent;
import com.epam.university.java.project.domain.BookImpl;
import com.epam.university.java.project.domain.BookStatus;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Created by Александр on 17.10.2017.
 * Service for book management.
 */
public class BookServiceImpl implements BookService {
    private BookDao bookDao;
    private StateMachineManager stateMachineManager = new StateMachineManagerImpl();
    private Resource xmlResource = new XmlResource(
            getClass().getResource("/project/DefaultBookStateMachineDefinition.xml")
                    .getFile());

    /**
     * Create new draft book instance.
     *
     * @return new book instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public Book createBook() {
        return (Book) stateMachineManager.handleEvent(
                stateMachineManager.initStateMachine(bookDao.createBook(),
                        (StateMachineDefinition<BookStatus, BookEvent>)
                                stateMachineManager.loadDefinition(xmlResource)),
                BookEvent.CREATE);
    }

    /**
     * Get the book by id.
     *
     * @param id book id
     * @return book instance
     */
    @Override
    public Book getBook(int id) {
        return bookDao.getBook(id);
    }

    /**
     * Get all books.
     *
     * @return books collection
     */
    @Override
    public Collection<Book> getBooks() {
        return bookDao.getBooks();
    }

    /**
     * Remove book from library.
     *
     * @param book book to remove
     */
    @Override
    public void remove(Book book) {
        bookDao.remove(book);
    }

    /**
     * Save book to library.
     *
     * @param book book to save
     * @return saved book instance
     */
    @Override
    public Book save(Book book) {
        return bookDao.save(book);
    }

    /**
     * Accept book into the account with the following <code>number</code>.
     *
     * @param book   book to accept
     * @param number book number
     * @return accounted book
     */
    @Override
    public Book accept(Book book, String number) {
        book.setSerialNumber(number);
        return bookDao.save((Book) stateMachineManager.handleEvent(book, BookEvent.ACCEPT));
    }

    /**
     * Issue book and set return date.
     *
     * @param book       book to issue
     * @param returnDate return date
     * @return issued book
     */
    @Override
    public Book issue(Book book, LocalDate returnDate) {
        book.setReturnDate(returnDate);
        return (Book) stateMachineManager.handleEvent(book, BookEvent.ISSUE);
    }

    /**
     * Return book from issue.
     *
     * @param book book to return
     * @return accounted book
     */
    @Override
    public Book returnFromIssue(Book book) {
        return (Book) stateMachineManager.handleEvent(book, BookEvent.RETURN);
    }
}
