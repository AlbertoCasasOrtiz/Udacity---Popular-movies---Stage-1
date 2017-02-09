package com.movies.popular.albert.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.movies.popular.albert.popularmovies.networkutils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alberto Casas Ortiz.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvVoteAverage;
    private TextView tvPlotSynopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.ivPoster = (ImageView) findViewById(R.id.movie_poster);
        this.tvTitle = (TextView) findViewById(R.id.movie_title);
        this.tvReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        this.tvVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
        this.tvPlotSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis);

        Intent intentThatStartedThisActivity = getIntent();

        // If we reveive de data, set it in the GUI.
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            String movieJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            String poster_path = null;
            String title = null;
            String release_date = null;
            String vote_average = null;
            String overview = null;
            try {
                JSONObject movieJSON = new JSONObject(movieJSONString);
                poster_path = movieJSON.getString("poster_path");
                title = movieJSON.getString("title");
                release_date = movieJSON.getString("release_date");
                vote_average = movieJSON.getString("vote_average");
                overview = movieJSON.getString("overview");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Picasso.with(this.ivPoster.getContext()).load(NetworkUtils.imageURL(poster_path).toString()).into(this.ivPoster);
            this.tvTitle.setText("Title: " + title);
            this.tvReleaseDate.setText("Release date: " + release_date);
            this.tvVoteAverage.setText("Vote Average: " + vote_average);
            this.tvPlotSynopsis.setText("Plot: " + overview);
        }

    }
}
