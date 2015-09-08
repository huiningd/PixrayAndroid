package fi.guagua.pixrayandroid.models;

public class Project {

    private static final String TAG = "Project";
    private int mId;
    private String mName;
    private String mOwner;
    private boolean mVisible;

    public Project() {
    }

    public Project(int id, String name) {
        mId = id;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public String toString() {
        return mName;
    }

}
