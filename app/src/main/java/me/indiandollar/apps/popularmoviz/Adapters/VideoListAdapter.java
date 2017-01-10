package me.indiandollar.apps.popularmoviz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.indiandollar.apps.popularmoviz.Models.MovieVideo;
import me.indiandollar.apps.popularmoviz.R;

/**
 * Created by Indian Dollar on 12/31/2016.
 */

public class VideoListAdapter extends PagerAdapter {


    private static final String TAG = "VideoListAdapter";
    private final Context mContext;
    private final ArrayList<MovieVideo> mVideos;



    public VideoListAdapter(Context c, ArrayList<MovieVideo> videos) {
        mContext = c;
        mVideos = videos;
        //Log.d(TAG, "VideoListAdapter: " + mVideos);

    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (FrameLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item_view = layoutInflater.inflate(R.layout.movie_video_item, container, false);
        //ViewPager viewPager = (ViewPager) item_view.findViewById(R.id.trailersHolder);
        //TextView textView = (TextView) item_view.findViewById(R.id.movie_video_item_label);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.movie_video_item_image);
        ImageButton imageButton = (ImageButton) item_view.findViewById(R.id.movie_video_item_button);

        /*ViewGroup item_view1 = (ViewGroup) layoutInflater.inflate(R.layout.fragment_movie_trailers, container, false);
        View item_view11 = item_view1.findViewById(R.id.movietrailersFrame);
        final TextView offlineTextView = (TextView) item_view11.findViewById(R.id.offlineTextView);

        Log.d(TAG, "instantiateItem: " + offlineTextView);

        offlineTextView.setText("called");
        offlineTextView.setVisibility(View.VISIBLE);*/

        //textView.setText(mVideos.get(position).getId());

        String size = mContext.getResources().getString(R.string.backdropSize);

        Log.d(TAG, "instantiateItem: " + size);


        Picasso.with(mContext).load("http://image.tmdb.org/t/p/"+size+"/" + mVideos.get(position).getVideoThumbnail())
                .placeholder(R.raw.ring)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideos.get(position).getVideoId()));
                mContext.startActivity(intent);
            }
        });

        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout)object);
    }
}
