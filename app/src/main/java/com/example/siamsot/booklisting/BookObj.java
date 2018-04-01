package com.example.siamsot.booklisting;

public class BookObj {
    private String mBookTitle;
    private String mBookAuthor;



    BookObj(String BookTitle, String BookAuthor){
        mBookTitle=BookTitle;
        mBookAuthor=BookAuthor;
    }

    public String getTitle() {
        return mBookTitle;
    }

    public String getAuthor() {
        return mBookAuthor;
    }

    }

