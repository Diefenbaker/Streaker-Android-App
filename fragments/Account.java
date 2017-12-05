package ie.wit.streaker.activities.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ie.wit.streaker.R;
import ie.wit.streaker.activities.models.Results;
import ie.wit.streaker.activities.models.User;

import static ie.wit.streaker.activities.MainActivity.navEmail;
import static ie.wit.streaker.activities.MainActivity.navName;


public class Account extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    private EditText accountName, accountEmail, nameHead, emailHead, idHead, accountID;
    private Button editButton;
    FirebaseUser user;
    ListView gameList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    String edit = "EDIT DETAILS";
    public static Results temp;
    public static String whichWeek;
    CircleImageView profileImage;

    private DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();


    public Account() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.v("Login", user.toString());

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.findItem(R.id.action_search).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onStart(){

        super.onStart();
    }


    public void onViewCreated(View v, Bundle savedInstanceState) {


        accountName = (EditText) v.findViewById(R.id.accountName);
        accountEmail = (EditText) v.findViewById(R.id.accountEmail);
        accountID = (EditText) v.findViewById(R.id.accountID);
        nameHead = (EditText) v.findViewById(R.id.nameHead);
        emailHead = (EditText) v.findViewById(R.id.emailHead);
        idHead = (EditText) v.findViewById(R.id.idHead);
        editButton = (Button) v.findViewById(R.id.editButton);
        gameList = (ListView) v.findViewById(R.id.gameList);
        profileImage = (CircleImageView)v.findViewById(R.id.profile_image);

        accountID.setText(user.getUid());

        accountName.setEnabled(false);
        accountEmail.setEnabled(false);
        accountID.setEnabled(false);
        nameHead.setEnabled(false);
        emailHead.setEnabled(false);
        idHead.setEnabled(false);



        myDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User pulledUser = dataSnapshot.getValue(User.class);
                        accountEmail.setText(pulledUser.userEmail);
                        accountName.setText(pulledUser.userName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        editButton.setOnClickListener(this);


        if (user.getPhotoUrl() == null) {
            profileImage.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_menu_camera));
        } else {
            Glide.with(getContext())
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .into(profileImage);
        }


        arrayList.clear();

        arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1,
                arrayList);
        gameList.setAdapter(arrayAdapter);
        gameList.setOnItemClickListener(this);

        Query gameHistory = myDatabase.child("games").child(user.getUid());
        gameHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot gameHistory : dataSnapshot.getChildren()){
                    Log.v("Account", gameHistory.getKey());
                    arrayList.add(gameHistory.getKey());
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        final String choice = arrayList.get(position);

        myDatabase.child("games").child(user.getUid()).child(choice).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Results thisGame = dataSnapshot.getValue(Results.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Your choices for "+choice+" :");
                builder.setMessage("Selection 01: "+thisGame.game01+"\nSelection 02: "+
                        thisGame.game02+"\nSelection 03: "+thisGame.game03+"\nSelection 04: "+
                        thisGame.game04+"\nSelection 05: "+thisGame.game05+"\nSelection 06: "+
                        thisGame.game06+"\nSelection 07: "+thisGame.game07+"\nSelection 08: "+
                        thisGame.game08+"\nSelection 09: "+thisGame.game09+"\nSelection 10: "+
                        thisGame.game10);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDatabase.child("games").child(user.getUid()).child(choice).removeValue();
                        //arrayAdapter.notifyDataSetChanged();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Account()).addToBackStack("Account").commit();
                        Toast.makeText(getContext(),"Entry removed.",Toast.LENGTH_LONG).show();

                    }
                });

                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        temp = thisGame;
                        whichWeek = choice;
                        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame, new Game()).addToBackStack("").commit();
                    }
                });

                builder.show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.editButton:

                if(editButton.getText().toString().equalsIgnoreCase(edit)){

                    accountName.setEnabled(true);
                    accountEmail.setEnabled(true);
                    editButton.setText("Save Details");

                }
                else{

                    String newName = accountName.getText().toString();
                    String newEmail = accountEmail.getText().toString();

                    if(newName.isEmpty() || isValidEmail(newEmail) == false){
                        Toast.makeText(getContext(), "Please check entered details.", Toast.LENGTH_LONG).show();
                    }
                    else{

                        myDatabase.child("users").child(user.getUid()).child("userName").setValue(newName);
                        myDatabase.child("users").child(user.getUid()).child("userEmail").setValue(newEmail);

                        accountName.setEnabled(false);
                        accountEmail.setEnabled(false);
                        editButton.setText("Edit Details");
                        navName.setText(newName);
                        navEmail.setText(newEmail);
                        }
                }
                break;
        }
    }

    // Method implemented with assistance from: stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    public final static boolean isValidEmail(CharSequence target){

        if(TextUtils.isEmpty(target)){
            return false;
        }
        else{
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

    }

}


