package ie.wit.streaker.activities.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by flyin on 08/12/2016.
 */
@IgnoreExtraProperties
public class History {

    public String game01, game02, game03, game04, game05, game06, game07, game08, game09, game10, winner;

    public History() {

    }

    public History(String game01, String game02, String game03,
                   String game04, String game05, String game06, String game07,
                   String game08, String game09, String game10, String winner) {
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
        this.winner = winner;

    }

}
