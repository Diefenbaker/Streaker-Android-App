package ie.wit.streaker.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import ie.wit.streaker.activities.fragments.Account;
import ie.wit.streaker.activities.fragments.FAQ;
import ie.wit.streaker.activities.fragments.Game;
import ie.wit.streaker.activities.fragments.Leaderboard;
import ie.wit.streaker.activities.fragments.MainFragment;
import ie.wit.streaker.activities.models.Results;
import ie.wit.streaker.activities.models.User;

import static ie.wit.streaker.activities.Login.mGoogleApiClient;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener{

    InterstitialAd mInterstitialAd;
    FragmentManager fragmentManager = getFragmentManager();
    public static SearchView searchView;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    ListView gameList;
    private DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static Results temp;
    public static String whichWeek;

    public static TextView navName;
    public static TextView navEmail;
    CircleImageView navPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this, "hidden"); //Initialising Admob

        //Original banner ad that was in place, wasn't playing nice with Fragments so swapped for interstitial
/*        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("hidden")
                .build();
        adView.loadAd(adRequest);
        */

        //Initialising interstitial ad, guide found at: https://firebase.google.com/docs/admob/android/interstitial
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("hidden");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

        requestNewInterstitial();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Inserting main menu fragment
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new MainFragment()).addToBackStack("Main").commit();


        //Initialising navigation drawer and adding listener for user choice
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View navView = navigationView.getHeaderView(0);
        navName = (TextView)navView.findViewById(R.id.navDrawerName);
        navEmail = (TextView)navView.findViewById(R.id.navDrawerEmail);

        myDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User pulledUser = dataSnapshot.getValue(User.class);
                        navEmail.setText(pulledUser.userEmail);
                        navName.setText(pulledUser.userName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        navPic = (CircleImageView)navView.findViewById(R.id.profile_image);
        if (user.getPhotoUrl() == null) {
            navPic.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_menu_camera));
        } else {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .into(navPic);
        }
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        //android.app.Fragment check = fragmentManager.findFragmentByTag("MainFragment");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        String backStack = null;
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 0) {
            FragmentManager.BackStackEntry fragment = fragmentManager.getBackStackEntryAt(count - 1);
            backStack = fragment.getName();
            Log.v("MainActivity", "name from backstack: "+backStack);
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(backStack == "Game"){
            Account.whichWeek = null;
            whichWeek = null;
            Account.temp = null;
            temp = null;
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new MainFragment()).addToBackStack("Main").commit();
        }else if(backStack != "Main"){
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new MainFragment()).addToBackStack("Main").commit();
        }else{
            finishAffinity(); //finishes all activities for user, finish() left Login activity alive,
        }                     //which brought user back to MainFragment if logged in.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //Adding search feature to the action bar

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(this.getString(R.string.action_search));

        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.tw__solid_white));
        searchView.setOnQueryTextListener(OnQuerySearchView);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
       ;

        //searchView.setVisibility(View.INVISIBLE);
        menu.findItem(R.id.action_search).setVisible(false);


        return true;

    }

    //Listener for the search feature text input
    private SearchView.OnQueryTextListener OnQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            searchDatabase(query);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {


            //Toast.makeText(getBaseContext(), ""+Account.gameList.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    };


    public void searchDatabase(final String string){


        Query query = myDatabase.child("games").child(user.getUid()).child("GameWeek"+string);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                                     final Results thisGame = dataSnapshot.getValue(Results.class);
                                                     if(thisGame == null){
                                                         Toast.makeText(MainActivity.this, "No matching record found.", Toast.LENGTH_SHORT).show();
                                                     }else {
                                                         AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                         builder.setTitle("Your choices for Game Week " + string + " :");
                                                         builder.setMessage("Selection 01: " + thisGame.game01 + "\nSelection 02: " +
                                                                 thisGame.game02 + "\nSelection 03: " + thisGame.game03 + "\nSelection 04: " +
                                                                 thisGame.game04 + "\nSelection 05: " + thisGame.game05 + "\nSelection 06: " +
                                                                 thisGame.game06 + "\nSelection 07: " + thisGame.game07 + "\nSelection 08: " +
                                                                 thisGame.game08 + "\nSelection 09: " + thisGame.game09 + "\nSelection 10: " +
                                                                 thisGame.game10);

                                                         builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                                 myDatabase.child("games").child(user.getUid()).child("GameWeek" + string).removeValue();
                                                                 //arrayAdapter.notifyDataSetChanged();
                                                                 FragmentManager fragmentManager = getFragmentManager();
                                                                 fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Account()).addToBackStack("Account").commit();
                                                                 Toast.makeText(getBaseContext(), "Entry removed.", Toast.LENGTH_LONG).show();

                                                             }
                                                         });

                                                         builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                                 temp = thisGame;
                                                                 whichWeek = "GameWeek" + string;
                                                                 getFragmentManager().beginTransaction().replace(R.id.fragmentFrame, new Game()).addToBackStack("Game").commit();
                                                             }
                                                         });

                                                         builder.show();
                                                     }

                                                 }



                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }

                                             });
    }
    //Listener for the settings (dropdown menu) in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signoutSettingsMenu) {
            Login.signOut();
            Intent goToLogin = new Intent(getApplicationContext(), Login.class);
            startActivity(goToLogin);
            Toast.makeText(getBaseContext(), "Signed out successfully.", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.homeSettingsMenu){
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new MainFragment()).addToBackStack("Main").commit();
        }

        return super.onOptionsItemSelected(item);
    }

    //Handler for users selected choice in navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_play) {
            if(mInterstitialAd.isLoaded()){
                mInterstitialAd.show();     //if the app has an ad ready to go display it, then load game screen
                fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Game()).addToBackStack("Game").commit();
            }else {
                fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Game()).addToBackStack("Game").commit();
            }
        }else if (id == R.id.nav_account) {
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Account()).addToBackStack("Account").commit();
        }else if(id == R.id.nav_leaderboard){
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new Leaderboard()).addToBackStack("Leaderboard").commit();
        }else if (id == R.id.nav_faq) {
            fragmentManager.beginTransaction().replace(R.id.fragmentFrame, new FAQ()).addToBackStack("FAQ").commit();
        }else if (id == R.id.nav_sign_out) {
            Login.signOut();
            Intent goToLogin = new Intent(getApplicationContext(), Login.class);
            startActivity(goToLogin);
            Toast.makeText(getBaseContext(), "Signed out successfully.", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_contact) {
            final SpannableString text = new SpannableString("Send us an email at support@streaker.ie \n" +
                    "Use our contact form at www.streaker.ie/contact\n"+
                    "Terms & Conditions located at www.streaker.ie/tsandcs");
            Linkify.addLinks(text, Linkify.ALL);

            new MaterialDialog.Builder(MainActivity.this)
                    .iconRes(R.mipmap.logo_ball)
                    .limitIconToDefaultSize()
                    .title("Contact Us")
                    .content(text)
                    .positiveText("Got it!")
                    .show();
        } else if (id == R.id.nav_about) {
            final SpannableString text = new SpannableString("Streaker is a project developed by Brian " +
                    "Phillips for Agile Software Development, a module from semester one of Software Systems " +
                    "Development(Yr4). The design document for this project is located at " +
                    "......");
            Linkify.addLinks(text, Linkify.ALL);

            new MaterialDialog.Builder(MainActivity.this)
                    .iconRes(R.mipmap.logo_ball)
                    .limitIconToDefaultSize()
                    .title("About Streaker")
                    .content(text)
                    .positiveText("Got it!")
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "No internet connectivity, please check your connection", Toast.LENGTH_LONG).show();
    }

    //method for requesting an ad from adMob, from same guide as above: https://firebase.google.com/docs/admob/android/interstitial
    private void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("hidden")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }




}
