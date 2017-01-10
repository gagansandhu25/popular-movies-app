package me.indiandollar.apps.popularmoviz.Models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Indian Dollar on 1/1/2017.
 */
public class MovieVideo implements Parcelable {

    private String mId;
    private Integer mMovieId;
    private String mVideoId;
    private String mVideoThumbnail;

    public MovieVideo() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Integer getMovieId() {
        return mMovieId;
    }

    public void setMovieId(Integer movieId) {
        mMovieId = movieId;
    }

    public String getVideoId() {
        return mVideoId;
    }
    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        mVideoThumbnail = videoThumbnail;
    }

    public String getVideoThumbnail() {
        return mVideoThumbnail;
    }

    public MovieVideo(Parcel in) {
        mId = in.readString();
        mMovieId = in.readByte() == 0x00 ? null : in.readInt();
        mVideoId = in.readString();
        mVideoThumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        if (mMovieId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mMovieId);
        }
        dest.writeString(mVideoId);
        dest.writeString(mVideoThumbnail);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };
}