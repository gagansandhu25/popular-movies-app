package me.indiandollar.apps.popularmoviz.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.indiandollar.apps.popularmoviz.Utilities.Config;
import me.indiandollar.apps.popularmoviz.Database.Contracts.UserFavoritesContract;
import me.indiandollar.apps.popularmoviz.Database.Helpers.UserFavoritesDbHelper;
import me.indiandollar.apps.popularmoviz.Adapters.GridViewAdapter;
import me.indiandollar.apps.popularmoviz.Models.Movie;
import me.indiandollar.apps.popularmoviz.R;
import me.indiandollar.apps.popularmoviz.Activities.SingleMovieActivity;
import me.indiandollar.apps.popularmoviz.Utilities.NetworkUtilities;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridViewFragment extends Fragment {

    private static final String TAG = "MainActivity";
    private ArrayList<Movie> mMovies = new ArrayList<Movie>();
    private Activity mActivity;
    private Context mContext;
    private UserFavoritesDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private GridView mGridView;
    private ProgressBar mProgressBar;


    public GridViewFragment() {
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mActivity = getActivity();
        mContext = (Context)mActivity;


        View view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        initControls(view);


        // set 3 columns in grid in landscape mode
        if(getResources().getString(R.string.orientation).equals("landscape")) {
            mGridView.setNumColumns(3);
        }

        toggleLoader("show");

        if(savedInstanceState != null && savedInstanceState.containsKey("movies_list")) {

            mMovies = savedInstanceState.<Movie>getParcelableArrayList("movies_list");
            mGridView.setAdapter(new GridViewAdapter(mContext, mMovies));

            toggleLoader("hide");
            gridViewItemClicked();

        }
        else {

            NetworkUtilities network = new NetworkUtilities(mContext);
            URL url = network.buildUrl(Config.TMDB_POPULAR_PARAM);
            makeRequest(url);

            ((AppCompatActivity)mActivity).getSupportActionBar()
                    .setTitle(getString(R.string.app_name) + getString(R.string.most_popular_text));

        }

        return view;

    }

    private void initControls(View view) {

        mGridView = (GridView) view.findViewById(R.id.gridview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

    }


    @Override
    public void onStart() {

        super.onStart();

    }





    private void gridViewItemClicked() {

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!getResources().getBoolean(R.bool.multiPane)) {

                    Intent intent = new Intent(mActivity, SingleMovieActivity.class);
                    intent.putExtra("movie", mMovies.get(position));
                    startActivity(intent);

                }
                else {

                    Bundle bundle = new Bundle();
                    bundle.putInt("movieId", mMovies.get(position).getId());
                    bundle.putString("movieBackdropPath", mMovies.get(position).getBackdropPath());
                    bundle.putParcelable("movie", mMovies.get(position));

                    MovieDetailsFragment fragment = new MovieDetailsFragment();
                    fragment.setArguments(bundle);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.movieDetailsContainer, fragment)
                            .commit();

                }

            }
        });

    }


    public void toggleLoader(String option) {

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
    public void onSaveInstanceState(Bundle outState) {

        outState.<Movie>putParcelableArrayList("movies_list", mMovies);
        super.onSaveInstanceState(outState);

    }






    // parse movies from json response
    public void parseMovies(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        JSONArray currently = forecast.getJSONArray("results");

        for (int i = 0; i < currently.length(); i++) {

            JSONObject o1 = currently.getJSONObject(i);
            Movie movie = new Movie();
            movie.setTitle(o1.getString("title"));
            movie.setOriginalTitle(o1.getString("original_title"));
            movie.setRawReleaseDate(o1.getString("release_date"));
            movie.setReleaseDate(o1.getString("release_date"));
            movie.setOverview(o1.getString("overview"));
            movie.setVoteAverage(o1.getDouble("vote_average"));
            movie.setPosterPath(o1.getString("poster_path"));
            movie.setBackdropPath(o1.getString("backdrop_path"));
            movie.setId(o1.getInt("id"));

            mMovies.add(i, movie);
        }

        toggleLoader("hide");

        mGridView.setAdapter(new GridViewAdapter(mActivity, mMovies));
        gridViewItemClicked();


    }




    public void getFavorites() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Cursor cursor = null;

                try {

                    cursor = getContext().getContentResolver().query(
                            UserFavoritesContract.PATH_MOVIES_URI,
                            null,
                            null,
                            null,
                            UserFavoritesContract._ID + " DESC"
                    );
                }
                catch (Exception e) {
                    Log.e(TAG, "doInBackground: ", e);
                }


                mMovies = new ArrayList<>();
                while(cursor.moveToNext()) {
                    Movie movie = new Movie();
                    //movie.setId(cursor.getInt(0));
                    movie.setId(cursor.getInt(1));
                    movie.setOriginalTitle(cursor.getString(2));
                    movie.setTitle(cursor.getString(2));
                    movie.setVoteAverage(cursor.getDouble(3));
                    movie.setRawReleaseDate(cursor.getString(4));
                    movie.setPosterPath(cursor.getString(5));
                    movie.setOverview(cursor.getString(6));
                    movie.setBackdropPath(cursor.getString(7));

                    mMovies.add(movie);
                }

                Log.d(TAG, "doInBackground: " + mMovies);

                cursor.close();

                FragmentActivity a = (FragmentActivity) mContext;
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFavoritesAdapter();
                    }
                });


                return null;

            }
        }.execute();


    }

    private void setFavoritesAdapter() {

        toggleLoader("hide");
        mGridView.setAdapter(new GridViewAdapter(mActivity, mMovies));
        gridViewItemClicked();

    }




    // network request

    public void makeRequest(URL url) {

        OkHttpClient okhttp = new OkHttpClient();
        Request request = new Request.Builder().url(url.toString()).build();

        if(NetworkUtilities.isNetworkAvailable(mContext)) {

            Call call = okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String body = response.body().string();

                    if(response.isSuccessful()) {
                        FragmentActivity a = (FragmentActivity) mContext;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GridViewFragment c = GridViewFragment.this;
                                    c.parseMovies(body);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else {

                    }

                }
            });

        }
        else {
            FragmentActivity a = (FragmentActivity) mContext;
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText((FragmentActivity) mContext, "Network unavailable", Toast.LENGTH_LONG).show();
                }
            });
        }

    }






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if(menuItemSelected == R.id.action_show_popular) {

            NetworkUtilities network = new NetworkUtilities(mContext);
            URL url = network.buildUrl(Config.TMDB_POPULAR_PARAM);
            makeRequest(url);

            toggleLoader("show");

            ((AppCompatActivity)mActivity)
                    .getSupportActionBar()
                    .setTitle(getString(R.string.app_name) + getString(R.string.most_popular_text));

            return true;

        }
        else if (menuItemSelected == R.id.action_show_top_rated) {

            NetworkUtilities network = new NetworkUtilities(mContext);
            URL url = network.buildUrl(Config.TMDB_TOP_RATED_PARAM);
            makeRequest(url);

            toggleLoader("show");

            ((AppCompatActivity)mActivity)
                    .getSupportActionBar()
                    .setTitle(getString(R.string.app_name) + getString(R.string.top_rated_text));

            return true;

        }
        else if(menuItemSelected == R.id.action_show_favorites) {

            getFavorites();

            toggleLoader("show");

            ((AppCompatActivity)mActivity).getSupportActionBar().setTitle(getString(R.string.app_name) + getString(R.string.favorites_text));

            return true;

        }

        return false;


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDbHelper != null) {
            mDbHelper.close();
        }
    }
}
