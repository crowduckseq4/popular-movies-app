package com.btandjaja.www.popular_movies_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieUtils {
    /* movies data constants to retrieve data*/
    private final static String RESULTS = "results";
    private final static String AVERAGE = "vote_average";
    private final static String POPULARITY = "popularity";
    private final static String ORIGINAL_TITLE = "original_title";
    private final static String POSTER_PATH = "poster_path";
    private final static String OVERVIEW = "overview";
    private final static String RELEASE_DATE = "release_date";
    private static ArrayList<Movie> mMovies = new ArrayList<Movie>();

    public static ArrayList<Movie> getMovieList(String jsonMovies) {
        try {
            JSONObject movieJsonObj = new JSONObject(jsonMovies);
            JSONArray movieJsonArr = movieJsonObj.getJSONArray(RESULTS);
            for(int i = 0; i < movieJsonArr.length(); i++) {
                JSONObject singleMovie = movieJsonArr.getJSONObject(i);
                Double voteAvg = singleMovie.getDouble(AVERAGE);
                Double popularity = singleMovie.getDouble(POPULARITY);
                String originalTitle = singleMovie.getString(ORIGINAL_TITLE);
                String posterPath = singleMovie.getString(POSTER_PATH);
                String overView = singleMovie.getString(OVERVIEW);
                String releaseDate = singleMovie.getString(RELEASE_DATE);
                mMovies.add(new Movie(voteAvg, popularity, originalTitle, posterPath, overView,
                        releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovies;
    }

    public static ArrayList<Movie> copy(ArrayList<Movie> movieList) {
        ArrayList<Movie> newMovieList = new ArrayList<>();
        for (Movie movie : movieList) {
            Double voteAvg = movie.getVoteAvg();
            Double popularity = movie.getPopularity();
            String originalTitle = movie.getTitle();
            String posterPath = movie.getPosterPath();
            String overView = movie.getOverView();
            String releaseDate = movie.getReleaseDate();
            newMovieList.add(new Movie(voteAvg, popularity, originalTitle, posterPath, overView,
                    releaseDate));
        }
        return newMovieList;
    }
}