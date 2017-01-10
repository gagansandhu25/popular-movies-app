package me.indiandollar.apps.popularmoviz.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import me.indiandollar.apps.popularmoviz.Adapters.MovieReviewAdapter;
import me.indiandollar.apps.popularmoviz.Utilities.Config;
import me.indiandollar.apps.popularmoviz.Database.Contracts.UserFavoritesContract;
import me.indiandollar.apps.popularmoviz.Database.Helpers.UserFavoritesDbHelper;
import me.indiandollar.apps.popularmoviz.Models.MovieReview;
import me.indiandollar.apps.popularmoviz.Models.Movie;
import me.indiandollar.apps.popularmoviz.Models.MovieVideo;
import me.indiandollar.apps.popularmoviz.R;
import me.indiandollar.apps.popularmoviz.Utilities.NetworkUtilities;
import me.indiandollar.apps.popularmoviz.Adapters.VideoListAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Indian Dollar on 1/4/2017.
 */

public class MovieDetailsFragment extends Fragment {

    private static final String TAG = "MovieDetailsFragment";
    private FragmentActivity mActivity;
    private Context mContext;
    private int mMovieId;
    private ArrayList<MovieReview> mReviewsList = new ArrayList<>();
    private TextView mReviewer;
    private TextView mReviewContent;
    private ListView mReviewsHolder;
    private View view;
    private String mBackdropPath;
    private ArrayList<MovieVideo> mVideosList =  new ArrayList<>();
    private ViewPager mTrailersHolder;
    private ImageButton mPlayButton;

    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMovieRating;
    private TextView mMoviePlot;
    private ImageView mMovieImage;
    private Movie mMovie;
    private UserFavoritesDbHelper mDbHelper;
    private SQLiteDatabase mDb;


    public MovieDetailsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_movie_view, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemSelected = item.getItemId();

        if(menuItemSelected == R.id.actionMarkFavorite) {

            addFavorites();

        } else if(menuItemSelected == R.id.actionShareTrailer) {
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");

            if(mVideosList.isEmpty()) return false;

            intent.putExtra(Intent.EXTRA_TEXT, "Check out this trailer! - http://www.youtube.com/watch?v=" + mVideosList.get(0).getVideoId());
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this trailer!");
            startActivity(Intent.createChooser(intent, "Share"));

            return true;

        } else if(menuItemSelected == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(mActivity);
            return true;
        }

        return true;

    }







    private void addFavorites() {
        mDbHelper = new UserFavoritesDbHelper(mContext);

        mDb = mDbHelper.getWritableDatabase();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                ContentValues values = new ContentValues();
                values.put(UserFavoritesContract.COL_MOVIE_ID, mMovie.getId());
                values.put(UserFavoritesContract.COL_MOVIE_TITLE, mMovie.getTitle());
                values.put(UserFavoritesContract.COL_MOVIE_RELEASE_DATE, mMovie.getRawReleaseDate());
                values.put(UserFavoritesContract.COL_MOVIE_PLOT, mMovie.getOverview());
                values.put(UserFavoritesContract.COL_MOVIE_RATING, mMovie.getVoteAverage());
                values.put(UserFavoritesContract.COL_MOVIE_POSTER, mMovie.getPosterPath());
                values.put(UserFavoritesContract.COL_MOVIE_BACKDROP, mMovie.getBackdropPath());

                final Uri uri = mActivity.getContentResolver().insert(UserFavoritesContract.PATH_MOVIES_URI, values);

                if(uri != null) {
                    FragmentActivity a = (FragmentActivity) mContext;
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Movie saved in favorites", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    FragmentActivity a = (FragmentActivity) mContext;
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Something went wrong, cannot be added to favorites", Toast.LENGTH_LONG).show();
                        }
                    });
                }


                return null;

            }
        }.execute();

    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mActivity = getActivity();
        mContext = getContext();

        mMovie = getArguments().getParcelable("movie");


        view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        View viewTemp = inflater.inflate(R.layout.movie_review_item, container, false);
        mReviewer = (TextView) viewTemp.findViewById(R.id.movie_review_user_name);
        mReviewContent = (TextView) view.findViewById(R.id.movie_review_content);
        mReviewsHolder = (ListView) view.findViewById(R.id.reviewsHolder);
        mPlayButton = (ImageButton) viewTemp.findViewById(R.id.movie_video_item_button);
        mTrailersHolder = (ViewPager) view.findViewById(R.id.trailersHolder);

        mMovieTitle = (TextView) view.findViewById(R.id.movieNameLabel);

        mMovieReleaseDate = (TextView) view.findViewById(R.id.movieDateLabel);
        mMovieRating = (TextView) view.findViewById(R.id.movieRatingLabel);
        mMovieImage = (ImageView) view.findViewById(R.id.movieImageView);
        mMoviePlot = (TextView) view.findViewById(R.id.moviePlotLabel);

        mMovieTitle.setText(mMovie.getOriginalTitle() + "");
        mMovieReleaseDate.setText(mMovie.getReleaseDate() + "");
        mMovieRating.setText(mMovie.getVoteAverage() + "");
        mMoviePlot.setText(mMovie.getOverview() + "");

        String size = getResources().getString(R.string.backdropSize);

        Picasso.with(mActivity).load("http://image.tmdb.org/t/p/" + size + "/" + mMovie.getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mMovieImage);

        mMovieId = getArguments().getInt("movieId");
        mBackdropPath = getArguments().getString("movieBackdropPath");


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey("trailers_list")) {
            mVideosList = savedInstanceState.getParcelableArrayList("trailers_list");
            mTrailersHolder.setAdapter(new VideoListAdapter(mContext, mVideosList));
        }
        else {
            NetworkUtilities network = new NetworkUtilities(mContext);
            network.setMovieId(mMovieId);

            URL url = network.buildUrl(Config.TMDB_MOVIE_REVIEW_PARAM);
            getReviews(url);

            url = network.buildUrl(Config.TMDB_MOVIE_TRAILER_PARAM);
            getTrailers(url);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.<MovieVideo>putParcelableArrayList("trailers_list", mVideosList);

    }

    // fetch trailers
    public void getTrailers(URL url) {

        OkHttpClient okhttp = new OkHttpClient();
        Request request = new Request.Builder().url(url.toString()).build();

        if(NetworkUtilities.isNetworkAvailable(mContext)) {

            Call call = okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Log.d(TAG, "onFailure: " + e);
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
                                    MovieDetailsFragment c = MovieDetailsFragment.this;
                                    c.parseTrailers(body);
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

            LinearLayout offlineTextView = (LinearLayout) view.findViewById(R.id.offlineTextView);

            offlineTextView.setVisibility(View.VISIBLE);

            Toast.makeText(mContext, "Network unavailable", Toast.LENGTH_LONG).show();
        }

    }




    public void parseTrailers(String body) throws JSONException {

        JSONObject forecast = new JSONObject(body);
        JSONArray videos = forecast.getJSONArray("results");

        for (int i = 0; i < videos.length(); i++) {

            JSONObject o1 = videos.getJSONObject(i);
            MovieVideo movie = new MovieVideo();
            movie.setId(o1.getString("id"));
            movie.setMovieId(mMovieId);
            movie.setVideoThumbnail(mBackdropPath);
            movie.setVideoId(o1.getString("key"));

            mVideosList.add(i, movie);
        }

        VideoListAdapter arrayAdapter = new VideoListAdapter(mActivity, mVideosList);
        mTrailersHolder.setAdapter(arrayAdapter);

    }







    // fetch reviews
    private void getReviews(URL url) {

        OkHttpClient okhttp = new OkHttpClient();
        Request request = new Request.Builder().url(url.toString()).build();

        if(NetworkUtilities.isNetworkAvailable(mContext)) {

            Call call = okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Log.d(TAG, "onFailure: " + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String body = response.body().string();

                    if(response.isSuccessful()) {
                        FragmentActivity a = (FragmentActivity) mContext;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MovieDetailsFragment c = MovieDetailsFragment.this;
                                try {
                                    c.parseReviews(body);
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

            LinearLayout offlineTextView = (LinearLayout) view.findViewById(R.id.offlineTextView1);

            offlineTextView.setVisibility(View.VISIBLE);

            FragmentActivity a = (FragmentActivity) mContext;
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Network unavailable", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void parseReviews(String body) throws JSONException {

        JSONObject forecast = new JSONObject(body);
        JSONArray videos = forecast.getJSONArray("results");

        for (int i = 0; i < videos.length(); i++) {

            JSONObject o1 = videos.getJSONObject(i);
            MovieReview review = new MovieReview();
            review.setId(o1.getString("id"));
            review.setMovieId(mMovieId);
            review.setReviewer(o1.getString("author"));
            review.setContent(o1.getString("content"));

            mReviewsList.add(i, review);
        }

        MovieReviewAdapter arrayAdapter = new MovieReviewAdapter(mActivity, mReviewsList);
        mReviewsHolder.setAdapter(arrayAdapter);

    }

}
