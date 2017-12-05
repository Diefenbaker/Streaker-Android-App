package ie.wit.streaker.activities.models;

/**
 * Created by flyin on 31/10/2016.
 */

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userName;
    public String userId;
    public String userEmail;
    public int userWins;
    public Uri profilePic;


    public User(){

        // Default constructor required for calls to DataSnapshot.getValue(User.class)

    }


    public User(String userId, String userName, String userEmail, int userWins){

        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userWins = userWins;
       // this.profilePic = profilePic; Hitting Binder issues when this is active, according to Stack Overflow it's an Android issue, not Firebase

    }


}
