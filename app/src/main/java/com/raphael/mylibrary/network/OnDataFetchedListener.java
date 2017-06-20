package com.raphael.mylibrary.network;

import com.raphael.mylibrary.model.Book;

import java.util.ArrayList;

/**
 * Created by Raphael Bragança on 20/05/2016.
 */
public interface OnDataFetchedListener {

     void updateUI(ArrayList<Book> books);
     void displayErrorMessage(String errorMessage);

}
