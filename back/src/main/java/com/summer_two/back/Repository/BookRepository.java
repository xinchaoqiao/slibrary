package com.summer_two.back.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.summer_two.back.Pojo.Book;
import com.summer_two.back.Pojo.Genre;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Long>{
    //按标题查找
    List<Book> findByTitleIgnoreCase(String title);

    //按作者查找
    List<Book> findByAuthorIgnoreCase(String author);

    //按ISBN查找
    Book findByIsbn(String isbn);

    //按分类查找
    List<Book> findByGenre(Genre genre);

    //查找可借阅的图书
    List<Book> findByAvailableTrue();

}
