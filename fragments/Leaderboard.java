package ie.wit.streaker.activities.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ie.wit.streaker.R;
import ie.wit.streaker.activities.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class Leaderboard extends Fragment {


    ListView leaderList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    private DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();


    public Leaderboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {

        leaderList = (ListView) v.findViewById(R.id.leaderList);
        arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1,
                arrayList);
        leaderList.setAdapter(arrayAdapter);
        //leaderList.setOnItemClickListener(getContext());

        Query users = myDatabase.child("users").orderByChild("userWins");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()){
                    User pulledUser = users.getValue(User.class);
                    String pulledName = pulledUser.userName;
                    int pulledWins = pulledUser.userWins;
                    String toList = Integer.toString(pulledWins)+" Wins: "+pulledName;
                    Log.v("Leaderboard", users.getKey());
                    arrayList.add(toList);
                    //

                }
                Collections.reverse(arrayList);
                arrayAdapter.notifyDataSetChanged();
            } //Not finished implementing arraylist in mainactivity yet, first on list tomorrow

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
