package me.indiandollar.apps.popularmoviz.Database.Contracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Indian Dollar on 1/6/2017.
 */

public class UserFavoritesContract implements BaseColumns {

    public static final String TABLE_NAME = "user_favorite_movies";
    public static final String COL_MOVIE_ID = "movieId";
    public static final String COL_MOVIE_TITLE = "title";
    public static final String COL_MOVIE_RELEASE_DATE = "releaseDate";
    public static final String COL_MOVIE_RATING = "averageRating";
    public static final String COL_MOVIE_PLOT = "plot";

    public static final String COL_MOVIE_POSTER = "posterPath";
    public static final String COL_MOVIE_BACKDROP = "backdropPath";

    public static final String SCHEMA = "content://";
    public static final String AUTHORITY = "me.indiandollar.apps.popularmoviz";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEMA + AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final Uri PATH_MOVIES_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
}
