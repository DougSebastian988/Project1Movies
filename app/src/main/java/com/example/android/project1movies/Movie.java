package com.example.android.project1movies;

/**
 * Created by Doug on 1/30/2017.
 */

public class Movie {
    public static final String TITLE = "MOVIE_TITLE";
    public static final String SYNOPSIS = "MOVIE_SYNOPSIS";
    public static final String RELEASE_DATE = "MOVIE_RELEASE_DATE";
    public static final String USER_RATING = "MOVIE_USER_RATING";
    public static final String POSTER_PATH = "MOVIE_POSTER_PATH";

    String title;
    String synopsis;
    double userRating;
    String releaseDate;
    String posterPath;

    public Movie( String title, String synopsis, double userRating, String releaseDate,
            String posterPath)
    {
        this.title = title;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

}