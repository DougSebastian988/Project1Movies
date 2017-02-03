package com.example.android.project1movies;

/**
 * Created by Doug on 1/30/2017.
 */


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.project1movies.Utilities.MovieDBJsonUtils;
import com.example.android.project1movies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * A fragment containing the list view of Android versions.
 */
public class MainActivityFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {

    /* keys for saving instance state */
    public static final String MOVIE_DB_SORT_TYPE = "MOVIE_DB_SORT_TYPE";
    public static final String DISPLAY_DETAIL_MOVIE = "DISPLAY_DETAIL_MOVIE";
    public static final String NO_MOVIE_DISPLAYED = "no movie displayed";

    /* movie sort types */
    public static final String MOVIE_DB_SORT_POPULAR = "popular";
    public static final String MOVIE_DB_SORT_TOP_RATED = "top_rated";

    /* kludge variable for getting the instance of this fragment */
    /* I tried to use getSupportFragmentManager but was unsuccessful in MainActivity */
    public static MainActivityFragment mMainActivityFragment;

    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private GridView mMoviesView;

    public static String mMoviesSortType = MOVIE_DB_SORT_POPULAR ;

    public static String mMovieDisplayed = NO_MOVIE_DISPLAYED;

    private List<Movie> mMovies = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recovering the instance state

        if (savedInstanceState != null) {
            mMoviesSortType = savedInstanceState.getString(MOVIE_DB_SORT_TYPE);
            mMovieDisplayed = savedInstanceState.getString(DISPLAY_DETAIL_MOVIE);
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mMoviesView = (GridView) rootView.findViewById(R.id.gridview_movies);

        mMainActivityFragment = this;

     /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
         mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

         mErrorMessageDisplay = (TextView) rootView.findViewById(R.id.error_message_display);

         /* Once all of our views are setup, we can load the weather data. */
         loadMovieData();

        return rootView;
    }

     /*
       * @param sortType: the type of sort to use when requesting movies from the movie db.
      */
     public void setMovieSortType(String sortType) {
         mMoviesSortType = sortType;
         loadMovieData();
     }
    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadMovieData() {
        showMovieDataView();
        new FetchMoviesTask().execute(mMoviesSortType);
    }
    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mMoviesView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movies
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mMoviesView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /*
    *  Background taks to fetch movie data from the movie db.
    *  S
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            /* If there is no sort type there is no request to make. */
            if (params.length == 0) {
                return null;
            }

            String movieSortType = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(movieSortType);

            try {
                /* Get the Json movie data from the movie db. */
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);
                /* parse the Json String data into Movie objects */
                List<Movie> jsonMoviesData = MovieDBJsonUtils
                        .getMoviesFromJson(getContext(), jsonMoviesResponse);

                return jsonMoviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> moviesData) {
             /* do we have movies */
            if (moviesData != null) {
                /* save movies for later reference */
                mMovies = moviesData;

                /* create a movie grid adapter for the given movie list */
                mMovieAdapter = new MovieAdapter(getActivity(), moviesData, MainActivityFragment.this);
                /* attach the adapter to our grid view */
                mMoviesView.setAdapter(mMovieAdapter);
                /* we are done loading */
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                /* make movies view visible */
                showMovieDataView();

                /* If a detailed movie view was previously displayed then bring that move view up
                /* again.
                 */
                if (!mMovieDisplayed.equals(NO_MOVIE_DISPLAYED)) {
                    for (Movie movie : mMovies) {
                        if (movie.title.equals(mMovieDisplayed)) {
                            onClick(movie);
                            break;
                        }
                    }
                }
            } else {
                /* no movies equals an error condition */
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        }
    }

    /**
     * This method is overridden by our MainActivityFragment class in order to handle AdapterView item
     * clicks.
     *
     * @param movie The movie poster that was clicked
     */
    @Override
    public void onClick(Movie movie) {
        Context context = getActivity();
        Class destinationClass = MovieDetailActivity.class;

        mMovieDisplayed = movie.title;

        /* Create intent to bring up detailed information for this movie. */

        Intent intentToStartMovieDetailActivity = new Intent(context, destinationClass);

        /* Push the movie information to the intent */
        intentToStartMovieDetailActivity.putExtra(Movie.TITLE, movie.title);
        intentToStartMovieDetailActivity.putExtra(Movie.SYNOPSIS, movie.synopsis);
        intentToStartMovieDetailActivity.putExtra(Movie.USER_RATING, movie.userRating);
        intentToStartMovieDetailActivity.putExtra(Movie.RELEASE_DATE, movie.releaseDate);
        intentToStartMovieDetailActivity.putExtra(Movie.POSTER_PATH, movie.posterPath);

        /* start the detailed movie activity */
        getActivity().startActivity(intentToStartMovieDetailActivity);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        /* save the sort type and name of any movie that is being displayed with detailed info. */
        outState.putString(MOVIE_DB_SORT_TYPE, mMoviesSortType);
        outState.putString(DISPLAY_DETAIL_MOVIE, mMovieDisplayed);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    /* Save the movie currently being displayed in the detail view.
    * @param movieTitle: movie which is currently being displayed in the detail view
     */
    public static void setMovieDisplayed(String movieTitle) {
        mMovieDisplayed = movieTitle;
    }

}