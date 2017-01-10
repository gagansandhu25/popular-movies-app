package me.indiandollar.apps.popularmoviz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.indiandollar.apps.popularmoviz.Models.Movie;
import me.indiandollar.apps.popularmoviz.R;

/**
 * Created by Indian Dollar on 12/31/2016.
 */

public class GridViewAdapter extends BaseAdapter {

    private static final String TAG = "GridViewAdapter";
    private ArrayList<Movie> mMovies;
    private Context mContext;


    private LayoutInflater inflater;

    public GridViewAdapter(Context c, ArrayList<Movie> movies) {

        mContext = c;
        inflater = LayoutInflater.from(mContext);
        mMovies = movies;

    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        TextView textView;
        View inflater1;

        if (convertView == null) {

            inflater1 = (View) inflater.inflate(R.layout.movie_grid_item, null);
            textView = (TextView) inflater1.findViewById(R.id.movie_grid_item_title);
            imageView = (ImageView) inflater1.findViewById(R.id.movie_grid_item_image);

        }
        else {
            inflater1 = convertView;
            imageView = (ImageView) inflater1.findViewById(R.id.movie_grid_item_image);
            textView = (TextView) inflater1.findViewById(R.id.movie_grid_item_title);
        }

        textView.setText(mMovies.get(position).getTitle());

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + mMovies.get(position).getPosterPath())
                .placeholder(R.raw.ring)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

        return inflater1;
    }
}
