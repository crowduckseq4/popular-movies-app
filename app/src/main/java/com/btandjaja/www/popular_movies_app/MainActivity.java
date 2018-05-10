package com.btandjaja.www.popular_movies_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private TextView mError;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private boolean mSort;

    /* detail activity constants */
    protected String ORIGINAL_TITLE = "original_title";
    protected String IMAGE_THUMBNAIL = "image_thumbnail";
    protected String OVER_VIEW = "over_view";
    protected String RATING = "vote_average";
    protected String RELEASE_DATE = "release_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* initialize variables */
        initializedDisplayVariables();
        /* get movie data */
        loadMoviesData();
        /* we don't want to sort when activity started */
        mSort = false;
    }

    /* initialize variables */
    private void initializedDisplayVariables() {
        mError = findViewById(R.id.tv_error);
        mProgressBar = findViewById(R.id.pb_view);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    /* get movies data */
    private void loadMoviesData() {
        showMoviesDataView();
        getDataFromNetwork();
    }

    /* connect to network retrieve data */
    private void getDataFromNetwork() {
        URL movieSearchUrl = NetworkUtils.buildUrl();
        new Movies().execute(movieSearchUrl);
    }

    /* show movie data */
    private void showMoviesDataView() {
        mError.setVisibility(View.INVISIBLE);
    }

    /* show error message */
    private void showErrorMessage() {
        mError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Toast.makeText(MainActivity.this, movie.getTitle(), Toast.LENGTH_LONG).show();
        Intent detailIntent = new Intent(this, Detail.class);
        detailIntent.putExtra(ORIGINAL_TITLE, movie.getTitle());
        detailIntent.putExtra(IMAGE_THUMBNAIL, movie.getPosterPath());
        detailIntent.putExtra(OVER_VIEW, movie.getOverView());
        detailIntent.putExtra(RATING, movie.getVoteAvg());
        detailIntent.putExtra(RELEASE_DATE, movie.getReleaseDate());
        startActivity(detailIntent);
    }

    private class Movies extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String movieSearchResult = null;
            try {
                movieSearchResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieSearchResult;
        }

        @Override
        protected void onPostExecute(String movieJsonString) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(movieJsonString!=null && !movieJsonString.equals("")) {
                if(mSort) {

                } else {
                    MovieUtils.getMovieList(movieJsonString, mMovieList);
                }
                createAndSetAdapter();
            }
            else showErrorMessage();
        }
    }

    private void createAndSetAdapter(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, mMovieList));
    }

    /* Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_settings){
            /* do something */
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
