package com.raphael.mylibrary.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.raphael.mylibrary.R;
import com.raphael.mylibrary.database.SqlHelper;
import com.raphael.mylibrary.model.Book;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raphael Bragança on 01/06/2016.
 */
public class LibraryDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookISBN;
    private TextView bookYear;
    private TextView bookCategories;
    private TextView bookDescription;
    private TextView bookComment;
    private RatingBar bookPersonalRate;
    private RatingBar bookOfficialRate;
    private ImageView bookCover;
    private ScrollView scrollView;
   // private CheckBox checkBox_read;
    private CheckBox checkbox_favorite;

    private Book book;
    private SqlHelper database;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_book_detail);

        getSupportActionBar().setTitle("Detalhes");
        Bundle bundle = getIntent().getExtras();
        book = bundle.getParcelable("book");

        bookTitle = (TextView) findViewById(R.id.book_title);
        bookAuthor = (TextView) findViewById(R.id.book_author);
        bookISBN = (TextView) findViewById(R.id.book_isbn);
        bookYear = (TextView) findViewById(R.id.book_year);
        bookCategories = (TextView) findViewById(R.id.book_categories);
        bookDescription = (TextView) findViewById(R.id.book_description);
        bookComment = (TextView) findViewById(R.id.book_comment);
        bookPersonalRate = (RatingBar) findViewById(R.id.book_personalRate);
        bookOfficialRate = (RatingBar) findViewById(R.id.book_officialRate);
        bookCover = (ImageView) findViewById(R.id.book_cover);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        checkbox_favorite = (CheckBox) findViewById(R.id.checkbox_favorite);
       // checkBox_read = (CheckBox) findViewById(R.id.checkbox_read);

        bookDescription.setMovementMethod(new ScrollingMovementMethod());
        bookComment.setMovementMethod(new ScrollingMovementMethod());

        //To be able to scroll the description Textview
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bookDescription.getParent().requestDisallowInterceptTouchEvent(false);
                bookComment.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        bookDescription.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                bookDescription.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        bookComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bookComment.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setCheckBoxes();
       // checkBox_read.setOnCheckedChangeListener(this);
        checkbox_favorite.setOnCheckedChangeListener(this);

    }

    @Override
    public void onStart(){

        super.onStart();
        database = new SqlHelper(getApplicationContext());
        if (book.getUrlNormalCover().equals("unknown")) {
            Picasso.with(getApplicationContext()).load(R.mipmap.book_not_found).into(bookCover);
        }else {
            Picasso.with(getApplicationContext()).load(book.getUrlNormalCover()).into(bookCover);
        }
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookOfficialRate.setRating(book.getOfficialRate());
        bookPersonalRate.setRating(book.getPersonalRate());
        bookYear.setText(formatPublishedDate(book.getYear()));
        bookISBN.setText(book.getISBN());
        bookCategories.setText(book.getCategories().get(0));

        if (book.getDescription().length() == 0){
            bookDescription.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bookComment.getLayoutParams();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                params.addRule(RelativeLayout.BELOW,R.id.linearLayoutGlobal);
            }else{
                params.addRule(RelativeLayout.BELOW, R.id.book_isbn);
            }

        }else {
            bookDescription.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bookComment.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.book_description);
            bookDescription.setText(book.getDescription());
        }

        bookComment.setText(Html.fromHtml(getString(R.string.label_comment,book.getComment())));


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private String formatPublishedDate(String date)  {

        Date currentDate = null;
        if (date == "unknown" || date.length() < 10) {
            return date;
        }else{
            try {
                currentDate = new SimpleDateFormat("yyyy-mm-dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new SimpleDateFormat("dd-mm-yyyy").format(currentDate);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.isPressed()){
            switch(buttonView.getId()){
                case R.id.checkbox_favorite:
                    if(isChecked){
                        book.setIsFavorite(true);
                        database.updateIsFavoriteColumn(book,getUserAccount().getId());
                        Toast.makeText(getApplicationContext(),"Livro adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        book.setIsFavorite(false);
                        database.updateIsFavoriteColumn(book,getUserAccount().getId());
                        Toast.makeText(getApplicationContext(),"book removed from your favorite", Toast.LENGTH_SHORT).show();
                        break;
                    }
            }
        }

    }

    public void setCheckBoxes(){

        if (book.getIsFavorite()){
            checkbox_favorite.setChecked(true);
        }else{
            checkbox_favorite.setChecked(false);
        }
    }
}


