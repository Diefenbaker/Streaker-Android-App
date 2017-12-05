package ie.wit.streaker.activities.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by flyin on 31/10/2016.
 */
@IgnoreExtraProperties
public class Results {

    public String gameWeek, game01, game02, game03, game04, game05, game06,
                    game07, game08, game09, game10;

    public Results(){


    }

    public Results(String game01, String game02, String game03,
                   String game04, String game05, String game06, String game07,
                   String game08, String game09, String game10){

        this.game01 = game01;
        this.game02 = game02;
        this.game03 = game03;
        this.game04 = game04;
        this.game05 = game05;
        this.game06 = game06;
        this.game07 = game07;
        this.game08 = game08;
        this.game09 = game09;
        this.game10 = game10;

    }
}
