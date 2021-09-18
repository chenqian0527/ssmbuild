package com.study.service;

import com.study.mapper.BookMapper;
import com.study.pojo.Books;

import java.util.List;

/**
 * @author zengyujie
 * @version 1.0
 * @Description: TODO
 * @Date 2021/9/16 11:22
 */
public class BookServiceImpl implements BookService{

    //Service层调用Dao层
    private BookMapper bookMapper;

    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public int addBook(Books books) {
        return bookMapper.addBook(books);
    }

    public int deleteBookById(int id) {
        return bookMapper.deleteBookById(id);
    }

    public int updateBook(Books books) {
        return bookMapper.updateBook(books);
    }

    public Books queryBookById(int id) {
        return bookMapper.queryBookById(id);
    }

    public List<Books> queryAllBook() {
        return bookMapper.queryAllBook();
    }

    public Books queryBookByName(String bookName) {
        return bookMapper.queryBookByName(bookName);
    }
}
