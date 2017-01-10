package me.indiandollar.apps.popularmoviz.Database.Helpers;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.indiandollar.apps.popularmoviz.Database.Contracts.UserFavoritesContract;

/**
 * Created by Indian Dollar on 1/6/2017.
 */

public class UserFavoritesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "popularMoviz.db";
    private static String SQL_DROP_TABLE;
    private static String SQL_CREATE_TABLE;

    public UserFavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        SQL_CREATE_TABLE = "CREATE TABLE " +
                UserFavoritesContract.TABLE_NAME + "(" +
                UserFavoritesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserFavoritesContract.COL_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                UserFavoritesContract.COL_MOVIE_TITLE + " TEXT NOT NULL, " +
                UserFavoritesContract.COL_MOVIE_RATING + " DOUBLE NOT NULL, " +
                UserFavoritesContract.COL_MOVIE_RELEASE_DATE + " DATE NOT NULL, " +
                UserFavoritesContract.COL_MOVIE_POSTER + " TEXT, " +
                UserFavoritesContract.COL_MOVIE_PLOT + " TEXT NOT NULL, " +
                UserFavoritesContract.COL_MOVIE_BACKDROP + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + UserFavoritesContract.TABLE_NAME;

        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);

    }
}
