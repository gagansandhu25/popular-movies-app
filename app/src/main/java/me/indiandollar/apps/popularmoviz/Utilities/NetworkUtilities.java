package me.indiandollar.apps.popularmoviz.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Indian Dollar on 12/31/2016.
 */

public class NetworkUtilities {

    final static String TMDB_BASE_URL = Config.TMDB_BASE_URL;
    final static String TMDB_API_KEY = Config.TMDB_API_KEY;
    final static String TMDB_PARAM_API_KEY = Config.TMDB_PARAM_API_KEY;
    private static final String TMDB_TOP_RATED_MOVIES_PARAMS = Config.TMDB_TOP_RATED_MOVIES_PARAMS;
    final static String TMDB_POPULAR_MOVIES_PARAMS = Config.TMDB_POPULAR_MOVIES_PARAMS;

    private static final String TAG = "NetworkUtilities";

    private Context mContext;
    private int mMovieId;

    public NetworkUtilities(Context context) {
        mContext = context;
    }
    
    public void setMovieId(int id) {

        mMovieId = id;

    }

    public URL buildUrl(String t) {

        URL url = null;

        String u = TMDB_BASE_URL;

        if(t.contentEquals(Config.TMDB_POPULAR_PARAM)) {
            u += TMDB_POPULAR_MOVIES_PARAMS;
        }
        else if(t.contentEquals(Config.TMDB_TOP_RATED_PARAM)) {
            u += TMDB_TOP_RATED_MOVIES_PARAMS;
        }
        else if(t.contentEquals(Config.TMDB_MOVIE_TRAILER_PARAM)) {
            u += "movie/" + mMovieId + "/" + Config.TMDB_MOVIE_TRAILERS_ROUTE;
        }
        else if(t.contentEquals(Config.TMDB_MOVIE_REVIEW_PARAM)) {
            u += "movie/" + mMovieId + "/" + Config.TMDB_MOVIE_REVIEWS_ROUTE;
        }

        Uri uri = Uri.parse(u).buildUpon()
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }





    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if(info != null && info.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;

    }


}
