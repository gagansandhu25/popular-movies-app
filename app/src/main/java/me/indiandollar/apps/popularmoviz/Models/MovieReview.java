package me.indiandollar.apps.popularmoviz.Models;

/**
 * Created by Indian Dollar on 1/4/2017.
 */

public class MovieReview {

    private String mId;
    private Integer mMovieId;
    private String mReviewer;
    private String mContent;

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

    public String getReviewer() {
        return mReviewer;
    }

    public void setReviewer(String reviewer) {
        mReviewer = reviewer;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
