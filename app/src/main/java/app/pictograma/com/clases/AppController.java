package app.pictograma.com.clases;

/**
 * Created by darco on 25/07/2016.
 */

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;


import com.google.firebase.database.FirebaseDatabase;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();


    private static AppController mInstance;

    private static FirebaseDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);

        }

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }







}
