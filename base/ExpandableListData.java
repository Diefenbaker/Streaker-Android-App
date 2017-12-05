package ie.wit.streaker.activities.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by flyin on 24/10/2016.
 */

public class ExpandableListData {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> howToPlay = new ArrayList<String>();
        howToPlay.add("---> Play Streaker");

        List<String> accountAndGameHistory = new ArrayList<String>();
        accountAndGameHistory.add("---> Acount");
        accountAndGameHistory.add("---> Game History");

        List<String> howWePay = new ArrayList<String>();
        howWePay.add("---> How We Pay");

        expandableListDetail.put("1) How To Play", howToPlay);
        expandableListDetail.put("2) Account & Game History", accountAndGameHistory);
        expandableListDetail.put("3) How We Pay", howWePay);
        return expandableListDetail;
    }
}
