package com.example.android.project1movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.android.project1movies.MainActivityFragment.NO_MOVIE_DISPLAYED;

/**
 * Created by Doug on 2/1/2017.
 */


public class MovieDetailActivity  extends AppCompatActivity {
    /* views in the movie detail display */
    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /* get the views */
        mTitleTextView = (TextView) findViewById(R.id.movie_title);
        mSynopsisTextView = (TextView) findViewById(R.id.movie_synopsis);
        mReleaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
        mUserRatingTextView = (TextView) findViewById(R.id.movie_user_rating);
        mPosterImageView = (ImageView) findViewById(R.id.movie_poster);

        Intent intentThatStartedThisActivity = getIntent();

        /* Set all the views with values pulled from the intent */
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Movie.TITLE)) {
                String text = intentThatStartedThisActivity.getStringExtra(Movie.TITLE);
                mTitleTextView.setText(text);
            }

            if (intentThatStartedThisActivity.hasExtra(Movie.SYNOPSIS)) {
                String text = intentThatStartedThisActivity.getStringExtra(Movie.SYNOPSIS);
                mSynopsisTextView.setText(text);
            }

            if (intentThatStartedThisActivity.hasExtra(Movie.RELEASE_DATE)) {
                String text = intentThatStartedThisActivity.getStringExtra(Movie.RELEASE_DATE);
                mReleaseDateTextView.setText(text);
            }

            if (intentThatStartedThisActivity.hasExtra(Movie.USER_RATING)) {
                double rating = intentThatStartedThisActivity.getDoubleExtra(Movie.USER_RATING, 0.0);
                mUserRatingTextView.setText(" User Rating: " + rating + " ");
            }

            if (intentThatStartedThisActivity.hasExtra(Movie.POSTER_PATH)) {
                String posterPath = intentThatStartedThisActivity.getStringExtra(Movie.POSTER_PATH);
                setMovieImage(getApplicationContext(), posterPath, mPosterImageView);
            }
        }
    }

    /* load up the image view using the poster path passed in using Picasso */
    private void setMovieImage(Context context, String posterPath, ImageView imageView) {
        String base_url = "http://image.tmdb.org/t/p/";
        String size = "w185";
        String url = base_url + size + "/" + posterPath;
        Picasso.with(context).load(url).into(imageView);
    }

    /* if the back arrow is pressed at the top of the dialog then the onCreate will be called
    * for the MainApplicationFragment. This will result in an attempt to reload the db. And this
    *  will bring back up this dialog if the mMovieDisplayed is not cleared.
    *  Back button at the bottom of the phone will not do that.
     */
    @Override
    protected void onPause() {
        MainActivityFragment.setMovieDisplayed(NO_MOVIE_DISPLAYED);
        super.onPause();
    }
}