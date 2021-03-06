package com.example.twozer00.booksearch.booksearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.twozer00.booksearch.booksearch.adapters.BookAdapter;
import com.example.twozer00.booksearch.booksearch.adapters.MovieRecomendationAdapter;
import com.example.twozer00.booksearch.booksearch.adapters.PopularMoviesAdapter;
import com.example.twozer00.booksearch.booksearch.models.Book;
import com.example.twozer00.booksearch.booksearch.models.MovieRecomendation;
import com.example.twozer00.booksearch.booksearch.models.PopularMovies;
import com.example.twozer00.booksearch.booksearch.net.BookClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PopularMoviesActivity extends AppCompatActivity {
    public static final String BOOK_DETAIL_KEY = "movie";
    private ListView lvBooks;
    private ListView PopularMovie;
    //private BookAdapter bookAdapter;
    private PopularMoviesAdapter MovieAdapter;


    private BookClient client;
    private ProgressBar progress;
    //private Reci
    //private AsyncHttpClient client1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies_activity);
        lvBooks = (ListView) findViewById(R.id.lvMovies);
        //PopularMovie = (ListView) findViewById(R.id.PopularMovie);
        ArrayList<Book> aBooks = new ArrayList<Book>();
        //bookAdapter = new BookAdapter(this, aBooks);
        MovieAdapter = new PopularMoviesAdapter(this,aBooks);
        lvBooks.setAdapter(MovieAdapter);
        progress = (ProgressBar) findViewById(R.id.progressBP);
        Log.d("query",client.PopularMovies());
        PopularMovies();
        setupBookSelectedListener();
        //fetchBooks(client.getPopular(new JsonHttpResponseHandler()));
    }
    public void setupBookSelectedListener() {
        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the detail view passing book as an extra
                Intent intent = new Intent(PopularMoviesActivity.this, BookDetailActivity.class);
                //Intent intent1 = new Intent(BookListActivity.this, BookDetailActivity.class);
                intent.putExtra(BOOK_DETAIL_KEY, MovieAdapter.getItem(position));
                //intent1.putExtra(BOOK_DETAIL_KEY, MovieAdapter.getItem(position));
                startActivity(intent);
                //startActivity(intent1);
            }
        });
    }
    private void PopularMovies() {
        progress.setVisibility(ProgressBar.VISIBLE);
        PopularMoviesActivity.this.setTitle("Popular Movies");
        client = new BookClient();
        client.getPopularMovies(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("Ejecutando","Me estoy ejecutando");
                    Toast toast1=Toast.makeText(getBaseContext(),"Has been found: "+response.getString("total_results")+" results",Toast.LENGTH_LONG);
                    //toast1.setGravity(0,0,((Gravity.END)-50));

                    progress.setVisibility(ProgressBar.GONE);
                    JSONArray docs = null;
                    if(response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("results");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        MovieAdapter.clear();
                        // Load model objects into the adapter
                        for (Book book : books) {
                            MovieAdapter.add(book);
                            // add book through the adapter
                        }
                        MovieAdapter.notifyDataSetChanged();
                    }
                    //toast1.show();
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(ProgressBar.GONE);

            }
        });
    }


}
