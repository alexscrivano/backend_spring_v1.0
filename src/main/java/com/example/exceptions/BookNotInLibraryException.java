package com.example.exceptions;

public class BookNotInLibraryException extends Exception{
    public BookNotInLibraryException(String message){
        super(message);
    }
}
