package fi.guagua.pixrayandroid;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryInfo implements Serializable {
    private int mProjectId;
    private int mPlateId;
    private ArrayList<String> mDates;
    private ArrayList<Integer> mDateIds;
    private int mRequestDateId;
    private ArrayList<String> mTypes;
    private ArrayList<Integer> mTypeIds;
    private int mRequestTypeId;

    public GalleryInfo() {}

    public GalleryInfo(int projectId, int plateId, ArrayList<String> dates,
                       ArrayList<Integer> dateIds, int requestDateId,
                       ArrayList<Integer> typeIds, ArrayList<String> types, int requestTypeId) {
        mProjectId = projectId;
        mPlateId = plateId;
        mDates = dates;
        mDateIds = dateIds;
        mRequestDateId = requestDateId;
        mTypeIds = typeIds;
        mTypes = types;
        mRequestTypeId = requestTypeId;
    }

    public String getDateById(int dateId) {
        int index = -1;
        for (int i = 0; i < mDateIds.size(); i++) {
            if (mDateIds.get(i) == dateId) index = i;
        }
        if (index >= 0) return mDates.get(index);
        return null;
    }

    public String getTypeById(int typeId) {
        Log.d("GalleryInfo", "type id is " + typeId);
        int index = -1;
        for (int i = 0; i < mTypeIds.size(); i++) {
            if (mTypeIds.get(i) == typeId) index = i;
        }
        Log.d("GalleryInfo", "index is " + index);
        if (index >= 0) return mTypes.get(index);
        return null;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public int getPlateId() {
        return mPlateId;
    }

    public int getRequestDateId() {
        return mRequestDateId;
    }

    public void setRequestDateId(int requestDateId) {
        mRequestDateId = requestDateId;
    }

    public ArrayList<String> getDates() {
        return mDates;
    }

    public ArrayList<Integer> getDateIds() {
        return mDateIds;
    }

    public ArrayList<String> getTypes() {
        return mTypes;
    }

    public ArrayList<Integer> getTypeIds() {
        return mTypeIds;
    }

    public int getRequestTypeId() {
        return mRequestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        mRequestTypeId = requestTypeId;
    }

    @Override
    public String toString() { // TODO: print dates and datesIds
        return "Gallery [projectId=" + mProjectId + ", plateId=" + mPlateId
                + ", requestDateId=" + mRequestDateId + "]";
    }

}
