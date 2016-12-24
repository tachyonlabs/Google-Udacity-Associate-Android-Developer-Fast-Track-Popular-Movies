package com.tachyonlabs.popularmovies;

import com.tachyonlabs.popularmovies.PosterAdapter.PosterAdapterOnClickHandler;
import com.tachyonlabs.popularmovies.models.Movie;
import com.tachyonlabs.popularmovies.utiities.NetworkUtils;
import com.tachyonlabs.popularmovies.utiities.TmdbJsonUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements PosterAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";

    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;
    private TextView tvErrorMessageDisplay;
    private ProgressBar pbLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String sortOrder = getSharedPreferencesSettings();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);

        tvErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        // COMPLETED (40) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        //mRecyclerView.setHasFixedSize(true);
        mPosterAdapter = new PosterAdapter(this);
        mRecyclerView.setAdapter(mPosterAdapter);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        pbLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        loadPosters(sortOrder);
    }

    public String getSharedPreferencesSettings() {
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        return mSettings.getString("sortOrder", POPULAR);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", movie);
        intent.putExtra("posters_base_url", PosterAdapter.POSTERS_BASE_URL);
        intent.putExtra("poster_width", PosterAdapter.POSTER_WIDTH);
        startActivity(intent);
    }

    private void loadPosters(String sortOrder) {
        showPosters();

        //String location = SunshinePreferences.getPreferredWeatherLocation(this);
        //new FetchWeatherTask().execute(location);
        new FetchPostersTask().execute(sortOrder);
    }

    public class FetchPostersTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            String tmdbApiKey = getApiKey();
            String sortOrder = params[0];

            URL postersRequestUrl = NetworkUtils.buildUrl(sortOrder, tmdbApiKey);

            try {
                String jsonTmdbResponse = NetworkUtils
                        .getResponseFromHttpUrl(postersRequestUrl);

                Movie[] moviesFromJson = TmdbJsonUtils
                        .getPosterUrlsFromJson(MainActivity.this, jsonTmdbResponse);

                return moviesFromJson;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showPosters();
                mPosterAdapter.setPosterData(movies);
            } else {
                showErrorMessage("onPostExecute returned null");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the main_menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our main_menu layout to this main_menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the main_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // COMPLETED (46) Instead of setting the text to "", set the adapter to null before refreshing
            Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showPosters() {
        /* First, make sure the error is invisible */
        tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        // COMPLETED (44) Hide mRecyclerView, not mWeatherTextView
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        tvErrorMessageDisplay.setText(errorMessage);
        tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public String getApiKey() {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("TMDB_API_KEY");

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
     }
}
