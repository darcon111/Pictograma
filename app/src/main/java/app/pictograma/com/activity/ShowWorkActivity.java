package app.pictograma.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;

import app.pictograma.com.R;
import app.pictograma.com.adapter.TourFragmentPagerAdapter;
import app.pictograma.com.clases.ImagenCircular.CircleImageView;
import app.pictograma.com.clases.Profeccional;
import app.pictograma.com.clases.User;
import app.pictograma.com.config.Constants;


public class ShowWorkActivity extends FragmentActivity {

    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;
    private TextView txtNombre,txtDireccion,txtAnios,txtHabilidades,txtCertificados,txtCualidades;
    private Toolbar toolbar;
    private static final String TAG = ShowWorkActivity.class.getSimpleName();
    private static DatabaseReference databaseProfesional;
    private static FirebaseUser user;
    private CircleImageView img;
    private ArrayList<String> select;
    private static DatabaseReference databaseUsers;


    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    private ViewPager pager = null;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private TourFragmentPagerAdapter pagerAdapter;
    // Create an adapter with the fragments we show on the ViewPager
    private TourFragmentPagerAdapter adapter;
    private PageIndicatorView pageIndicatorView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_show_work);

        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pager);

        pageIndicatorView = (PageIndicatorView) this.findViewById(R.id.pageIndicatorView);


        /* toolbar*/
        /*toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
        }*/



        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);
        img4=(ImageView) findViewById(R.id.img4);
        img5=(ImageView) findViewById(R.id.img5);
        img6=(ImageView) findViewById(R.id.img6);
        img7=(ImageView) findViewById(R.id.img7);
        img8=(ImageView) findViewById(R.id.img8);
        img9=(ImageView) findViewById(R.id.img9);
        img10=(ImageView) findViewById(R.id.img10);

        txtNombre=(TextView) findViewById(R.id.txtNombre);
        txtDireccion=(TextView) findViewById(R.id.txtDireccion);
        txtAnios=(TextView) findViewById(R.id.txtAnios);
        txtHabilidades=(TextView) findViewById(R.id.txtHabilidades);
        txtCertificados=(TextView) findViewById(R.id.txtCertificados);
        txtCualidades=(TextView) findViewById(R.id.txtCualidades);

        img=(CircleImageView) findViewById(R.id.imgUser);


        Bundle extras = getIntent().getExtras();
        String busqueda = extras.getString("id");
        String busqueda2 = extras.getString("userid");



        // Initialize Firebase Auth
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);

        Query queryusers = databaseUsers
                .orderByChild("firebaseId").equalTo(busqueda2);


        queryusers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    User temp = postSnapshot.getValue(User.class);

                    if(!temp.getUrl_imagen().equals(""))
                    {
                        img.setImageBitmap(Constants.decodeBase64(temp.getUrl_imagen()));
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        databaseProfesional = FirebaseDatabase.getInstance().getReference("profesionales");
        Query querypro = databaseProfesional
                .orderByChild("id").equalTo(busqueda);


        querypro.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting artist


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Profeccional temp = postSnapshot.getValue(Profeccional.class);

                    databaseProfesional.removeEventListener(this);

                    select = new ArrayList<String>();
                    //llenar info
                    String[] servicios =temp.getServicios().split(",");

                    for (int x=0;x<servicios.length;x++)
                    {
                        select.add(Constants.getServicios().get(Integer.parseInt(servicios[x])));
                    }


                    txtNombre.setText(temp.getNombre());
                    txtDireccion.setText(temp.getDirreccion());
                    txtAnios.setText(select.get(Integer.parseInt(temp.getEspecilidad()))+" con "+temp.getAnio()+" años de experiencia");
                    String habilidades="",certificados="",razones="";
                    for(int i=0;i<select.size();i++)
                    {
                        if(i==select.size()-2)
                        {
                            habilidades += select.get(i) + " y ";
                        }else {
                            habilidades += select.get(i) + ",";
                        }
                    }
                    txtHabilidades.setText("Especialidad en "+habilidades.substring(0, habilidades.length()-1));

                    certificados=temp.getTitulo1();
                    if(!temp.getTitulo2().equals(""))
                    {
                        certificados+=","+temp.getTitulo2();
                    }
                    if(!temp.getTitulo3().equals(""))
                    {
                        certificados+=","+temp.getTitulo3();
                    }
                    txtCertificados.setText(certificados);
                    razones=temp.getRazon1();
                    if(!temp.getRazon2().equals(""))
                    {
                        razones+="\n\r"+temp.getRazon2();
                    }
                    if(!temp.getRazon3().equals(""))
                    {
                        razones+=","+temp.getRazon3();
                    }
                    txtCualidades.setText(razones);

                    if(!temp.getImg1().equals(""))
                    {
                        img1.setImageBitmap(Constants.decodeBase64(temp.getImg1()));
                    }else
                    {
                        img1.setVisibility(View.GONE);
                    }

                    int Contador=0;

                    // Create an adapter with the fragments we show on the ViewPager
                    adapter = new TourFragmentPagerAdapter(
                            getSupportFragmentManager());

                    if(!temp.getImg1().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg1(),Contador++));
                    }
                    if(!temp.getImg2().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg2(),Contador++));
                    }
                    if(!temp.getImg3().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg3(),Contador++));
                    }
                    if(!temp.getImg4().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg4(),Contador++));
                    }
                    if(!temp.getImg5().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg5(),Contador++));
                    }
                    if(!temp.getImg6().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg6(),Contador++));
                    }
                    if(!temp.getImg7().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg7(),Contador++));
                    }
                    if(!temp.getImg8().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg8(),Contador++));
                    }
                    if(!temp.getImg9().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg9(),Contador++));
                    }
                    if(!temp.getImg10().equals(""))
                    {
                        adapter.addFragment(ImagenSlidePageFragment.newInstance(temp.getImg10(),Contador++));
                    }

                    pager.setAdapter(adapter);

                    pageIndicatorView.setViewPager(pager);

                    pageIndicatorView.setInteractiveAnimation(true);
                    pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);

                    pageIndicatorView.setCount(Contador);





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });



    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                finish();
                //------------
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
