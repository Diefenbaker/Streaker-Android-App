package ie.wit.streaker.activities.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.streaker.R;
import ie.wit.streaker.activities.MainActivity;
import ie.wit.streaker.activities.models.Results;

/**
 * A simple {@link Fragment} subclass.
 */
public class Game extends Fragment implements View.OnClickListener {

    private static TableRow game1;
    private static TableRow game2;
    private static TableRow game3;
    private static TableRow game4;
    private static TableRow game5;
    private static TableRow game6;
    private static TableRow game7;
    private static TableRow game8;
    private static TableRow game9;
    private static TableRow game10;

    private static Button submit;

    public ToggleButton tempG1C1, tempG1C2, tempG1C3, tempG2C1, tempG2C2, tempG2C3, tempG3C1,tempG3C2,
            tempG3C3, tempG4C1, tempG4C2, tempG4C3, tempG5C1, tempG5C2, tempG5C3, tempG6C1, tempG6C2, tempG6C3,
            tempG7C1, tempG7C2, tempG7C3, tempG8C1, tempG8C2, tempG8C3, tempG9C1, tempG9C2, tempG9C3, tempG10C1,
            tempG10C2, tempG10C3;
    Results gameTemp;



    public Game() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);

    }


    public void onViewCreated(View v, Bundle savedInstanceState){

        game1 = (TableRow)v.findViewById(R.id.game1);
        game2 = (TableRow)v.findViewById(R.id.game2);
        game3 = (TableRow)v.findViewById(R.id.game3);
        game4 = (TableRow)v.findViewById(R.id.game4);
        game5 = (TableRow)v.findViewById(R.id.game5);
        game6 = (TableRow)v.findViewById(R.id.game6);
        game7 = (TableRow)v.findViewById(R.id.game7);
        game8 = (TableRow)v.findViewById(R.id.game8);
        game9 = (TableRow)v.findViewById(R.id.game9);
        game10 = (TableRow)v.findViewById(R.id.game10);

        submit = (Button) v.findViewById(R.id.submitButton);
        submit.setOnClickListener(this);

        tempG1C1 = (ToggleButton) game1.getChildAt(0);
        tempG1C2 = (ToggleButton) game1.getChildAt(1);
        tempG1C3 = (ToggleButton) game1.getChildAt(2);
        tempG2C1 = (ToggleButton) game2.getChildAt(0);
        tempG2C2 = (ToggleButton) game2.getChildAt(1);
        tempG2C3 = (ToggleButton) game2.getChildAt(2);
        tempG3C1 = (ToggleButton) game3.getChildAt(0);
        tempG3C2 = (ToggleButton) game3.getChildAt(1);
        tempG3C3 = (ToggleButton) game3.getChildAt(2);
        tempG4C1 = (ToggleButton) game4.getChildAt(0);
        tempG4C2 = (ToggleButton) game4.getChildAt(1);
        tempG4C3 = (ToggleButton) game4.getChildAt(2);
        tempG5C1 = (ToggleButton) game5.getChildAt(0);
        tempG5C2 = (ToggleButton) game5.getChildAt(1);
        tempG5C3 = (ToggleButton) game5.getChildAt(2);
        tempG6C1 = (ToggleButton) game6.getChildAt(0);
        tempG6C2 = (ToggleButton) game6.getChildAt(1);
        tempG6C3 = (ToggleButton) game6.getChildAt(2);
        tempG7C1 = (ToggleButton) game7.getChildAt(0);
        tempG7C2 = (ToggleButton) game7.getChildAt(1);
        tempG7C3 = (ToggleButton) game7.getChildAt(2);
        tempG8C1 = (ToggleButton) game8.getChildAt(0);
        tempG8C2 = (ToggleButton) game8.getChildAt(1);
        tempG8C3 = (ToggleButton) game8.getChildAt(2);
        tempG9C1 = (ToggleButton) game9.getChildAt(0);
        tempG9C2 = (ToggleButton) game9.getChildAt(1);
        tempG9C3 = (ToggleButton) game9.getChildAt(2);
        tempG10C1 = (ToggleButton) game10.getChildAt(0);
        tempG10C2 = (ToggleButton) game10.getChildAt(1);
        tempG10C3 = (ToggleButton) game10.getChildAt(2);


        if(Account.temp != null || MainActivity.temp != null ) {

            if(Account.temp != null) {
                gameTemp = Account.temp;
            }else{
                gameTemp = MainActivity.temp;
            }

            if (gameTemp.game01.matches(tempG1C1.getTextOn().toString())) {
                tempG1C1.setChecked(true);
            }else if(gameTemp.game01.matches(tempG1C2.getTextOn().toString())) {
                tempG1C2.setChecked(true);
            }else{
                tempG1C3.setChecked(true);
            }

            if (gameTemp.game02.matches(tempG2C1.getTextOn().toString())) {
                tempG2C1.setChecked(true);
            }else if(gameTemp.game02.matches(tempG2C2.getTextOn().toString())) {
                tempG2C2.setChecked(true);
            }else{
                tempG2C3.setChecked(true);
            }

            if (gameTemp.game03.matches(tempG3C1.getTextOn().toString())) {
                tempG3C1.setChecked(true);
            }else if(gameTemp.game03.matches(tempG3C2.getTextOn().toString())) {
                tempG3C2.setChecked(true);
            }else{
                tempG3C3.setChecked(true);
            }

            if (gameTemp.game04.matches(tempG4C1.getTextOn().toString())) {
                tempG4C1.setChecked(true);
            }else if(gameTemp.game04.matches(tempG4C2.getTextOn().toString())) {
                tempG4C2.setChecked(true);
            }else{
                tempG4C3.setChecked(true);
            }

            if (gameTemp.game05.matches(tempG5C1.getTextOn().toString())) {
                tempG5C1.setChecked(true);
            }else if(gameTemp.game05.matches(tempG5C2.getTextOn().toString())) {
                tempG5C2.setChecked(true);
            }else{
                tempG5C3.setChecked(true);
            }

            if (gameTemp.game06.matches(tempG6C1.getTextOn().toString())) {
                tempG6C1.setChecked(true);
            }else if(gameTemp.game06.matches(tempG6C2.getTextOn().toString())) {
                tempG6C2.setChecked(true);
            }else{
                tempG6C3.setChecked(true);
            }

            if (gameTemp.game07.matches(tempG7C1.getTextOn().toString())) {
                tempG7C1.setChecked(true);
            }else if(gameTemp.game07.matches(tempG7C2.getTextOn().toString())) {
                tempG7C2.setChecked(true);
            }else{
                tempG7C3.setChecked(true);
            }

            if (gameTemp.game08.matches(tempG8C1.getTextOn().toString())) {
                tempG8C1.setChecked(true);
            }else if(gameTemp.game08.matches(tempG8C2.getTextOn().toString())) {
                tempG8C2.setChecked(true);
            }else{
                tempG8C3.setChecked(true);
            }

            if (gameTemp.game09.matches(tempG9C1.getTextOn().toString())) {
                tempG9C1.setChecked(true);
            }else if(gameTemp.game09.matches(tempG9C2.getTextOn().toString())) {
                tempG9C2.setChecked(true);
            }else{
                tempG9C3.setChecked(true);
            }

            if (gameTemp.game10.matches(tempG10C1.getTextOn().toString())) {
                tempG10C1.setChecked(true);
            }else if(gameTemp.game10.matches(tempG10C2.getTextOn().toString())) {
                tempG10C2.setChecked(true);
            }else{
                tempG10C3.setChecked(true);
            }

        }

    }



    public void onStart(){
        super.onStart();



    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.submitButton:
                playGame();

                break;

        }

    }


    public void playGame(){

        final String thisWeek = "GameWeek6";


        if(tempG1C1.isChecked() && tempG1C2.isChecked() && tempG1C3.isChecked() ||
                tempG1C1.isChecked() && tempG1C2.isChecked() || tempG1C1.isChecked() &&
                tempG1C3.isChecked() || tempG1C2.isChecked() && tempG1C3.isChecked() ||

                tempG2C1.isChecked() && tempG2C2.isChecked() && tempG2C3.isChecked() ||
                tempG2C1.isChecked() && tempG2C2.isChecked() || tempG2C1.isChecked() &&
                tempG2C3.isChecked() || tempG2C2.isChecked() && tempG2C3.isChecked() ||

                tempG3C1.isChecked() && tempG3C2.isChecked() && tempG3C3.isChecked() ||
                tempG3C1.isChecked() && tempG3C2.isChecked() || tempG3C1.isChecked() &&
                tempG3C3.isChecked() || tempG3C2.isChecked() && tempG3C3.isChecked() ||

                tempG4C1.isChecked() && tempG4C2.isChecked() && tempG4C3.isChecked() ||
                tempG4C1.isChecked() && tempG4C2.isChecked() || tempG4C1.isChecked() &&
                tempG4C3.isChecked() || tempG4C2.isChecked() && tempG4C3.isChecked() ||

                tempG5C1.isChecked() && tempG5C2.isChecked() && tempG5C3.isChecked() ||
                tempG5C1.isChecked() && tempG5C2.isChecked() || tempG5C1.isChecked() &&
                tempG5C3.isChecked() || tempG5C2.isChecked() && tempG5C3.isChecked() ||

                tempG6C1.isChecked() && tempG6C2.isChecked() && tempG6C3.isChecked() ||
                tempG6C1.isChecked() && tempG6C2.isChecked() || tempG6C1.isChecked() &&
                tempG6C3.isChecked() || tempG6C2.isChecked() && tempG6C3.isChecked() ||

                tempG7C1.isChecked() && tempG7C2.isChecked() && tempG7C3.isChecked() ||
                tempG7C1.isChecked() && tempG7C2.isChecked() || tempG7C1.isChecked() &&
                tempG7C3.isChecked() || tempG7C2.isChecked() && tempG7C3.isChecked() ||

                tempG8C1.isChecked() && tempG8C2.isChecked() && tempG8C3.isChecked() ||
                tempG8C1.isChecked() && tempG8C2.isChecked() || tempG8C1.isChecked() &&
                tempG8C3.isChecked() || tempG8C2.isChecked() && tempG8C3.isChecked() ||

                tempG9C1.isChecked() && tempG9C2.isChecked() && tempG9C3.isChecked() ||
                tempG9C1.isChecked() && tempG9C2.isChecked() || tempG9C1.isChecked() &&
                tempG9C3.isChecked() || tempG9C2.isChecked() && tempG9C3.isChecked() ||

                tempG10C1.isChecked() && tempG10C2.isChecked() && tempG10C3.isChecked() ||
                tempG10C1.isChecked() && tempG10C2.isChecked() || tempG10C1.isChecked() &&
                tempG10C3.isChecked() || tempG10C2.isChecked() && tempG10C3.isChecked() ||

                !tempG1C1.isChecked() && !tempG1C2.isChecked() && !tempG1C3.isChecked() ||
                !tempG2C1.isChecked() && !tempG2C2.isChecked() && !tempG2C3.isChecked() ||
                !tempG3C1.isChecked() && !tempG3C2.isChecked() && !tempG3C3.isChecked() ||
                !tempG4C1.isChecked() && !tempG4C2.isChecked() && !tempG4C3.isChecked() ||
                !tempG5C1.isChecked() && !tempG5C2.isChecked() && !tempG5C3.isChecked() ||
                !tempG6C1.isChecked() && !tempG6C2.isChecked() && !tempG6C3.isChecked() ||
                !tempG7C1.isChecked() && !tempG7C2.isChecked() && !tempG7C3.isChecked() ||
                !tempG8C1.isChecked() && !tempG8C2.isChecked() && !tempG8C3.isChecked() ||
                !tempG9C1.isChecked() && !tempG9C2.isChecked() && !tempG9C3.isChecked() ||
                !tempG10C1.isChecked() && !tempG10C2.isChecked() && !tempG10C3.isChecked()){

            Toast.makeText(getContext(), "Please select one, and only one, result for each game.", Toast.LENGTH_LONG).show();

        }else {

            String predict1, predict2, predict3, predict4, predict5,
                    predict6, predict7, predict8, predict9, predict10;

            if (tempG1C1.isChecked()) {
                predict1 = (String) tempG1C1.getTextOn();
            } else {
                if (tempG1C2.isChecked()) {
                    predict1 = (String) tempG1C2.getTextOn();
                } else {
                    predict1 = (String) tempG1C3.getTextOn();
                }
            }

            if (tempG2C1.isChecked()) {
                predict2 = (String) tempG2C1.getTextOn();
            } else {
                if (tempG2C2.isChecked()) {
                    predict2 = (String) tempG2C2.getTextOn();
                } else {
                    predict2 = (String) tempG2C3.getTextOn();
                }
            }

            if (tempG3C1.isChecked()) {
                predict3 = (String) tempG3C1.getTextOn();
            } else {
                if (tempG3C2.isChecked()) {
                    predict3 = (String) tempG3C2.getTextOn();
                } else {
                    predict3 = (String) tempG3C3.getTextOn();
                }
            }

            if (tempG4C1.isChecked()) {
                predict4 = (String) tempG4C1.getTextOn();
            } else {
                if (tempG4C2.isChecked()) {
                    predict4 = (String) tempG4C2.getTextOn();
                } else {
                    predict4 = (String) tempG4C3.getTextOn();
                }
            }

            if (tempG5C1.isChecked()) {
                predict5 = (String) tempG5C1.getTextOn();
            } else {
                if (tempG5C2.isChecked()) {
                    predict5 = (String) tempG5C2.getTextOn();
                } else {
                    predict5 = (String) tempG5C3.getTextOn();
                }
            }

            if (tempG6C1.isChecked()) {
                predict6 = (String) tempG6C1.getTextOn();
            } else {
                if (tempG6C2.isChecked()) {
                    predict6 = (String) tempG6C2.getTextOn();
                } else {
                    predict6 = (String) tempG6C3.getTextOn();
                }
            }

            if (tempG7C1.isChecked()) {
                predict7 = (String) tempG7C1.getTextOn();
            } else {
                if (tempG7C2.isChecked()) {
                    predict7 = (String) tempG7C2.getTextOn();
                } else {
                    predict7 = (String) tempG7C3.getTextOn();
                }
            }

            if (tempG8C1.isChecked()) {
                predict8 = (String) tempG8C1.getTextOn();
            } else {
                if (tempG8C2.isChecked()) {
                    predict8 = (String) tempG8C2.getTextOn();
                } else {
                    predict8 = (String) tempG8C3.getTextOn();
                }
            }

            if (tempG9C1.isChecked()) {
                predict9 = (String) tempG9C1.getTextOn();
            } else {
                if (tempG9C2.isChecked()) {
                    predict9 = (String) tempG9C2.getTextOn();
                } else {
                    predict9 = (String) tempG9C3.getTextOn();
                }
            }

            if (tempG10C1.isChecked()) {
                predict10 = (String) tempG10C1.getTextOn();
            } else {
                if (tempG10C2.isChecked()) {
                    predict10 = (String) tempG10C2.getTextOn();
                } else {
                    predict10 = (String) tempG10C3.getTextOn();
                }
            }


            Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(300);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Results userSelection = new Results(predict1, predict2, predict3, predict4,
                    predict5, predict6, predict7, predict8, predict9, predict10);

            DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();

            if(Account.temp != null || MainActivity.temp != null){

                String changeThisOne;

                if(Account.whichWeek != null){
                    changeThisOne = Account.whichWeek;
                }else {
                    changeThisOne = MainActivity.whichWeek;
                }

                DatabaseReference dataRef = myDatabase.child("games").child(user.getUid()).child(changeThisOne);

                dataRef.setValue(userSelection, new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        FragmentManager fragmentManager = getFragmentManager();
                        if (databaseError != null) {
                            Toast.makeText(getContext(), "Something went wrong: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Selection saved successfully", Toast.LENGTH_LONG).show();
                            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Account()).addToBackStack("Account").commit();
                        }

                        Account.whichWeek = null;
                        MainActivity.whichWeek = null;
                        Account.temp = null;
                        MainActivity.temp = null;
                    }
                });
            }else{
                DatabaseReference dataRef = myDatabase.child("games").child(user.getUid()).child(thisWeek);

                dataRef.setValue(userSelection, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        FragmentManager fragmentManager = getFragmentManager();
                        if (databaseError != null) {
                            Toast.makeText(getContext(), "Something went wrong: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Selection saved successfully", Toast.LENGTH_LONG).show();
                            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new MainFragment()).addToBackStack("Main").commit();
                            }

                    }
                });

            }
        }
    }
}
