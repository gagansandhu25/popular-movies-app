package me.indiandollar.apps.popularmoviz.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.indiandollar.apps.popularmoviz.Models.MovieReview;
import me.indiandollar.apps.popularmoviz.R;

/**
 * Created by Indian Dollar on 12/31/2016.
 */

public class MovieReviewAdapter extends BaseAdapter {

    private static final String TAG = "MovieReviewAdapter";
    private ArrayList<MovieReview> mReviews;
    private Context mContext;


    private LayoutInflater inflater;

    public MovieReviewAdapter(Context c, ArrayList<MovieReview> reviews) {

        mContext = c;
        inflater = LayoutInflater.from(mContext);
        mReviews = reviews;

    }

    @Override
    public int getCount() {
        return mReviews.size();
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
        TextView textView2;

        if (convertView == null) {

            inflater1 = inflater.inflate(R.layout.movie_review_item, null);
            textView = (TextView) inflater1.findViewById(R.id.movie_review_user_name);

            textView2 = (TextView) inflater1.findViewById(R.id.movie_review_content);

        }
        else {
            inflater1 = convertView;
            textView = (TextView) inflater1.findViewById(R.id.movie_review_user_name);
            textView2 = (TextView) inflater1.findViewById(R.id.movie_review_content);
        }

        textView.setText(mReviews.get(position).getReviewer());
        textView2.setText(mReviews.get(position).getContent());


        return inflater1;
    }
}
