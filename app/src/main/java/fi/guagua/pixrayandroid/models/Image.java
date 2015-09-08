package fi.guagua.pixrayandroid.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Image implements Serializable {
    private int[] mRowColumnDrop; // index 0 is row, index 1 is col, index 2 is drop.
    private GalleryInfo mGalleryInfo;
    private String mLabel;
    //private String mId;
    private String mThumbnailUrl;
    private String mLargeImageUrl;
    private String mSample;
    private String mScreenName;
    private ArrayList<WellConditions> mWellConditionses;
    private int mCurrentScoreId;
    private ScoreTypes mScoreTypes;

    public Image(GalleryInfo galleryInfo, String thumbnailUrl, int[] rowColDrop, String label) {
        mGalleryInfo = galleryInfo;
        mLabel = label;
        mThumbnailUrl = thumbnailUrl;
        mRowColumnDrop = rowColDrop;
    }

    public int[] getRowColumnDrop() {
        return mRowColumnDrop;
    }

    public GalleryInfo getGalleryInfo() {
        return mGalleryInfo;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        mLargeImageUrl = largeImageUrl;
    }

    public String toString() {
        return mLabel;
    }

    public String getSample() {
        return mSample;
    }

    public void setSample(String sample) {
        mSample = sample;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public void setScreenName(String screenName) {
        mScreenName = screenName;
    }

    public ArrayList<WellConditions> getWellConditionses() {
        return mWellConditionses;
    }

    public void setWellConditionses(ArrayList<WellConditions> wellConditionses) {
        mWellConditionses = wellConditionses;
    }

    public int getCurrentScoreId() {
        return mCurrentScoreId;
    }

    public void setCurrentScoreId(int currentScoreId) {
        mCurrentScoreId = currentScoreId;
    }

    public ScoreTypes getScoreTypes() {
        return mScoreTypes;
    }

    public void setScoreTypes(ScoreTypes scoreTypes) {
        mScoreTypes = scoreTypes;
    }
}
