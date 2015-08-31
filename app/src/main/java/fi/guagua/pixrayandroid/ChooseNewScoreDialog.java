package fi.guagua.pixrayandroid;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;


public class ChooseNewScoreDialog extends DialogFragment{
    private static final String TAG = "ChooseNewScoreDialog";
    private RadioGroup mRadioGroup;
    private ArrayList<Integer> mIds;
    private ArrayList<String> mNames;
    private ArrayList<String> mColors;
    protected OnScoreSelectedListener mListener;

    public ChooseNewScoreDialog() {
        // Empty constructor required for DialogFragment
    }

    public static ChooseNewScoreDialog newInstance(ScoreTypes scoreTypes) {
        ChooseNewScoreDialog frag = new ChooseNewScoreDialog();
        Bundle args = new Bundle();
        args.putSerializable(Pixray.EXTRA_SCORE_TYPES, scoreTypes);
        //args.putInt(Pixray.EXTRA_CURRENT_SCORE, scoreId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_new_score, container);
        getDialog().setTitle(R.string.choose_score_below);

        ScoreTypes st = (ScoreTypes) getArguments().getSerializable(Pixray.EXTRA_SCORE_TYPES);
        mIds = st.getIds();
        mNames = st.getNames();
        mColors = st.getColors();

        // create scoring radio buttons programmatically
        mRadioGroup = (RadioGroup) view.findViewById(R.id.scoringChoices);
        for (int i = 0; i < mIds.size(); i++) {
            RadioButton newRadioButton = new RadioButton(getActivity());
            newRadioButton.setText(mNames.get(i));
            newRadioButton.setId(mIds.get(i));
            newRadioButton.setBackgroundColor(Color.parseColor(mColors.get(i)));
            newRadioButton.setWidth(350);
            //newRadioButton.setLayoutParams();
            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            mRadioGroup.addView(newRadioButton, 0, layoutParams);
        }

        final Button submitButton = (Button) view.findViewById(R.id.chooseScoreConfirm);
        submitButton.setEnabled(false); // can't submit before a score is selected

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(true);
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.chooseScoreCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // dismiss the dialog
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // dismiss the dialog
                int scoreId = mRadioGroup.getCheckedRadioButtonId(); // chosen score
                // transmit data through interface
                if (mListener != null) {
                    mListener.onNewScoreSelected(scoreId);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnScoreSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnScoreSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnScoreSelectedListener {
        void onNewScoreSelected(int scoreId);
    }

}
