package com.example.kongwenyao.guesstheceleb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityMainFragment extends Fragment implements View.OnClickListener {

    //Views
    private ImageView celebImage;
    private GridLayout mainGridLayout;

    //Global Variables
    MainGame mainGame;
    private String chosenCeleb;
    private String previousChosenCeleb;
    private boolean firstRound;
    static int numOfOptions;

    //Final
    static final String BUTTON_TEXT = "RadioButtonText";

    public ActivityMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_main, container, false);
        //view.setPadding(0,0,0,0);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainGame = new MainGame(getActivity());
        mainGame.setOnClickListener(this);
        updateNumOfOptions(numOfOptions);

        //View & Layout Assignment
        celebImage = getView().findViewById(R.id.celeb_image);
        mainGridLayout = getView().findViewById(R.id.main_grid_layout);

        //Generate contents
        try {
            firstRound = true;
            setContent();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        TextView view = (TextView) v;
        boolean result  = mainGame.validateInputGuess(view.getText().toString(), chosenCeleb);

        if (result) {
            Toast.makeText(getContext(), "Good job!", Toast.LENGTH_SHORT).show();

            try {
                setContent();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getContext(), "Wrong Answer! Guess again!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setContent() throws IllegalAccessException {
        int imageId = mainGame.generateRandomImageId();
        chosenCeleb = mainGame.getImageName(imageId); //Get chosen celeb name

        if (!firstRound) { //if not the first game
            while (chosenCeleb.equals(previousChosenCeleb)) { //avoid getting pick the same celebrity as previous round
                imageId = mainGame.generateRandomImageId();
                chosenCeleb = mainGame.getImageName(imageId);
            }
        } else {
            firstRound = false;
            previousChosenCeleb = chosenCeleb;
        }

        celebImage.setImageResource(imageId);

        TextView[] textViews = mainGame.generateGuessChoices();
        mainGridLayout.removeAllViews();    //Clear all possible choice options

        //Set possible choices
        for (TextView textView: textViews) {
            mainGridLayout.addView(textView);
        }
    }

    public void updateNumOfOptions(int numOfOptions) {
        mainGame.setNumOfOptions(numOfOptions);
    }
}
