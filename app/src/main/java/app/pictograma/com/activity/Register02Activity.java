package app.pictograma.com.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import app.pictograma.com.R;
import app.pictograma.com.clases.Alert;
import app.pictograma.com.clases.MultiSpinner;
import app.pictograma.com.config.Constants;


public class Register02Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,MultiSpinner.MultiSpinnerListener {

    private MultiSpinner servicios;
    private Toolbar toolbar;
    private FirebaseUser user;
    private Spinner especialidad;
    private ArrayList<String> list;
    private String itemSelect;
    private ArrayList<String> select;
    private EditText txtAnio,txtTitulo1,txtTitulo2,txtTitulo3,txtRazon1,txtRazon2,txtRazon3;
    private Alert message;
    private boolean[] serviciosSelect;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register02);

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

        next=(Button) findViewById(R.id.btncontratar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();

            }
        });

        servicios=(MultiSpinner)findViewById(R.id.servicios);

        // Spinner element
        especialidad = (Spinner) findViewById(R.id.especialidad);

        // Spinner click listener
        especialidad.setOnItemSelectedListener(this);

        list = Constants.getServicios();

        serviciosSelect = new boolean[list.size()];

        select = new ArrayList<String>();

        select.clear();


        if( !Constants.serviciosOfrece.equals("")) {

            String[] arrayServicios = Constants.serviciosOfrece.split(",");

            for (int x = 0; x < arrayServicios.length; x++) {
                serviciosSelect[Integer.parseInt(arrayServicios[x])] = true;
            }
        }

        servicios.setItems(list, "", this,serviciosSelect);


        for (int i=0;i<list.size();i++)
        {
            if(serviciosSelect[i])
            {
                select.add(list.get(i));
            }
        }

        if(select.size()>0)
        {
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, select);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            especialidad.setAdapter(dataAdapter);
            especialidad.setSelection(Integer.parseInt(Constants.especialidad));
        }






        txtAnio=(EditText) findViewById(R.id.txtanio);
        txtTitulo1=(EditText) findViewById(R.id.txttitulo1);
        txtTitulo2=(EditText) findViewById(R.id.txttitulo2);
        txtTitulo3=(EditText) findViewById(R.id.txttitulo3);
        txtRazon1=(EditText) findViewById(R.id.txtrazon1);
        txtRazon2=(EditText) findViewById(R.id.txtrazon2);
        txtRazon3=(EditText) findViewById(R.id.txtrazon3);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            message = new Alert(Register02Activity.this, R.style.AlertDialog);
        }
        else {
            message = new Alert(Register02Activity.this);
        }

        if(!Constants.anios.equals(""))
        {
            txtAnio.setText(Constants.anios);
        }

        if(!Constants.titulos1.equals(""))
        {
            txtTitulo1.setText(Constants.titulos1);
        }
        if(!Constants.titulos2.equals(""))
        {
            txtTitulo2.setText(Constants.titulos2);
        }
        if(!Constants.titulos3.equals(""))
        {
            txtTitulo3.setText(Constants.titulos3);
        }
        if(!Constants.razon1.equals(""))
        {
            txtRazon1.setText(Constants.razon1);
        }
        if(!Constants.razon2.equals(""))
        {
            txtRazon2.setText(Constants.razon2);
        }
        if(!Constants.razon3.equals(""))
        {
            txtRazon3.setText(Constants.razon3);
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelect = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemsSelected(boolean[] selected) {

        Log.e("selected", String.valueOf(selected.length));

        select.clear();

       for (int i=0;i<list.size();i++)
        {
            if(selected[i])
            {
                select.add(list.get(i));
            }
        }

        if(select.size()>0)
        {
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, select);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            especialidad.setAdapter(dataAdapter);
        }


    }

    private void validate()
    {
        if(select.size()==0)
        {
            message.setMessage(getResources().getString(R.string.error_servicios));
            message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    message.dismiss();

                }
            });




            message.show();
            return;

        }

        if(txtAnio.getText().toString().trim().equals(""))
        {
            txtAnio.setError(getString(R.string.error_anio));
            return;
        }
        if(txtTitulo1.getText().toString().trim().equals(""))
        {
            txtTitulo1.setError(getString(R.string.error_titulo));
            return;
        }
        if(txtRazon1.getText().toString().trim().equals(""))
        {
            txtRazon1.setError(getString(R.string.error_razon));
            return;
        }


        Intent intent = new Intent(Register02Activity.this,Register03Activity.class);

        Constants.anios= txtAnio.getText().toString();
        Constants.titulos1= txtTitulo1.getText().toString();
        Constants.titulos2= txtTitulo2.getText().toString();
        Constants.titulos3= txtTitulo3.getText().toString();

        Constants.razon1= txtRazon1.getText().toString();
        Constants.razon2= txtRazon2.getText().toString();
        Constants.razon3= txtRazon3.getText().toString();

        String servicios="";

        for (int y=0;y<select.size();y++)
        {
            for (int x=0;x<Constants.getServicios().size();x++)
            {
                if(Constants.getServicios().get(x).equals(select.get(y)))
                {
                    servicios+=x+",";
                }
            }
        }
        servicios=servicios.substring(0, servicios.length()-1);
        Constants.serviciosOfrece = servicios;
        Constants.especialidad = String.valueOf(especialidad.getSelectedItemPosition());






        startActivity(intent);
        finish();
    }




    @Override
    public void onBackPressed() {

        Intent intent= new Intent(this,Register01Activity.class);
        startActivity(intent);
        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent= new Intent(this,Register01Activity.class);
                startActivity(intent);
                //onBackPressed();
                finish();
                //------------
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
