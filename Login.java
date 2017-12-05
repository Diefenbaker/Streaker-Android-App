package ie.wit.streaker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import ie.wit.streaker.R;
import ie.wit.streaker.activities.models.User;
import io.fabric.sdk.android.Fabric;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    public static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private LoginButton fb_login_button;
    CallbackManager callbackManager;
    private TwitterLoginButton tw_login_button;
    private DatabaseReference myDatabase;
    private ProgressBar loginProgress, twitterProgress;
    ImageView gifView;
    public static SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));

        myDatabase = FirebaseDatabase.getInstance().getReference();


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);

        gifView = (ImageView) findViewById(R.id.gifView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifView);
        Glide.with(this).load("https://media.giphy.com/media/OdQ87wjlghO2A/giphy.gif").into(imageViewTarget);

        tw_login_button = (TwitterLoginButton) findViewById(R.id.twitter_button);
        tw_login_button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.v("Login", "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.v("Login", "twitterLogin:failure", exception);

            }
        });


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("Login", "FB already logged in");

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        fb_login_button = (LoginButton) findViewById(R.id.fb_login_button);
        fb_login_button.setReadPermissions("email");

        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(""));
                handleFaceBookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.v("Login", "facebook:onCancel");
                Toast.makeText(Login.this, "FB Login Cancel.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("Login", "facebook:onError", error);
                Toast.makeText(Login.this, "FB Login Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sign_in_button).setOnClickListener(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Log.v("Login", "onAuthStateChanged:signed_in: " + user.getUid());

                } else {

                    Log.v("Login", "onAuthStateChanged:signed_out");

                }
            }
        };


    }


    @Override
    public void onStart() {

        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);

        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {

        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        if (mAuthListener != null) {

            mAuth.removeAuthStateListener(mAuthListener);

        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }


    private void handleTwitterSession(final TwitterSession session){

        Log.v("Login", "handleTwitterSession: " + session);

        final AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("Login", "signInWithCredential:onComplete: " +task.isSuccessful());

                        if(task.isSuccessful()){
                            loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
                            loginProgress.setVisibility(View.VISIBLE);
                            loginProgress.setEnabled(true);
                            loginProgress.animate();
                            onAuthSuccess();
                        }

                        if (!task.isSuccessful()){
                            Log.v("Login", "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){

        Log.v("Login", "firebaseWithGoogle: "+acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("Login", "signInWithCredential:onComplete: "+task.isSuccessful());

                        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
                        loginProgress.setVisibility(View.VISIBLE);
                        loginProgress.setEnabled(true);
                        loginProgress.animate();
                        onAuthSuccess();

                        if(!task.isSuccessful()){

                            Log.v("Login", "signinWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(Login.this, "Connection error: " +connectionResult, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sign_in_button:
                signIn();
                break;

        }
    }


    private void signIn(){

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public static void signOut(){

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                }
        );

        FirebaseAuth.getInstance().signOut();

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback(){


            @Override
            public void onCompleted(GraphResponse response) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();

        mAuth.signOut();
        Twitter.logOut();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        tw_login_button.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){

                GoogleSignInAccount acct = result.getSignInAccount();
                firebaseAuthWithGoogle(acct);


            }else {


            }

        }
    }


    private void handleFaceBookAccessToken(final AccessToken token){

        Log.v("Login", "handleFacebookAccessToken: "+token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()){
                            Log.v("Login", "signInWithCredential:onComplete: "+ task.isSuccessful());
                            Log.v("Login", "signInWithCredential:onComplete: ", task.getException());
                            loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
                            loginProgress.setVisibility(View.VISIBLE);
                            loginProgress.setEnabled(true);
                            loginProgress.animate();
                            onAuthSuccess();
                        }

                    }
                });


    }


    public void onAuthSuccess(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String userName = user.getDisplayName();
        final String userId = user.getUid();
        final String email = user.getEmail();

        myDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Log.v("Login", "User found");
                            Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goToMain);
                        }
                        else{
                            Log.v("Login", "User not found");
                            int userWinsInitialise = 0;
                            User newUser = new User(userId, userName, email, userWinsInitialise);
                            myDatabase.child("users").child(userId).setValue(newUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
