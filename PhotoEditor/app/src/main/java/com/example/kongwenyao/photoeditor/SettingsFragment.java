package com.example.kongwenyao.photoeditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private SeekBar strokeSeekBar;
    private Button strokeButton;
    private Button doneButton;

    OnColorChosenListener mCallBack;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //View Assignment
        strokeButton = getActivity().findViewById(R.id.stroke_button);
        strokeSeekBar = getActivity().findViewById(R.id.seekbar);
        doneButton = getActivity().findViewById(R.id.done_button);

        //Set Event Listener
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtionHandler();
            }
        });
        strokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strokeButtonHandler();
            }
        });
        strokeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCallBack.onSeekBarChange(progress);
                setLabelText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Set Stroke Seekbar
        int strokeWidth = Math.round(MainActivityFragment.strokeWidth);
        strokeSeekBar.setProgress(strokeWidth);
        setLabelText(strokeWidth);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallBack = (OnColorChosenListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnSettingSelectedListener");
        }
    }

    public void strokeButtonHandler() {
        final ColorPicker cp = new ColorPicker(getActivity(), MainActivityFragment.r, MainActivityFragment.g, MainActivityFragment.b, 73);
        cp.show();

        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                mCallBack.onColorChosen(Color.red(color), Color.green(color), Color.blue(color));
                cp.dismiss();
            }
        });
    }

    public void setLabelText(int progress) {
        TextView textView = getActivity().findViewById(R.id.seekbar_label);
        textView.setText("Stroke Width (" + String.valueOf(progress) + ")");
    }

    public void doneButtionHandler() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public interface OnColorChosenListener {
        void onColorChosen(int red, int green, int blue);
        void onSeekBarChange(int width);
    }

}
