package com.movies.popular.albert.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movies.popular.albert.popularmovies.networkutils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.movies.popular.albert.popularmovies.networkutils.NetworkUtils.getResponseFromHttpURL;

/**
 * Created by Alberto Casas Ortiz.
 */
public class MainActivity extends AppCompatActivity implements GridAdapter.ItemListenerClick{
    //Arrays to save movie datas and posters path.
    public ArrayList<String> posters;
    public ArrayList<JSONObject> movies;

    ///Number of items in the recyclerView.
    public int numItems;
    //Sorted popular(true) or top rated (false)
    private boolean sortPopular;
    //Number of columns in the gridView.
    private static final int NUM_COLUMNS = 3;

    //Adapter of the RecyclerView.
    private GridAdapter adapter;
    //RecyclerView
    private RecyclerView recyclerView;

    //Error message if connection fails.
    private TextView mErrorConnection;
    //ProgressBar to show during connection time.
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorConnection = (TextView) findViewById(R.id.tv_error_connection);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        posters = new ArrayList<String>();
        movies = new ArrayList<JSONObject>();

        sortPopular = true;

        //Load data
        new MovieLoaderAsyncTask().execute(NetworkUtils.popularMovieURL(sortPopular));

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layout = new GridLayoutManager(MainActivity.this, NUM_COLUMNS);
        recyclerView.setLayoutManager(layout);

        recyclerView.setHasFixedSize(true);

        adapter = new GridAdapter(MainActivity.this, numItems, posters, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        boolean res = false;
        switch (itemClicked){
            case R.id.action_sort_popular:
                sortPopular = true;
                new MovieLoaderAsyncTask().execute(NetworkUtils.popularMovieURL(sortPopular));
                res = true;
                break;
            case R.id.action_sort_top_rated:
                sortPopular = false;
                new MovieLoaderAsyncTask().execute(NetworkUtils.popularMovieURL(sortPopular));
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        //Click an image lead us to the movie detail activity.
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movies.get(clickedItemIndex).toString());
        startActivity(intent);
    }

    /**
     * With the string json returned by the http connection, get he data and store it in the arrays.
     * @param json JSON from the http connection.
     */
    public void extractJSONData(String json){
        try {
            posters.clear();
            movies.clear();
            JSONObject object = new JSONObject(json);
            JSONArray arrayMovies = object.getJSONArray("results");
            for(int i = 0; i < arrayMovies.length(); i++){
                JSONObject movie = arrayMovies.getJSONObject(i);
                movies.add(movie);
                posters.add(movie.getString("poster_path"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the data in the GUI.
     */
    public void refresh(){
        numItems = movies.size();

        adapter = new GridAdapter(MainActivity.this, numItems, posters, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Show posters and not show error.
     */
    public void showPosters() {
        mErrorConnection.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show error and not show posters.
     */
    public void showErrorConnection(){
        mErrorConnection.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Async task to make the http connection.
     */
    public class MovieLoaderAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            String response = "";
            try {
                response = getResponseFromHttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if(s != null && s != ""){
                showPosters();
                extractJSONData(s);
                //Refresh with extracted data.
                refresh();
            }else{
                showErrorConnection();
            }
        }
    }





}
