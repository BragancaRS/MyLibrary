package com.raphael.mylibrary.network;

import com.google.gson.annotations.SerializedName;
import com.raphael.mylibrary.model.Book;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Raphael Bragança on 20/05/2016.
 */
public interface BookService {

    @GET("books/v1/volumes")
    Call<BookAnswer> findBook(@Query("q") String query, @Query("key") String keyAPI,
                              @Query("maxResults") int numberMax);

    class BookAnswer{

        @SerializedName(value="items")
        ArrayList<BookItems> bookItems;

        @SerializedName(value="totalItems")
        int totalItems;

        public int getTotalItems() { return totalItems; }
        public ArrayList<BookItems> getBooks() { return bookItems; }
    }

     class BookItems{

        @SerializedName(value="volumeInfo")
        Book bookData;

        public Book getBookData(){return bookData; }

    }

}


