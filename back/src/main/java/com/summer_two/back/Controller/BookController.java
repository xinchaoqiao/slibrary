package com.summer_two.back.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summer_two.back.Pojo.Book;
import com.summer_two.back.Pojo.Genre;
import com.summer_two.back.Service.BookService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 获取所有图书
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // 获取单个图书
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookByid(@PathVariable Long id) {// 把路径抓出来用
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // 添加新书
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
    }

    // 更新图书
    @PutMapping("/{id}") // @RequestBody 处理Post和Put请求 前端传 JSON 数据，后端用 @RequestBody 直接映射到实体类
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {

        return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
    }

    // 删除图书
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // 搜索图书
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }

    // 按分类获取图书
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable Genre genre) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }

    // 借阅图书
    @PostMapping("/{id}/borrow")
    public ResponseEntity<Book> borrowBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.borrowBook(id));
    }

    // 归还图书
    @PostMapping("/{id}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.returnBook(id));
    }

    // 获取可借阅图书
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        logger.info("获取可借阅图书请求");
        try{
            List<Book> availableBooks = bookService.getAvailableBooks();
            return ResponseEntity.ok(availableBooks);
        }catch(Exception e){
            logger.error("获取可借阅图书出错", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
