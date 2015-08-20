package fi.guagua.pixrayandroid;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreTypes implements Serializable {

    private ArrayList<Integer> mIds;
    private ArrayList<String> mNames;
    private ArrayList<String> mColors;

    public ScoreTypes(ArrayList<Integer> ids, ArrayList<String> names, ArrayList<String> colors) {
        mIds = ids;
        mNames = names;
        mColors = colors;
    }

    public ArrayList<Integer> getIds() {
        return mIds;
    }

    public ArrayList<String> getNames() {
        return mNames;
    }

    public ArrayList<String> getColors() {
        return mColors;
    }
}
