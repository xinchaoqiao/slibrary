package com.summer_two.back.Exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String messageString) {
        super(messageString);
    }
}
