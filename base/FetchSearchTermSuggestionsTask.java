package ie.wit.streaker.activities.base;

import android.app.SearchManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static ie.wit.streaker.activities.MainActivity.searchView;

/**
 *This class was to be used with the SearchView feature in ActionBar,
 * more trouble that it's worth so close to deadline.
 */

public class FetchSearchTermSuggestionsTask extends AsyncTask<String, Void, Cursor> {

    FirebaseUser user;
    ListView gameList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<>();

    private DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();

    private static final String[] sAutoCompleteColumnNames = new String[]{
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1

    };

    @Override
    protected Cursor doInBackground(String... strings) {
        final MatrixCursor cursor = new MatrixCursor(sAutoCompleteColumnNames);


        Query gameHistory = myDatabase.child("games").child(user.getUid());
        gameHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot gameHistory : dataSnapshot.getChildren()){
                    Log.v("Account", gameHistory.getKey());
                    String data = gameHistory.getKey();
                    Object[] row = new Object[]{data};
                    cursor.addRow(row);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return cursor;
    }


    @Override
    protected void onPostExecute(Cursor cursor){
        searchView.getSuggestionsAdapter().changeCursor(cursor);
    }
}
