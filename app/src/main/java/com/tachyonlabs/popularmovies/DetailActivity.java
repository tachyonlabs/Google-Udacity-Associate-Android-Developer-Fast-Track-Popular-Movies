package com.tachyonlabs.popularmovies;

import com.squareup.picasso.Picasso;
import com.tachyonlabs.popularmovies.models.Movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        ImageView ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        TextView tvOverview = (TextView) findViewById(R.id.tv_overview);

        Intent callingIntent = getIntent();

        if (callingIntent.hasExtra("movie")) {
            Movie movie = (Movie) callingIntent.getSerializableExtra("movie");
            String postersBaseUrl = callingIntent.getStringExtra("posters_base_url");
            String posterWidth = callingIntent.getStringExtra("poster_width");

            tvTitle.setText(movie.getTitle());
            Picasso.with(this).load(postersBaseUrl + posterWidth + movie.getPosterUrl()).into(ivThumbnail);
            tvOverview.setText(movie.getOverview());
            Log.d(TAG, movie.getOverview());
        }
    }
}
