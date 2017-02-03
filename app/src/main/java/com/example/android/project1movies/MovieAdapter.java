package com.example.android.project1movies;

/**
 * Created by Doug on 1/30/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> implements View.OnClickListener {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> mMovies;

    /*
    * An on-click handler that we've defined to make it easy for an Activity to interface with
    * our RecyclerView
    */
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies A List of Movie objects to display in a list
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */

    public MovieAdapter(Activity context, List<Movie> movies, MovieAdapterOnClickHandler clickHandler) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

        super(context, 0, movies);
        mClickHandler = clickHandler;
    }

    /* Uses Picasso to load an ImageView using Movie poster path.
    *   @param context The context for this imageView
    *   @param Movie   The Movie object which will have its poster loaded.
    *   @param ImageView The ImageView being loaded.
     */
    private void setMovieImage(Context context, Movie movie, ImageView imageView) {
        String base_url = "http://image.tmdb.org/t/p/";
        String size = "w185";
        String url = base_url + size + "/" + movie.posterPath;
        Picasso.with(context).load(url).into(imageView);
    }


    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);
        Context context = parent.getContext();

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);

        setMovieImage(context, movie, iconView);

        iconView.setOnClickListener(this);

        /* Store the position for this Movie object in the ContentDescription String of the ImageView.
        * This is used later to quickly retrieve the Movie object in the click handler.
         */
        iconView.setContentDescription(String.valueOf(position));

        // Some movie posters do not display the movie name. However most do. So
        // not adding the title for now.
        // TextView movieTitle = (TextView) convertView.findViewById(R.id.movie_title);
        // movieTitle.setText(movie.title);

        return convertView;
    }

    /* Handle a click from an ImageView for one of our movies
    * @param v The ImageView which has been clicked on.
     */
    @Override
    public void onClick(View v) {
        /* retrieve the position of the Movie in the list of Movies */
        /* This was previously stored in the ContentDescription of the ImageView */
        String adapterPositionString = v.getContentDescription().toString();

        int adapterPosition = Integer.parseInt(adapterPositionString);

        Movie movie = getItem(adapterPosition);

        mClickHandler.onClick(movie);
    }
}