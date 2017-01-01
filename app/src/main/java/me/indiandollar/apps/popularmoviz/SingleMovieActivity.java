package me.indiandollar.apps.popularmoviz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie);

        mMovie = (Movie) getIntent().getParcelableExtra("movie");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mMovie.getTitle());


        TextView mMovieTitle = (TextView) findViewById(R.id.movieNameLabel);
        TextView mMovieReleaseDate = (TextView) findViewById(R.id.movieDateLabel);
        TextView mMovieRating = (TextView) findViewById(R.id.movieRatingLabel);
        ImageView mMovieImage = (ImageView) findViewById(R.id.movieImageView);
        TextView mMoviePlot = (TextView) findViewById(R.id.moviePlotLabel);

        mMovieTitle.setText(mMovie.getOriginalTitle() + "");
        mMovieReleaseDate.setText(mMovie.getReleaseDate() + "");
        mMovieRating.setText(mMovie.getVoteAverage() + "");
        mMoviePlot.setText(mMovie.getOverview() + "");

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + mMovie.getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mMovieImage);

        //Log.d(TAG, "onCreate: " + movie.getTitle());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();

        if(itemid == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return true;
    }
}
