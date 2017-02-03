package com.example.android.project1movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

 /* Main activity class */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*  I attempted to get an instance of my fragment application with the following code.
        *   I was not successful. I finally just stored a static variable to the instance in the
        *   class. This is definitely a Kludge and I will try to figure out how to make this work
        *   properly in the near future. Any suggestions would be appreciated.
         */

        // FragmentManager fm = getSupportFragmentManager();
        // MainActivityFragment mainActivityFragment = (MainActivityFragment) fm.findFragmentByTag("MainActivityFragment");

        /*
        *   Set the sort type for the query to the movie db.
         */

        if (MainActivityFragment.mMainActivityFragment != null) {
            if (id == R.id.action_most_popular) {
               MainActivityFragment.mMainActivityFragment.setMovieSortType(MainActivityFragment.MOVIE_DB_SORT_POPULAR);
                return true;
            } else if (id == R.id.action_highly_rated) {
                MainActivityFragment.mMainActivityFragment.setMovieSortType(MainActivityFragment.MOVIE_DB_SORT_TOP_RATED);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}