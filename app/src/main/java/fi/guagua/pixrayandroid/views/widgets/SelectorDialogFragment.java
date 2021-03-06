package fi.guagua.pixrayandroid.views.widgets;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fi.guagua.pixrayandroid.R;
import fi.guagua.pixrayandroid.utils.Utility;


public class SelectorDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = SelectorDialogFragment.class.getSimpleName();
    private View mRootView;
    protected OnDateOrTypeSelectedListener mListener;
    private String mWhichSelector;
    private ArrayList<String> mNames;
    private ArrayList<Integer> mIds;

    public SelectorDialogFragment() {
        // Required empty public constructor
    }

    public static SelectorDialogFragment newInstance(String whichSelector, ArrayList<String> names,
                                                     ArrayList<Integer> ids) {
        SelectorDialogFragment fragment = new SelectorDialogFragment();
        Bundle args = new Bundle();
        args.putString(Utility.EXTRA_WHICH_SELECTOR, whichSelector);
        args.putIntegerArrayList(Utility.EXTRA_DATA_IDS, ids);
        args.putStringArrayList(Utility.EXTRA_DATA_NAMES, names);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWhichSelector = getArguments().getString(Utility.EXTRA_WHICH_SELECTOR);
            mIds = getArguments().getIntegerArrayList(Utility.EXTRA_DATA_IDS);
            mNames = getArguments().getStringArrayList(Utility.EXTRA_DATA_NAMES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set title for the dialog
        if (mWhichSelector.equals(Utility.DATE_SELECTOR)) {
            getDialog().setTitle(R.string.date_selector_title);
        } else if (mWhichSelector.equals(Utility.TYPE_SELECTOR)) {
            getDialog().setTitle(R.string.type_selector_title);
        }

        mRootView = inflater.inflate(R.layout.fragment_selector, container, false);
        setCancelable(false); // dialog doesn't get cancelled by clicking outside of the dialog
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView = (ListView) mRootView.findViewById(R.id.selector_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        Button cancelButton = (Button) mRootView.findViewById(R.id.selector_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss(); // dismiss the dialog after user click on it
        Toast.makeText(getActivity(), "You've selected " + mNames.get(position), Toast.LENGTH_SHORT).show();
        int requestId = mIds.get(position);
        //mGalleryInfo.setRequestDateId(requestDateId);
        Log.d(TAG, "mRequestDateId: " + requestId);
        if (mListener != null) {
            mListener.onDateOrTypeSelected(requestId, mWhichSelector);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDateOrTypeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDateOrTypeSelectedListener {
        void onDateOrTypeSelected(int id, String whichSelector);
    }

}
