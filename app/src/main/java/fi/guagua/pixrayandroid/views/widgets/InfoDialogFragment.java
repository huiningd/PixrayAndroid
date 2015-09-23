package fi.guagua.pixrayandroid.views.widgets;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fi.guagua.pixrayandroid.R;
import fi.guagua.pixrayandroid.utils.Utility;

public class InfoDialogFragment extends DialogFragment {

    private TextView mDateInfo;
    private TextView mTypeInfo;

    public InfoDialogFragment() {
        // Empty constructor requiredfor DialogFragment
    }

    public static InfoDialogFragment newInstance(String date, String type) {
        InfoDialogFragment frag = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString(Utility.EXTRA_GALLERY_DATE, date);
        args.putString(Utility.EXTRA_GALLERY_TYPE, type);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_dialog, container);
        mDateInfo = (TextView) view.findViewById(R.id.date_info);
        mTypeInfo = (TextView) view.findViewById(R.id.type_info);
        String date = getArguments().getString(Utility.EXTRA_GALLERY_DATE);
        String type = getArguments().getString(Utility.EXTRA_GALLERY_TYPE);

        getDialog().setTitle(R.string.gallery_info_title);
        mDateInfo.setText(getActivity().getString(R.string.date_is) + date);
        mTypeInfo.setText(getActivity().getString(R.string.type_is) + type);

        Button okButton = (Button) view.findViewById(R.id.info_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
