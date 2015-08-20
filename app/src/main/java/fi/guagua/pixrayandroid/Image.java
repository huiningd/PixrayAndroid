package fi.guagua.pixrayandroid;

import java.io.Serializable;

public class Image implements Serializable {
    private int mRows;
    private int mColumns;
    private int mDrops;
    private GalleryInfo mGalleryInfo;
    private String mLabel;
    //private String mId;
    private String mThumbnailUrl;
    private String mLargeImageUrl;

    public Image(GalleryInfo galleryInfo, int rows, int cols, int drops, String thumbnailUrl, String label) {
        mGalleryInfo = galleryInfo;
        mRows = rows;
        mColumns = cols;
        mDrops = drops;
        mLabel = label;
        mThumbnailUrl = thumbnailUrl;
    }

    public int getRows() {
        return mRows;
    }

    public int getColumns() {
        return mColumns;
    }

    public int getDrops() {
        return mDrops;
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

}
