package com.summer_two.back.Service;

import java.util.List;

import com.summer_two.back.Pojo.Book;
import com.summer_two.back.Pojo.Genre;

public interface BookService {
    // 获取所有图书
    List<Book> getAllBooks();

    // 根据ID获取图书
    Book getBookById(Long id);

    // 添加新书
    Book addBook(Book book);

    // 更新图书信息
    Book updateBook(Long id, Book bookDetails);

    // 删除图书
    void deleteBook(Long id);

    // 搜索图书
    List<Book> searchBooks(String keyword);

    // 按分类获取图书
    List<Book> getBooksByGenre(Genre genre);

    // 借阅图书
    Book borrowBook(Long id);

    // 归还图书
    Book returnBook(Long id);

    // 获取可借阅图书
    List<Book> getAvailableBooks();
}
