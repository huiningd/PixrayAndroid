package fi.guagua.pixrayandroid;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WellConditionsDialog extends DialogFragment {
    private static final String TAG = "WellConditionsDialog";

    public WellConditionsDialog() {
        // Empty constructor required for DialogFragment
    }

    public static WellConditionsDialog newInstance(ArrayList<WellConditions> wcs) {
        WellConditionsDialog frag = new WellConditionsDialog();
        Bundle args = new Bundle();
        args.putSerializable(Pixray.EXTRA_WC, wcs);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_well_conditions, container);
        getDialog().setTitle(R.string.well_conditions);

        //if(getArguments().getSerializable(Pixray.EXTRA_WC) instanceof ArrayList) {}
        @SuppressWarnings("unchecked")
        ArrayList<WellConditions> wcs = (ArrayList<WellConditions>) getArguments().
                    getSerializable(Pixray.EXTRA_WC);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.wellCondition);
        for (int i = 0; i < wcs.size(); i++) {
            WellConditions wc = wcs.get(i);
            String wcClass = wc.getWcClass();
            TextView wcClassTv = new TextView(getActivity());
            wcClassTv.setText("- Class: " + wcClass);
            layout.addView(wcClassTv);
            //wcClass.setWidth();

            String wcName = wc.getName();
            TextView wcNameTv = new TextView(getActivity());
            wcNameTv.setText("  Name: " + wcName);
            layout.addView(wcNameTv);

            String concentration = wc.getConcentration();
            String units = wc.getUnits();
            TextView wcConcentrationTv = new TextView(getActivity());
            wcConcentrationTv.setText("  Concentration: " + concentration + " (" + units + ")");
            layout.addView(wcConcentrationTv);

            String pH = wc.getPH();
            if (!pH.isEmpty()) {
                TextView wcPhTv = new TextView(getActivity());
                wcPhTv.setText("  PH: " + pH);
                layout.addView(wcPhTv);
            }
        }

        Button dismissButton = (Button) view.findViewById(R.id.dismiss);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // dismiss the dialog
            }
        });

        return view;
    }

}
