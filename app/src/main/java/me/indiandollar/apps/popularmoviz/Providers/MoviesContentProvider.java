package me.indiandollar.apps.popularmoviz.Providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.sql.SQLData;

import me.indiandollar.apps.popularmoviz.Database.Contracts.UserFavoritesContract;
import me.indiandollar.apps.popularmoviz.Database.Helpers.UserFavoritesDbHelper;

import static android.R.attr.id;

/**
 * Created by Indian Dollar on 1/10/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    private UserFavoritesDbHelper mMovieDbHelper;
    public static final int MOVIES_CODE = 100;
    public static final int MOVIE_SINGLE_CODE = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(UserFavoritesContract.AUTHORITY, UserFavoritesContract.PATH_MOVIES, MOVIES_CODE);
        uriMatcher.addURI(UserFavoritesContract.AUTHORITY, UserFavoritesContract.PATH_MOVIES + "/#", MOVIE_SINGLE_CODE);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new UserFavoritesDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor = null;

        switch (match) {
            case MOVIES_CODE:

                retCursor = db.query(UserFavoritesContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default:
                break;
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri = null;

        switch (match) {
            case MOVIES_CODE:

                long id = db.insert(UserFavoritesContract.TABLE_NAME, null, values);

                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(UserFavoritesContract.PATH_MOVIES_URI, id);
                }

                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
