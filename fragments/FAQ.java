package ie.wit.streaker.activities.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.wit.streaker.R;
import ie.wit.streaker.activities.base.CustomExpandableListAdapter;
import ie.wit.streaker.activities.base.ExpandableListData;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQ extends Fragment{

    public FAQ() {
        // Required empty public constructor
    }




    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    TextView hiddenView;





        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_faq, container, false);
        }


        public void onViewCreated(View v, Bundle savedInstanceState) {

            hiddenView = (TextView) getView().findViewById(R.id.hiddenView);
            expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
            expandableListDetail = ExpandableListData.getData();
            expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
            expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);


          

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {


                    Log.v("FAQ", parent+" "+groupPosition+" "+childPosition+" "+id);

                    if(groupPosition == 0){
                        new MaterialDialog.Builder(getContext())
                                .iconRes(R.mipmap.logo_ball)
                                .limitIconToDefaultSize()
                                .title("Play the game!")
                                .content("The game is simple; select the result (winning team/draw) in " +
                                        "each of the 10 Premier League games being played this game week. Once all games have been"+
                                        "completed the correct results will be matched against your predictions. " +
                                        "The player with the highest streak of correct scores wins the pot!")
                                .positiveText("Got it!")
                                .show();
                    }

                    if(groupPosition == 1 && childPosition == 0){
                        new MaterialDialog.Builder(getContext())
                                .iconRes(R.mipmap.logo_ball)
                                .limitIconToDefaultSize()
                                .title("Account")
                                .content("In the account area your details (name&/email) can be changed, " +
                                        "but your Unique ID is fixed. Click 'Edit Details' to unlock your " +
                                        "current details, click 'Save Details' when you're done.")
                                .positiveText("Got it!")
                                .show();
                    }

                    if(groupPosition == 1 && childPosition == 1){
                        new MaterialDialog.Builder(getContext())
                                .iconRes(R.mipmap.logo_ball)
                                .limitIconToDefaultSize()
                                .title("Game History")
                                .content("The Account screen also shows details of your past predictions, " +
                                        "scroll through the Game Weeks until you find the one you want, click " +
                                        "on it to show details, then edit/delete/close. Alternatively, use the "+
                                        "search feature at the top of the Account screen, enter the Game Week number " +
                                        "and, if found, the details will appear.")
                                .positiveText("Got it!")
                                .show();
                    }

                    if(groupPosition == 2){
                        new MaterialDialog.Builder(getContext())
                                .iconRes(R.mipmap.logo_ball)
                                .limitIconToDefaultSize()
                                .title("We don't...")
                                .content("The money will be resting in my account.")
                                .positiveText("Got it? Good!")
                                .show();
                    }


                    return false;
                }
            });
        }

}
