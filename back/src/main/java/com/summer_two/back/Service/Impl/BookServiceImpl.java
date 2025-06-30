package com.summer_two.back.Service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.summer_two.back.Exception.BookNotFoundException;
import com.summer_two.back.Exception.ServiceException;
import com.summer_two.back.Pojo.Book;
import com.summer_two.back.Pojo.Genre;
import com.summer_two.back.Repository.BookRepository;
import com.summer_two.back.Service.BookService;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("找不到ID为：" + id + "的图书"));
    }

    @Override
    public Book addBook(Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()) != null) {
            throw new IllegalArgumentException("ISBN已存在：" + book.getIsbn());
        }
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setGenre(bookDetails.getGenre());
        book.setPublicationDate(bookDetails.getPublicationDate());

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        // 同时搜索标题和作者
        List<Book> byTitle = bookRepository.findByTitleIgnoreCase(keyword);
        List<Book> byAuthor = bookRepository.findByAuthorIgnoreCase(keyword);

        // 合并结果并去重
        byTitle.addAll(byAuthor);
        return byTitle.stream().distinct().toList();
    }

    @Override
    public List<Book> getBooksByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public Book borrowBook(Long id) {
        Book book = getBookById(id);
        if (!book.isAvailable()) {
            throw new IllegalStateException("图书已被借出");
        }
        book.setAvailable(false);
        return bookRepository.save(book);
    }

    @Override
    public Book returnBook(Long id) {
        Book book = getBookById(id);
        if (book.isAvailable()) {
            throw new IllegalStateException("图书已归还，请勿重复归还");
        }
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAvailableBooks() {
        try{
            logger.info("开始查询可借阅图书");
            List<Book> books = bookRepository.findByAvailableTrue();
            logger.info("成功查询到{}本可借阅图书", books.size());
            if (books.isEmpty()) {
                logger.warn("未找到可借阅图书");
            } else {
                books.forEach(book -> logger.debug("可借阅图书：ID={}", book.getId(), book.getTitle()));
            }
            return books;
        }catch(Exception e){
            logger.error("查阅可借阅图书时发生错误", e);
            throw new ServiceException("获取可借阅图书失败", e);
        }
    }

}
