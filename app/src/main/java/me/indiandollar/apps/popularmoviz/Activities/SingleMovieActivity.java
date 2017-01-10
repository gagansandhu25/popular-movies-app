package me.indiandollar.apps.popularmoviz.Activities;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;

import me.indiandollar.apps.popularmoviz.Database.Helpers.UserFavoritesDbHelper;
import me.indiandollar.apps.popularmoviz.Fragments.MovieDetailsFragment;
import me.indiandollar.apps.popularmoviz.Models.Movie;
import me.indiandollar.apps.popularmoviz.R;

/**
 * Created by Indian Dollar on 12/31/2016.
 */
public class SingleMovieActivity extends AppCompatActivity {


    /*@BindView(R.id.movieNameLabel) TextView mMovieTitle;
    @BindView(R.id.movieDateLabel) TextView mMovieReleaseDate;
    @BindView(R.id.movieRatingLabel) TextView mMovieRating;
    @BindView(R.id.movieImageView) ImageView mMovieImage;
    @BindView(R.id.moviePlotLabel) TextView mMoviePlot;*/

    private static final String TAG = "SingleMovieActivity";
    private Movie mMovie;

    UserFavoritesDbHelper mDbHelper;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
        //actionBar.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mMovie = getIntent().getParcelableExtra("movie");

        Bundle bundle = new Bundle();
        bundle.putInt("movieId", mMovie.getId());
        bundle.putString("movieBackdropPath", mMovie.getBackdropPath());
        bundle.putParcelable("movie", mMovie);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movieDetailsContainer, fragment)
                .commit();

        /*MovieReviewsFragment fragment1 = new MovieReviewsFragment();
        fragment1.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movieReviewsContainer, fragment1)
                .commit(); */


        //network.getMovieTrailers(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
