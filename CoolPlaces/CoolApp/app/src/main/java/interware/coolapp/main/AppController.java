package interware.coolapp.main;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by chelixpreciado on 7/13/16.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
