package me.indiandollar.apps.popularmoviz.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import me.indiandollar.apps.popularmoviz.Config;
import me.indiandollar.apps.popularmoviz.MainActivity;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

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
    private MainActivity mActivity;

    public NetworkUtilities(Context context, MainActivity activity) {
        mContext = context;
        mActivity = activity;
    }

    public static URL buildUrl(String t) {

        URL url = null;

        String u = TMDB_BASE_URL;

        Log.d(TAG, "buildUrl: " + t.contentEquals(Config.TMDB_TOP_RATED_PARAM));

        if(t.contentEquals(Config.TMDB_POPULAR_PARAM)) {
            u += TMDB_POPULAR_MOVIES_PARAMS;
        }
        else if(t.contentEquals(Config.TMDB_TOP_RATED_PARAM)) {
            u += TMDB_TOP_RATED_MOVIES_PARAMS;
        }

        Uri uri = Uri.parse(u).buildUpon()
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        Log.d(TAG, "URL is: " + uri.toString());

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Log.d(TAG, "buildUrl: " + url);

        return url;

    }


    public void makeRequest(URL url) {

        OkHttpClient okhttp = new OkHttpClient();
        Request request = new Request.Builder().url(url.toString()).build();

        if(isNetworkAvailable()) {

            Call call = okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Log.d(TAG, "onFailure: " + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {


                    final String jsonData = response.body().string();
                    //Log.d(TAG, "onResponse: " + jsonData);
                    if(response.isSuccessful()) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mActivity.getMovie(jsonData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else {
                        Log.d(TAG, "onResponse: error");
                    }

                }
            });

        }
        else {
            Log.d(TAG, "network not available");
        }

        //Log.d(TAG, "makeRequest: " + jsonData);

        //return jsonData;
    }


    public boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if(info != null && info.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;

    }


}
