package fi.guagua.pixrayandroid.models;

public class Project {

    private int mId;
    private String mName;

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

    public String toString() {
        return mName;
    }

}
