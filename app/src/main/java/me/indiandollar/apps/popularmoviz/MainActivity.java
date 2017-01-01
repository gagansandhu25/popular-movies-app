package me.indiandollar.apps.popularmoviz;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import me.indiandollar.apps.popularmoviz.Utilities.NetworkUtilities;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<Movie> mGridData = new ArrayList<Movie>();
    private ProgressBar mProgressBar;
    private GridView mGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        toggleLoader("show");

        if(savedInstanceState != null && savedInstanceState.containsKey("movies_list")) {
            mGridData = savedInstanceState.<Movie>getParcelableArrayList("movies_list");
            mGridView.setAdapter(new GridViewAdapter(this, mGridData));

            toggleLoader("hide");

            mGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SingleMovieActivity.class);
                    //Log.d(TAG, "onItemClick: " + mGridData);
                    intent.putExtra("movie", (Parcelable) mGridData.get(position));
                    startActivity(intent);
                }
            });
        }
        else {
            NetworkUtilities network = new NetworkUtilities(this, this);

            URL url = network.buildUrl(Config.TMDB_POPULAR_PARAM);

            //Log.d(TAG, "onCreate: " + url);
            network.makeRequest(url);
            getSupportActionBar().setTitle(getString(R.string.app_name) + getString(R.string.most_popular_text));
        }


    }


    private void toggleLoader(String option) {

        if(option == "show") {
            mProgressBar.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.INVISIBLE);
        }
        else if(option == "hide") {
            mProgressBar.setVisibility(View.INVISIBLE);
            mGridView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.<Movie>putParcelableArrayList("movies_list", mGridData);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if(menuItemSelected == R.id.action_show_popular) {
            NetworkUtilities network = new NetworkUtilities(this, this);

            toggleLoader("show");

            URL url = network.buildUrl(Config.TMDB_POPULAR_PARAM);

            network.makeRequest(url);

            getSupportActionBar().setTitle(getString(R.string.app_name) + getString(R.string.most_popular_text));
        }
        else if (menuItemSelected == R.id.action_show_top_rated) {
            NetworkUtilities network = new NetworkUtilities(this, this);

            toggleLoader("show");

            URL url = network.buildUrl(Config.TMDB_TOP_RATED_PARAM);
            network.makeRequest(url);

            getSupportActionBar().setTitle(getString(R.string.app_name) + getString(R.string.top_rated_text));
        }

        return true;
    }

    public void getMovie(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);

        JSONArray currently = forecast.getJSONArray("results");


        for (int i = 0; i < currently.length(); i++) {

            JSONObject o1 = currently.getJSONObject(i);
            Movie movie = new Movie();
            movie.setTitle(o1.getString("title"));
            movie.setOriginalTitle(o1.getString("original_title"));
            movie.setReleaseDate(o1.getString("release_date"));
            movie.setOverview(o1.getString("overview"));
            movie.setVoteAverage(o1.getDouble("vote_average"));
            movie.setPosterPath(o1.getString("poster_path"));

            mGridData.add(i, movie);
        }

        //Log.d(TAG, "getMovie1: " + mGridData);

        mGridView.setAdapter(new GridViewAdapter(this, mGridData));

        toggleLoader("hide");

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SingleMovieActivity.class);
                //Log.d(TAG, "onItemClick: " + mGridData);
                intent.putExtra("movie", (Parcelable) mGridData.get(position));
                startActivity(intent);
            }
        });

        //Log.i(TAG, movie.toString());

    }

}
