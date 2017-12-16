package com.example.kongwenyao.guesstheceleb;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MainGame.class contains main functionality of GuessTheCeleb application and is build to adapt
 * dynamic changes of added images. New celeb images can be added to drawable prefix with "celeb_"
 * (Eg. "celeb_Barrack_Obama" or "celeb_Donald_Trump").
 *
 * Created by kongwenyao on 12/14/17.
 */

public class MainGame {

    private TextView.OnClickListener onClickListener;
    private Activity activity;
    private int numOfOptions;
    private int mainCelebImageId = 0;

    public MainGame(Activity activity) {
        this.activity = activity;
    }

    /**
     * This method generates selection choices in text view. All text view will contain celeb name
     * found in celeb images stored within drawable folder.
     *
     * @return Array of TextView
     */
    public TextView[] generateGuessChoices() throws IllegalAccessException {
        String[] celebsNames = getCelebsImageName();    //Example: ["adam levine", "emma stone", ...]
        ArrayList<String> optionChoices = new ArrayList<>();    //Record for randomize selection choice
        int totalCeleb = celebsNames.length;
        int randomVal;
        String celebName = null;
        String string = null;

        Random rand = new Random();
        int answerPosition = rand.nextInt(numOfOptions);  //Randomize position of correct choice

        TextView[] optionTextViews = new TextView[numOfOptions];
        for (int i = 0; i < numOfOptions; i++) {
            //Set textView
            TextView textView = new TextView(activity);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(50, 20, 20, 20);
            textView.setOnClickListener(onClickListener);
            textView.setTextSize(18);

            //Get celebrity name
            if (i != answerPosition) {
                randomVal = rand.nextInt(totalCeleb);
                celebName = celebsNames[randomVal];
            } else {
                if (mainCelebImageId != 0) {
                    celebName = getImageName(mainCelebImageId);
                }
            }

            //Check whether generated celebName exist in already chosen selection choices
            while (optionChoices.contains(celebName)) {
                randomVal = rand.nextInt(totalCeleb);
                celebName = celebsNames[randomVal];
            }

            optionChoices.add(celebName);
            string = (i + 1) + ". " + capitalizeString(celebName);
            textView.setText(string);
            optionTextViews[i] = textView;
        }
        return optionTextViews;
    }

    /**
     * This method retrieve all celeb name from images (prefix with "celeb_") within drawable folder.
     *
     * @return Array of strings contain celeb name found through file name of celeb image within
     * drawable folder
     */
    public String[] getCelebsImageName() throws IllegalAccessException {
        Field[] fieldsId = R.drawable.class.getFields(); //Get id of all files in drawable
        List<Integer> celebImageId = new ArrayList<>();
        String fieldName;

        for (Field field: fieldsId) {
            fieldName = field.getName(); //Get field name that represent the field object

            if (fieldName.contains("celeb")) {
                celebImageId.add(field.getInt(null));
            }
        }

        String imageName;
        List celebsNames = new ArrayList<String>();

        //Get celebrity name of drawable file with "celeb" prefix
        for (int imageId: celebImageId) {
            imageName = getImageName(imageId);  //Example: "adam levine"
            celebsNames.add(imageName);
        }

        return (String[]) celebsNames.toArray(new String[celebsNames.size()]);
    }

    /**
     * This method generate id of random celeb image from drawable resource.
     *
     * @return Id of drawable image
     */
    public int generateRandomImageId() throws IllegalAccessException {
        String[] celebsNames = getCelebsImageName();

        Random rand = new Random();
        int randomVal = rand.nextInt(celebsNames.length);

        String[] celebName = celebsNames[randomVal].split(" "); //Example: "adam levine"
        String imageName = "celeb_" + celebName[0] + "_" + celebName[1]; //Example: "celeb_adam_levine"
        int imageId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
        setCelebImageId(imageId);
        return imageId;
    }

    /**
     * This method capitalize a string.
     *
     * @param string value that is to be capitalized
     * @return Array of TextView
     */
    public String capitalizeString(String string) {
        String[] stringSplit = string.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        String capitalizeWord;

        for (int i = 0; i < stringSplit.length; i++) {
            if (i != (stringSplit.length - 1)) {
                capitalizeWord = Character.toUpperCase(stringSplit[i].charAt(0)) + stringSplit[i].substring(1) + " ";
            } else {
                capitalizeWord = Character.toUpperCase(stringSplit[i].charAt(0)) + stringSplit[i].substring(1);
            }
            stringBuilder.append(capitalizeWord);
        }

        return stringBuilder.toString();
    }

    /**
     * This method verifies a given expected string with the actual string. Expected param must
     * prefix with 3 letter before the actual string (Eg. "1. " or "a. ")
     *
     * @param  expected String prefix with three letters before the expected value that is to be compare
     * @param actual Actual string to be compare
     * @return true if both equal and false if both unequal
     */
    public boolean validateInputGuess(String expected, String actual) { //TODO: format "1. Jennifer Lawrence"
        return expected.substring(3).toLowerCase().equals(actual);
    }


    /**
     * This method get celeb name from image file that match the given image id
     *
     * @param  imageId Id represents the drawable image
     * @return Name of the celeb based from the specified file name of the image
     */
    public String getImageName(int imageId) {
        String[] imageName = activity.getResources().getResourceEntryName(imageId).split("_");
        String name = imageName[1] + " " + imageName[2];

        return name;
    }

    public void setOnClickListener(TextView.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setNumOfOptions(int number) {
        numOfOptions = number;
    }

    private void setCelebImageId(int imageId) {
        mainCelebImageId = imageId;
    }

}
