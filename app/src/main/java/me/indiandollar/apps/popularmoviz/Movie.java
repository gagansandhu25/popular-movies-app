package me.indiandollar.apps.popularmoviz;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Indian Dollar on 12/31/2016.
 */

public class Movie implements Parcelable {

    private String mTitle;
    private String mOriginalTitle;
    private Integer mId;
    private Double mVoteAverage;
    private String mOverview;
    private String mPosterPath;
    private Double mPopularity;
    private String mReleaseDate;


    public Movie() {
    }

    public String getReleaseDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        try {
            Date newDate = format.parse(mReleaseDate);

            SimpleDateFormat date = new SimpleDateFormat("MMM yyyy");
            return date.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public Double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Double popularity) {
        mPopularity = popularity;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mOriginalTitle = in.readString();
        mId = in.readByte() == 0x00 ? null : in.readInt();
        mVoteAverage = in.readByte() == 0x00 ? null : in.readDouble();
        mOverview = in.readString();
        mPosterPath = in.readString();
        mPopularity = in.readByte() == 0x00 ? null : in.readDouble();
        mReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOriginalTitle);
        if (mId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mId);
        }
        if (mVoteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mVoteAverage);
        }
        dest.writeString(mOverview);
        dest.writeString(mPosterPath);
        if (mPopularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mPopularity);
        }
        dest.writeString(mReleaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}