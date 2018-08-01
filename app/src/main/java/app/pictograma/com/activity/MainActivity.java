package app.pictograma.com.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.pictograma.com.R;
import app.pictograma.com.adapter.MenuAdapter;
import app.pictograma.com.clases.Alert;
import app.pictograma.com.clases.Profeccional;
import app.pictograma.com.clases.Servicies;
import app.pictograma.com.clases.User;
import app.pictograma.com.config.AppPreferences;
import app.pictograma.com.config.Constants;
import app.pictograma.com.helpers.HidingScrollListener;
import app.pictograma.com.helpers.UtilHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class MainActivity extends AppCompatActivity {

    private int PROFILE = R.drawable.ic_email;
    private RecyclerView mRecyclerView_menu, mServiciesRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout Drawer;
    private String TAG = MainActivity.class.getName();
    private AppPreferences app;
    private String[] TITLES = new String[4];
    private int[] ICONS = new int[4];
    private ActionBarDrawerToggle mDrawerToggle;
    private String imagen, name="Usuario";
    private static FirebaseDatabase mDatabase;

    private Alert message;
    private ServiciesRecycleAdapter mServiciesAdapter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseUsers;
    private Toolbar toolbar;
    private FrameLayout principal;
    private ArrayList<Servicies> mListServicies;

    private User temp=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* set orientation*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        FacebookSdk.sdkInitialize(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        app = new AppPreferences(getApplicationContext());
        /* set language*/
        Constants.setLanguage(app.getLanguage(), getApplicationContext());

        if (mDatabase == null) {
            try {
                mDatabase = FirebaseDatabase.getInstance();
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            } catch (DatabaseException e) {

            }

        }
        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_menu));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu));
        }

        /*main menu*/
        mRecyclerView_menu = (RecyclerView) findViewById(R.id.RecyclerView_main); // Assigning the RecyclerView Object to the xml View
        mRecyclerView_menu.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size




            /* menu main*/
        TITLES[0] = getString(R.string.join);
        TITLES[1] = getString(R.string.help);
        TITLES[2] = getString(R.string.config);
        TITLES[3] = getString(R.string.end);

        ICONS[0] = R.drawable.ic_join;
        ICONS[1] = R.drawable.ic_help;
        ICONS[2] = R.drawable.ic_config;
        ICONS[3] = R.drawable.ic_exit;





        /* data info user*/
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);

        String[] parts = user.getEmail().split("@");
        name = parts[0];

        Query queryuser = databaseUsers
                .orderByChild("firebaseId").equalTo(user.getUid());


        queryuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting artist

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    temp = postSnapshot.getValue(User.class);
                    if(temp.getType().equals("2")) {
                        TITLES[0] = getString(R.string.profesional);
                        ICONS[0] = R.drawable. ic_create_white_24dp;

                    }

                    menu();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });





        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            message = new Alert(MainActivity.this, R.style.AlertDialog);
        }
        else {
            message = new Alert(MainActivity.this);
        }

        if (user.isEmailVerified() == false) {

            message.setMessage(getString(R.string.error_user));
            message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    signOut();
                    finish();
                    message.dismiss();
                }
            });
            message.show();

        }

        auth = FirebaseAuth.getInstance();


        mListServicies = new ArrayList<Servicies>();

        mListServicies.add(new Servicies(1,"Diseño gráfico",R.drawable.ic_diseno));
        mListServicies.add(new Servicies(2,"Imagen Corporativa",R.drawable.ic_imagen));
        mListServicies.add(new Servicies(3,"Conceptos de comunicación",R.drawable.ic_comunicacion));
        mListServicies.add(new Servicies(4,"Sitios web",R.drawable.ic_sitios));
        mListServicies.add(new Servicies(5,"Tienda en internet",R.drawable.ic_tienda));
        mListServicies.add(new Servicies(6,"Publicidad en internet",R.drawable.ic_publicidad));
        mListServicies.add(new Servicies(7,"Creación de artículos",R.drawable.ic_articulos));
        mListServicies.add(new Servicies(8,"Videos",R.drawable.ic_videos));
        mListServicies.add(new Servicies(9,"Fotografía publicitaria",R.drawable.ic_foto));
        mListServicies.add(new Servicies(10,"Logotipos",R.drawable.ic_logo));

        mServiciesRecyclerView = (RecyclerView) findViewById(R.id.servicies_recycler_view);
        // Create a grid layout with two columns

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        // Create a custom SpanSizeLookup where the first item spans both columns
        int paddingTop = UtilHelper.getToolbarHeight(MainActivity.this) + UtilHelper.getTabsHeight(MainActivity.this);
        mServiciesRecyclerView.setPadding(mServiciesRecyclerView.getPaddingLeft(), 150, mServiciesRecyclerView.getPaddingRight(), mServiciesRecyclerView.getPaddingBottom());


        mServiciesRecyclerView.setLayoutManager(layoutManager);

        mServiciesAdapter = new ServiciesRecycleAdapter();
        mServiciesRecyclerView.setAdapter(mServiciesAdapter);
        /*mServiciesRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });*/






        principal = (FrameLayout) findViewById(R.id.principal);

        /*update user */
        if (app.getFlag().equals("1")) {
            databaseUsers.child(app.getUserId()).child("firebase_code").setValue(app.getFirebasetoken());
            app.setFlag("0");
        }




    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        Drawer.closeDrawers();
        super.onBackPressed();
        finish();

    }



    /* adapter*/

    public class ServiciesRecycleAdapter extends RecyclerView.Adapter<ServiciesRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public ServiciesRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_servicies, viewGroup, false);
            return new ServiciesRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final ServiciesRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListServicies.get(i).getName());

            productHolder.mImage.setImageResource(mListServicies.get(i).getImagen());


        }


        @Override
        public int getItemCount() {
            return mListServicies.size();
        }

        public void removeItem(int position) {
            mListServicies.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mListServicies.size());
            //Signal.get().reset();


        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation;
                if (position % 2 == 0) {
                    animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.left_in);
                } else {
                    animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.right_in);
                }

                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }


    }

    public class ServiciesRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mImage;


        public ServiciesRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txttitle);
            mImage = (ImageView) itemView.findViewById(R.id.imagen);
        }
    }

    //sign out method
    public void signOut() {
        // if(provider.equals("facebook.com")) {
        FirebaseAuth.getInstance().signOut();
        //}
        LoginManager.getInstance().logOut();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }



    public void menu()
    {
        mAdapter = new MenuAdapter(TITLES, ICONS, name, PROFILE, imagen, MainActivity.this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)

        mRecyclerView_menu.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView_menu.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made

        Drawer.addDrawerListener(mDrawerToggle);

        mRecyclerView_menu.addOnItemTouchListener(new Constants.RecyclerTouchListener(getApplicationContext(), mRecyclerView_menu, new Constants.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

                Intent intent;

                switch (position) {
                    case 1:

                        if(temp!=null)
                        {
                            if(temp.getType().equals("2"))
                            {
                                intent= new Intent(MainActivity.this,Register01Activity.class);
                            }else
                            {
                                intent= new Intent(MainActivity.this,JoinActivity.class);
                            }
                        }else
                        {
                                intent= new Intent(MainActivity.this,JoinActivity.class);
                        }

                       startActivity(intent);
                        break;
                    case 2:
                        
                        break;
                    case 3:




                        
                        break;
                    case 4:

                        new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle(getString(R.string.app_name))
                                .setMessage(getString(R.string.exit))
                                .setCancelable(false)
                                .setNegativeButton(getString(R.string.no), null)//WITHOUT listener
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signOut();
                                        finish();
                                    }
                                })
                                .show();
                        
                        break;
                    case 5:
                        
                        break;
                    case 6:
                       
                        break;
                    case 7:
                       
                        break;
                    case 8:
                        

                        
                        break;


                    default:
                        //Toast.makeText(vg_activity, "NO WINDOW", Toast.LENGTH_SHORT).show();
                        break;
                }

                mDrawerToggle.onDrawerClosed(mRecyclerView_menu);
                Drawer.closeDrawers();
                return;
            }
        }));
    }






    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        mServiciesRecyclerView.setPadding(mServiciesRecyclerView.getPaddingLeft(), 0, mServiciesRecyclerView.getPaddingRight(), mServiciesRecyclerView.getPaddingBottom());
    }

    private void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

    }

}