package app.pictograma.com.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import app.pictograma.com.R;
import app.pictograma.com.clases.Profeccional;
import app.pictograma.com.clases.User;
import app.pictograma.com.config.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Register01Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText txtNombre, txtEmail,txtDireccion,txtBanco,txtSucursal,txtCta,txtBancocc,txtCtacc;
    private FirebaseUser user;

    private static DatabaseReference databaseProfesional;
    private static final String TAG = Register01Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register01);

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


        databaseProfesional = FirebaseDatabase.getInstance().getReference("profesionales");
        databaseProfesional.keepSynced(true);

        txtNombre=(EditText) findViewById(R.id.txtnombre);
        txtEmail=(EditText) findViewById(R.id.txtemail);
        txtDireccion=(EditText) findViewById(R.id.txtdire);
        txtBanco=(EditText) findViewById(R.id.txtbanco);
        txtSucursal=(EditText) findViewById(R.id.txtsurcusal);
        txtCta=(EditText) findViewById(R.id.txtcta);
        txtBancocc=(EditText)findViewById(R.id.txtBancocc);
        txtCtacc=(EditText)findViewById(R.id.txtCtacc);

        user = FirebaseAuth.getInstance().getCurrentUser();

        txtEmail.setText(user.getEmail());
        txtEmail.setEnabled(false);



        Constants.nombre = "";
        Constants.direccion = "";
        Constants.banco = "";
        Constants.sucursal = "";
        Constants.cta = "";
        Constants.tarjeta = "";
        Constants.numtarjeta = "";
        Constants.serviciosOfrece = "";
        Constants.especialidad = "";
        Constants.anios = "";
        Constants.titulos1 = "";
        Constants.titulos2 = "";
        Constants.titulos3 = "";
        Constants.razon1 = "";
        Constants.razon2 = "";
        Constants.razon3 = "";

        Constants.imagen1 = "";
        Constants.imagen2 = "";
        Constants.imagen3 = "";
        Constants.imagen4 = "";
        Constants.imagen5 = "";
        Constants.imagen6 = "";
        Constants.imagen7 = "";
        Constants.imagen8 = "";
        Constants.imagen9 = "";
        Constants.imagen10 = "";



        Query queryuser = databaseProfesional
                .orderByChild("userid").equalTo(user.getUid());


        queryuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting artist


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Profeccional temp = postSnapshot.getValue(Profeccional.class);

                    Constants.nombre = temp.getNombre();
                    Constants.direccion = temp.getDirreccion();
                    Constants.banco = temp.getBanco();
                    Constants.sucursal = temp.getSucursal();
                    Constants.cta = temp.getNum();
                    Constants.tarjeta = temp.getTarjeta();
                    Constants.numtarjeta = temp.getNumtarjeta();
                    Constants.serviciosOfrece = temp.getServicios();
                    Constants.especialidad = temp.getEspecilidad();
                    Constants.anios = temp.getAnio();
                    Constants.titulos1 = temp.getTitulo1();
                    Constants.titulos2 = temp.getTitulo2();
                    Constants.titulos3 = temp.getTitulo3();
                    Constants.razon1 = temp.getRazon1();
                    Constants.razon2 = temp.getRazon2();
                    Constants.razon3 = temp.getRazon3();

                    Constants.imagen1 = temp.getImg1();
                    Constants.imagen2 = temp.getImg2();
                    Constants.imagen3 = temp.getImg3();
                    Constants.imagen4 = temp.getImg4();
                    Constants.imagen5 = temp.getImg5();
                    Constants.imagen6 = temp.getImg6();
                    Constants.imagen7 = temp.getImg7();
                    Constants.imagen8 = temp.getImg8();
                    Constants.imagen9 = temp.getImg9();
                    Constants.imagen10 = temp.getImg10();



                    llenar();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });












    }

    public void llenar()
    {
        if(!Constants.nombre.equals(""))
        {
            txtNombre.setText(Constants.nombre);
        }
        if(!Constants.direccion.equals(""))
        {
            txtDireccion.setText(Constants.direccion);
        }
        if(!Constants.banco.equals(""))
        {
            txtBanco.setText(Constants.banco);
        }
        if(!Constants.sucursal.equals(""))
        {
            txtSucursal.setText(Constants.sucursal);
        }
        if(!Constants.cta.equals(""))
        {
            txtCta.setText(Constants.cta);
        }

        if(!Constants.tarjeta.equals(""))
        {
            txtBancocc.setText(Constants.tarjeta);
        }

        if(!Constants.numtarjeta.equals(""))
        {
            txtCtacc.setText(Constants.numtarjeta);
        }
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
                onBackPressed();
                finish();
                //------------
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void siguiente(View v)
    {
       if(txtNombre.getText().toString().equals(""))
       {
           txtNombre.setError(getString(R.string.error_reg_name));
           return;
       }

        if(txtEmail.getText().toString().equals(""))
        {
            txtEmail.setError(getString(R.string.error_reg_email));
            return;
        }

        if(txtDireccion.getText().toString().equals(""))
        {
            txtDireccion.setError(getString(R.string.error_reg_direcccion));
            return;
        }

        if(txtBanco.getText().toString().equals(""))
        {
            txtBanco.setError(getString(R.string.error_reg_banco));
            return;
        }

        if(txtSucursal.getText().toString().equals(""))
        {
            txtSucursal.setError(getString(R.string.error_reg_sucursal));
            return;
        }
        if(txtCta.getText().toString().equals(""))
        {
            txtCta.setError(getString(R.string.error_reg_cta));
            return;
        }


        Intent intent = new Intent(Register01Activity.this,Register02Activity.class);

        Constants.nombre= txtNombre.getText().toString();
        Constants.direccion= txtDireccion.getText().toString();
        Constants.banco= txtBanco.getText().toString();
        Constants.sucursal= txtSucursal.getText().toString();
        Constants.cta= txtCta.getText().toString();
        Constants.tarjeta= txtBancocc.getText().toString();
        Constants.numtarjeta= txtCtacc.getText().toString();

        startActivity(intent);
        finish();



    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
