package app.pictograma.com.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.pictograma.com.R;
import app.pictograma.com.clases.ImagenCircular.CircleImageView;
import app.pictograma.com.clases.Profeccional;
import app.pictograma.com.clases.User;
import app.pictograma.com.config.Constants;
import app.pictograma.com.helpers.UtilHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContratarActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView txtNombre,txtDireccion,txtAnios,txtHabilidades,txtCertificados,txtCualidades;
    private CircleImageView img;
    private ArrayList<String> select;

    private static DatabaseReference databaseProfesional;
    private ValueEventListener listen;

    private ArrayList<Profeccional> mListProfeccional;
    private ArrayList<Profeccional> mListProfeccionalSelect;

    private ArrayList<User> mListUser;


    private RecyclerView mProRecyclerView;

    private ProfeccionalRecycleAdapter mProAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_contratar2);


        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
        }

        img=(CircleImageView) findViewById(R.id.imgUser);

        txtNombre=(TextView) findViewById(R.id.txtNombre);
        txtDireccion=(TextView) findViewById(R.id.txtDireccion);
        txtAnios=(TextView) findViewById(R.id.txtAnios);
        txtHabilidades=(TextView) findViewById(R.id.txtHabilidades);
        txtCertificados=(TextView) findViewById(R.id.txtCertificados);
        txtCualidades=(TextView) findViewById(R.id.txtCualidades);

        databaseProfesional = FirebaseDatabase.getInstance().getReference("profesionales");
        databaseProfesional.keepSynced(true);


        mProRecyclerView = (RecyclerView) findViewById(R.id.pro);
        // Create a grid layout with two columns

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        // Create a custom SpanSizeLookup where the first item spans both columns


        mProRecyclerView.setLayoutManager(layoutManager);

        mProAdapter = new ProfeccionalRecycleAdapter();
        mProRecyclerView.setAdapter(mProAdapter);





        Bundle extras = getIntent().getExtras();
        String busqueda = extras.getString("id");

        mListProfeccional = new ArrayList<Profeccional>();
        mListProfeccionalSelect = new ArrayList<Profeccional>();
        mListUser = new ArrayList<User>();

        listen= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                mListProfeccional.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Profeccional temp = postSnapshot.getValue(Profeccional.class);

                    if(temp.getServicios().contains(busqueda)){

                        mListProfeccionalSelect.add(temp);
                    }


                    //mListProfeccional.add(temp);
                }

                if(mListProfeccionalSelect.size()>0){

                    mProAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseProfesional.addValueEventListener(listen);

        /*for (int x=0; x<mListProfeccional.size();x++){

            if(mListProfeccional.get(x).getServicios().contains(busqueda)){

                mListProfeccionalSelect.add(mListProfeccional.get())
            }

        }*/




        /*Query queryuser = databaseProfesional
                .orderByChild("userid").equalTo(user.getUid());


        queryuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting artist


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Profeccional temp = postSnapshot.getValue(Profeccional.class);



                    llenar();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });*/




        /*select = new ArrayList<String>();

        String[] servicios = Constants.serviciosOfrece.split(",");

        for (int x=0;x<servicios.length;x++)
        {
            select.add(Constants.getServicios().get(Integer.parseInt(servicios[x])));
        }




        txtNombre.setText(Constants.nombre);
        txtDireccion.setText(Constants.direccion);
        txtAnios.setText(select.get(Integer.parseInt(Constants.especialidad))+" con "+Constants.anios+" años de experiencia");
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

        certificados=Constants.titulos1;
        if(!Constants.titulos2.equals(""))
        {
            certificados+=","+Constants.titulos2;
        }
        if(!Constants.titulos3.equals(""))
        {
            certificados+=","+Constants.titulos3;
        }
        txtCertificados.setText(certificados);
        razones=Constants.razon1;
        if(!Constants.razon2.equals(""))
        {
            razones+="\n\r"+Constants.razon2;
        }
        if(!Constants.razon3.equals(""))
        {
            razones+=","+Constants.razon3;
        }
        txtCualidades.setText(razones);*/



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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


    /* adapter*/

    public class ProfeccionalRecycleAdapter extends RecyclerView.Adapter<ProfeccionalRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public ProfeccionalRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pro, viewGroup, false);
            return new ProfeccionalRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final ProfeccionalRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListProfeccionalSelect.get(i).getNombre());


            if(!mListProfeccionalSelect.get(i).getImgPerfil().equals("")){

                productHolder.mImage.setImageBitmap(Constants.decodeBase64(mListProfeccionalSelect.get(i).getImgPerfil()));

            }

            //productHolder.mImage.setImageResource(mListServicies.get(i).getImagen());


            /*productHolder.mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(MainActivity.this,ContratarActivity.class);
                    intent.putExtra("id",String.valueOf(i));
                    startActivity(intent);


                }
            });*/


        }


        @Override
        public int getItemCount() {
            return mListProfeccionalSelect.size();
        }

        public void removeItem(int position) {
            mListProfeccionalSelect.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mListProfeccionalSelect.size());
            //Signal.get().reset();


        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation;
                if (position % 2 == 0) {
                    animation = AnimationUtils.loadAnimation(ContratarActivity.this, R.anim.left_in);
                } else {
                    animation = AnimationUtils.loadAnimation(ContratarActivity.this, R.anim.right_in);
                }

                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }


    }

    public class ProfeccionalRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public CircleImageView mImage;


        public ProfeccionalRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txttitle);
            mImage = (CircleImageView) itemView.findViewById(R.id.imagen);
        }
    }

}
