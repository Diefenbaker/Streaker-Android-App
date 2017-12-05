package ie.wit.streaker.activities.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ie.wit.streaker.R;
import ie.wit.streaker.activities.models.History;
import ie.wit.streaker.activities.models.User;


public class MainFragment extends Fragment implements View.OnClickListener {


    InterstitialAd mInterstitialAd;
    TextView welcomeBack, lastWinner;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
    WebView webView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getContext(), "hidden");
/*        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("hidden")
                .build();
        adView.loadAd(adRequest);
        */

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("hidden");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

        requestNewInterstitial();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        welcomeBack = (TextView)view.findViewById(R.id.welcomeBackTV);
        myDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User pulledUser = dataSnapshot.getValue(User.class);
                        welcomeBack.setText("Welcome back "+pulledUser.userName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        lastWinner = (TextView)view.findViewById(R.id.lastWeeksWinnerTV);

        myDatabase.child("results").child("gameweek5").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                History temp = dataSnapshot.getValue(History.class);
                Log.v("MainFragment", "Temp winner: "+temp.winner);
                lastWinner.setText("Last weeks Streaker was: "+temp.winner+"!");

            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        webView = (WebView)view.findViewById(R.id.webView);
        webView.loadUrl("http://www.bbc.com/sport/football/38236379");

        return view;

    }




    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("hidden")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getFragmentManager();

        switch (v.getId()) {

        }
    }
}
