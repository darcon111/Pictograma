package app.pictograma.com.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import app.pictograma.com.R;
import app.pictograma.com.clases.Alert;
import app.pictograma.com.clases.User;
import app.pictograma.com.config.AppPreferences;
import app.pictograma.com.config.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private static String imagen,name;

    /***************************************
     *              TWITTER                *
     ***************************************/
    public static final int RC_TWITTER_LOGIN = 2;
    private TwitterLoginButton mLoginTwitter;



    private LoginButton loginFacebook;

    private ImageView btnTwitter,btnFacebook;

    private static FirebaseUser user;
    private static String provider;
    private static AppPreferences app;
    private static User Utemp;
    private static DatabaseReference databaseUsers;
    private static List<User> mListUser;
    private ValueEventListener listen;
    private ConstraintLayout main;

    private SweetAlertDialog pDialog;

    //data facebook
    private static String appname="",applastname="";
    private static String appbirthday="",appgenero="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* *************************************
         *                TWITTER              *
         ***************************************/
        // Configure Twitter SDK
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);

        /* *************************************
         *                FACEBOOK             *
         ***************************************/

        FacebookSdk.sdkInitialize(getApplicationContext());



        AppEventsLogger.activateApp(this);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        main=(ConstraintLayout) findViewById(R.id.main);

        app = new AppPreferences(getApplicationContext());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mListUser = new ArrayList<User>();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);


        // ...
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out" );

                }
                // ...
            }
        };

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton) findViewById(R.id.button_facebook_login);
        loginFacebook.setReadPermissions("email", "public_profile","user_friends","user_birthday");
        /*loginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });*/

        btnFacebook = (ImageView) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook.performClick();
                loginFacebook.setPressed(true);
                loginFacebook.invalidate();
                loginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {


                                            if (Constants.isHasJson(object, "gender")) {
                                                if (object.getString("gender").equals("male")) {
                                                    appgenero = "1";
                                                } else {
                                                    appgenero = "2";
                                                }
                                            }

                                            if (Constants.isHasJson(object, "birthday")) {
                                                appbirthday = object.getString("birthday"); // 01/31/1980 format
                                            }

                                            if (Constants.isHasJson(object, "name")) {
                                                String[] name = object.getString("name").split(" ");

                                                appname = name[0];
                                                applastname = name[1];
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        handleFacebookAccessToken(loginResult.getAccessToken());

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
                loginFacebook.setPressed(false);
                loginFacebook.invalidate();

            }
        });




        // Initialize Twitter Login button
        mLoginTwitter = findViewById(R.id.button_twitter_login);
        mLoginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);


                handleTwitterSession(result.data.getAuthToken().token,result.data.getAuthToken().secret);

            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure "+ exception.getMessage().toString());

            }
        });


        btnTwitter = (ImageView) findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginTwitter.performClick();
                mLoginTwitter.setPressed(true);
                mLoginTwitter.invalidate();


            }
        });



           /* App permissions */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);



        }







        listen= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                mListUser.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    User temp = postSnapshot.getValue(User.class);
                    //adding artist to the list

                    mListUser.add(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseUsers.addValueEventListener(listen);



    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        mAuth.addAuthStateListener(mAuthListener);



    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void auth(String token)
    {


            mAuth.signInWithCustomToken(token)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                Log.d(TAG, "error Login :" + task.getException().toString());
                                pDialog.dismiss();



                                pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText(getResources().getString(R.string.app_name));
                                pDialog.setContentText(getResources().getString(R.string.error_user));
                                pDialog.setConfirmText(getResources().getString(R.string.ok));
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        LoginManager.getInstance().logOut();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                });
                                pDialog.show();



                            } else {
                                insertUser("");
                                databaseUsers.removeEventListener(listen);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });





    }







    public static void insertUser(String imagen)
    {



        user = FirebaseAuth.getInstance().getCurrentUser();

        if(mListUser.size()>0)
        {
            for(int i=0;i<mListUser.size();i++)
            {
                if(mListUser.get(i).getFirebaseId().equals(user.getUid()))
                {
                    Utemp=mListUser.get(i);
                }
            }
        }


        if(Utemp==null)
        {
            if (user != null) {
                List<String> listProvider =user.getProviders();
                provider=listProvider.get(0);
                // User is signed in
                if(user.getPhotoUrl()!=null) {
                    imagen = user.getPhotoUrl().toString();
                }

                try{
                    name=user.getEmail().toString();
                }catch (Exception e)
                {
                    if(user.getDisplayName()!=null) {
                        name = user.getDisplayName().toString();
                    }
                }


                if(imagen==null){imagen="";}
                if(name==null){name="";}

                String id = databaseUsers.push().getKey();
                User data = new User();
                data.setId(id);
                data.setFirebaseId(user.getUid());
                data.setEmail(user.getEmail());
                if(imagen==null){
                    imagen="";
                }
                data.setUrl_imagen(imagen);
                data.setName(appname);
                data.setLastname(applastname);
                data.setFecha_nac(appbirthday);
                data.setGenero(appgenero);
                data.setType("1");
                data.setLat("0");
                data.setLog("0");
                data.setFirebase_code("");
                Date date = new Date();
                data.setMobile("1");
                data.setDate_created(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date));
                data.setProvider(provider);

                //Saving
                databaseUsers.child(id).setValue(data);
                app.setUserId(id);


            }
        }else
        {
            app.setUserId(Utemp.getId());
            if(!imagen.equals("")) {
                databaseUsers.child(Utemp.getId()).child("url_imagen").setValue(imagen);


                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(imagen))
                        .build();

                user.updateProfile(profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });

            }


        }









    }

    public void loginEmail(View v)
    {
        Intent intent = new Intent(LoginActivity.this, LoginEmailActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, "Permission Granted, Now you can access location data.");
                    restartActivity(this);

                } else {
                    Log.e(TAG, "Permission Denied, You cannot access location data.");


                    pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText(getResources().getString(R.string.app_name));
                    pDialog.setContentText(getResources().getString(R.string.permissions));
                    pDialog.setConfirmText(getResources().getString(R.string.ok));
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            finish();
                        }
                    });
                    pDialog.show();



                }
                break;
        }
    }

    public static void restartActivity(Activity actividad) {
        Intent intent = new Intent();
        intent.setClass(actividad, actividad.getClass());
        actividad.startActivity(intent);
        actividad.finish();
    }




    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            auth(token.getToken().toString());
                        }else
                        {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()==false) {
                                user.sendEmailVerification();



                                pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText(getResources().getString(R.string.app_name));
                                pDialog.setContentText(getResources().getString(R.string.user_create));
                                pDialog.setConfirmText(getResources().getString(R.string.ok));
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        LoginManager.getInstance().logOut();
                                    }
                                });
                                pDialog.show();



                            }else {
                                String imagen ="https://graph.facebook.com/"+token.getUserId()+"/picture?type=large";
                                insertUser(imagen);
                                databaseUsers.removeEventListener(listen);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.

        if(data!=null) {
            mLoginTwitter.onActivityResult(requestCode, resultCode, data);
        }else
        {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_TWITTER_LOGIN) {
            if(data!=null) {
                loginTwitter(data.getStringExtra("oauth_token"), data.getStringExtra("oauth_token_secret"));
            }
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }*/


    /* ************************************
   *               TWITTER              *
   **************************************
   */


    private void handleTwitterSession(final String token, final String secret)
    {


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(getString(R.string.colorAccent)));
        pDialog.setTitleText(getResources().getString(R.string.auth));
        pDialog.setCancelable(true);
        pDialog.show();



        AuthCredential credential = TwitterAuthProvider.getCredential(token, secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            pDialog.dismiss();


                            pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                            pDialog.setTitleText(getResources().getString(R.string.app_name));
                            pDialog.setContentText(getResources().getString(R.string.error_user));
                            pDialog.setConfirmText(getResources().getString(R.string.ok));
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    LoginManager.getInstance().logOut();
                                }
                            });
                            pDialog.show();




                            return;
                        }

                        if (task.isSuccessful()) {

                            pDialog.dismiss();

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()==false) {
                                user.sendEmailVerification();



                                pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText(getResources().getString(R.string.app_name));
                                pDialog.setContentText(getResources().getString(R.string.user_create));
                                pDialog.setConfirmText(getResources().getString(R.string.ok));
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        LoginManager.getInstance().logOut();
                                    }
                                });
                                pDialog.show();


                            }else {
                                pDialog.dismiss();
                                insertUser("");
                                databaseUsers.removeEventListener(listen);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pDialog.dismiss();
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }




    public void restablecer(View view)
    {

        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(getResources().getString(R.string.app_name));
        pDialog.setContentText(getResources().getString(R.string.restabler_msg));
        pDialog.setConfirmText(getResources().getString(R.string.ok));
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                recuperar();
            }
        });
        pDialog.show();


    }


    private void recuperar()
    {
        final Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v=getLayoutInflater().inflate(R.layout.item_restablecer
                , null);

        Button btnclose=(Button) v.findViewById(R.id.close);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss();
            }
        });

        final EditText txtemail=(EditText) v.findViewById(R.id.txtemail);

        Button btnsend=(Button) v.findViewById(R.id.send);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!txtemail.getText().toString().equals("")) {
                    mAuth.sendPasswordResetEmail(txtemail.getText().toString());

                    settingsDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(main, "Por favor revise su email!!!", Snackbar.LENGTH_LONG);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else
                {
                    txtemail.setError(getString(R.string.error_mail));
                }

            }
        });

        settingsDialog.setContentView(v);
        settingsDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(settingsDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        settingsDialog.show();
        settingsDialog.getWindow().setAttributes(lp);
    }




}
