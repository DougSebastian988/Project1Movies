package com.example.android.project1movies.Utilities;

import android.content.Context;

import com.example.android.project1movies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;




/**
 * Created by Doug on 1/31/2017.
 */

public class MovieDBJsonUtils {
    /**
     * This method parses JSON from a web response and returns an array of Movie objects
     * describing movies from the Movie DB.
     * <p/>
     *
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Movie objects describing a movie from the Movie DB
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */

    public static List<Movie> getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        /* Movie information. Each movie description is an element of the "list" array */
        final String MOVIE_DB_RESULTS = "results";

        final String POSTER_PATH = "poster_path";

        final String OVERVIEW = "overview";

        final String RELEASE_DATE = "release_date";

        final String TITLE = "title";

        final String VOTE_AVERAGE = "vote_average";

        final String OWM_MESSAGE_CODE = "cod";

        /* Get top level Json object */
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;

            }
        }
        /* Get the array of movies */
        JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_DB_RESULTS);

        List<Movie> movies = new ArrayList<Movie>();


        for (int i = 0; i < moviesArray.length(); i++) {

             /* These are the values that will be collected */
            String title;
            String synopsis;
            double userRating;
            String releaseDate;
            String posterPath;

            /* Get the JSON object representing the movie */
            JSONObject movieObj = moviesArray.getJSONObject(i);

            posterPath = movieObj.getString(POSTER_PATH);
            synopsis = movieObj.getString(OVERVIEW);
            releaseDate = movieObj.getString(RELEASE_DATE);
            title = movieObj.getString(TITLE);
            userRating = movieObj.getDouble(VOTE_AVERAGE);

            Movie movie = new Movie(title, synopsis, userRating, releaseDate, posterPath);

            movies.add(movie);
        }

        return movies;
    }

}
