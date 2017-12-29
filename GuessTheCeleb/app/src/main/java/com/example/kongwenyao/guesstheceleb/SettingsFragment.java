package com.example.kongwenyao.guesstheceleb;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    OnSettingSelectedListener mCallBack;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //View reference
        RadioGroup selectionOption = getActivity().findViewById(R.id.selction_option_setting);
        setRadioButtonListener(selectionOption);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallBack = (OnSettingSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnSettingSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        RadioButton button = (RadioButton) v;
        boolean checked = button.isChecked();

        if (checked) {
            int chosenVal = Integer.parseInt(String.valueOf(button.getText()));
            mCallBack.onRadioButtonSelected(chosenVal); //Pass data to interface as argument
        }
    }

    //Interface for passing data from this fragment to another
    public interface OnSettingSelectedListener {
        void onRadioButtonSelected(int buttonText);
    }

    public void setRadioButtonListener(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setOnClickListener(this);
        }
    }

}
