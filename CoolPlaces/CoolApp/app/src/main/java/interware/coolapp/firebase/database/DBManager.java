package interware.coolapp.firebase.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by chelixpreciado on 7/19/16.
 */
public class DBManager {

    public static final String dbTagPrefs = "DB_TAG_PREFS";
    private final String _is_first_time_in = "_is_first_time_in";

    private SharedPreferences manager;
    private SharedPreferences.Editor editor;
    private DatabaseReference mDataBase;

    public DBManager(Context context) {
        manager = context.getSharedPreferences(dbTagPrefs, 0);
        editor = manager.edit();
    }

    public DatabaseReference getmDataBase() {
        if (mDataBase==null){
            if (isFirstTimeIn()){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                setFirstTimeIn(false);
            }
            mDataBase = FirebaseDatabase.getInstance().getReference();
        }
        return mDataBase;
    }

    public void setFirstTimeIn(boolean firstTimeIn){
        editor.putBoolean(_is_first_time_in, firstTimeIn);
        editor.commit();
    }

    public boolean isFirstTimeIn(){
        return manager.getBoolean(_is_first_time_in, true);
    }
}
