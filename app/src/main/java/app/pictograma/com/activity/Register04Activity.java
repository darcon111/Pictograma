package app.pictograma.com.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import app.pictograma.com.R;
import app.pictograma.com.clases.Alert;
import app.pictograma.com.clases.Profeccional;
import app.pictograma.com.clases.User;
import app.pictograma.com.config.Constants;


public class Register04Activity extends AppCompatActivity  implements
        IPickResult {


    private Toolbar toolbar;
    private String itemSelect;

    private static DatabaseReference databaseUsers;

    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;

    private int imgselect=0;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private File outPutFile = null;
    private String mCurrentPhotoPath;
    private Bitmap bitmap;
    private String image1 = "";
    private String image2 = "";
    private String image3 = "";
    private String image4 = "";
    private String image5 = "";
    private String image6 = "";
    private String image7 = "";
    private String image8 = "";
    private String image9 = "";
    private String image10 = "";

    private Button guardar;
    private Alert message;
    private static FirebaseUser user;

    private static DatabaseReference databaseProfesional;
    private String idUser="",idProfesional="";
    private static final String TAG = Register04Activity.class.getSimpleName();

    private ProgressDialog progressDialog=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register04);

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

        guardar=(Button) findViewById(R.id.btncontratar);


        // Initialize Firebase Auth
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseProfesional = FirebaseDatabase.getInstance().getReference("profesionales");

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);


        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");


        if(!Constants.imagen1.equals(""))
        {
            img1.setImageBitmap(Constants.decodeBase64(Constants.imagen1));
            image1 = Constants.imagen1;
        }

        if(!Constants.imagen2.equals(""))
        {
            img2.setImageBitmap(Constants.decodeBase64(Constants.imagen2));
            image2 = Constants.imagen2;
        }


        if(!Constants.imagen3.equals(""))
        {
            img3.setImageBitmap(Constants.decodeBase64(Constants.imagen3));
            image3 = Constants.imagen3;
        }


        if(!Constants.imagen4.equals(""))
        {
            img4.setImageBitmap(Constants.decodeBase64(Constants.imagen4));
            image4 = Constants.imagen4;
        }

        if(!Constants.imagen5.equals(""))
        {
            img5.setImageBitmap(Constants.decodeBase64(Constants.imagen5));
            image5 = Constants.imagen5;
        }



        if(!Constants.imagen6.equals(""))
        {
            img6.setImageBitmap(Constants.decodeBase64(Constants.imagen6));
            image6 = Constants.imagen6;
        }

        if(!Constants.imagen7.equals(""))
        {
            img7.setImageBitmap(Constants.decodeBase64(Constants.imagen7));
            image7 = Constants.imagen7;
        }

        if(!Constants.imagen8.equals(""))
        {
            img8.setImageBitmap(Constants.decodeBase64(Constants.imagen8));
            image8 = Constants.imagen8;
        }

        if(!Constants.imagen9.equals(""))
        {
            img9.setImageBitmap(Constants.decodeBase64(Constants.imagen9));
            image4 = Constants.imagen9;
        }
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=1;
                showFileChooser();
            }
        });

        if(!Constants.imagen2.equals(""))
        {
            img2.setImageBitmap(Constants.decodeBase64(Constants.imagen2));
        }

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=2;
                showFileChooser();
            }
        });

        if(!Constants.imagen3.equals(""))
        {
            img3.setImageBitmap(Constants.decodeBase64(Constants.imagen3));
        }


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=3;
                showFileChooser();
            }
        });

        if(!Constants.imagen4.equals(""))
        {
            img4.setImageBitmap(Constants.decodeBase64(Constants.imagen4));
        }

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=4;
                showFileChooser();
            }
        });

        if(!Constants.imagen5.equals(""))
        {
            img5.setImageBitmap(Constants.decodeBase64(Constants.imagen5));
        }


        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=5;
                showFileChooser();
            }
        });

        if(!Constants.imagen6.equals(""))
        {
            img6.setImageBitmap(Constants.decodeBase64(Constants.imagen6));
        }


        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=6;
                showFileChooser();
            }
        });

        if(!Constants.imagen7.equals(""))
        {
            img7.setImageBitmap(Constants.decodeBase64(Constants.imagen7));
        }


        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=7;
                showFileChooser();
            }
        });

        if(!Constants.imagen8.equals(""))
        {
            img8.setImageBitmap(Constants.decodeBase64(Constants.imagen8));
        }


        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=8;
                showFileChooser();
            }
        });

        if(!Constants.imagen9.equals(""))
        {
            img9.setImageBitmap(Constants.decodeBase64(Constants.imagen9));
        }


        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=9;
                showFileChooser();
            }
        });

        if(!Constants.imagen10.equals(""))
        {
            img10.setImageBitmap(Constants.decodeBase64(Constants.imagen10));
        }

        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgselect=10;
                showFileChooser();
            }
        });



        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            message = new Alert(this, R.style.AlertDialog);
        }
        else {
            message = new Alert(this);
        }



        Query queryuser = databaseUsers
                .orderByChild("firebaseId").equalTo(user.getUid());


        queryuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting artist


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    User use = postSnapshot.getValue(User.class);
                    idUser= use.getId();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


        Query queryuser2 = databaseProfesional
                .orderByChild("userid").equalTo(user.getUid());


        queryuser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting artist


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Profeccional pro = postSnapshot.getValue(Profeccional.class);
                    idProfesional= pro.getId();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(image1.equals("") && image2.equals("") && image3.equals("") && image4.equals("") && image5.equals("") && image6.equals("") && image7.equals("") && image8.equals("") && image9.equals("") && image10.equals(""))
                {

                    if(Constants.imagen1.equals("") && Constants.imagen2.equals("") && Constants.imagen3.equals("") && Constants.imagen4.equals("") && Constants.imagen5.equals("") && Constants.imagen6.equals("") && Constants.imagen7.equals("") && Constants.imagen8.equals("") && Constants.imagen9.equals("") && Constants.imagen10.equals("")) {
                        message.setMessage(getApplicationContext().getString(R.string.error_img));
                        message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                message.dismiss();
                            }
                        });
                        message.show();
                        return;
                    }
                }

                Profeccional data = new Profeccional();
                data.setNombre(Constants.nombre);
                data.setDirreccion(Constants.direccion);
                data.setBanco(Constants.banco);
                data.setSucursal(Constants.sucursal);
                data.setNum(Constants.cta);
                data.setTarjeta(Constants.tarjeta);
                data.setNumtarjeta(Constants.numtarjeta);


                data.setServicios(Constants.serviciosOfrece);
                data.setEspecilidad(Constants.especialidad);
                data.setAnio(Constants.anios);

                data.setTitulo1(Constants.titulos1);
                data.setTitulo2(Constants.titulos2);
                data.setTitulo3(Constants.titulos3);
                data.setRazon1(Constants.razon1);
                data.setRazon2(Constants.razon2);
                data.setRazon3(Constants.razon3);

                data.setImg1(image1);
                data.setImg2(image2);
                data.setImg3(image3);
                data.setImg4(image4);
                data.setImg5(image5);
                data.setImg6(image6);
                data.setImg7(image7);
                data.setImg8(image8);
                data.setImg9(image9);
                data.setImg10(image10);

                data.setImgPerfil(Constants.imagenPerfil);


                progressDialog = new ProgressDialog(Register04Activity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialogsave);
                progressDialog.setCancelable(false);


                if(idProfesional.trim().length()==0){
                    String id = databaseProfesional.push().getKey();

                    data.setId(id);
                    data.setUserid(user.getUid());

                    //Saving
                    databaseProfesional.child(id).setValue(data);

                    databaseProfesional.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            progressDialog.dismiss();

                            databaseUsers.child(idUser).child("type").setValue("2");


                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                                message = new Alert(Register04Activity.this, R.style.AlertDialog);
                            }
                            else {
                                message = new Alert(Register04Activity.this);
                            }

                            message.setMessage(getResources().getString(R.string.ok_save));
                            message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    message.dismiss();
                                    finish();
                                }
                            });
                            message.show();



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressDialog.dismiss();
                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                                message = new Alert(Register04Activity.this, R.style.AlertDialog);
                            }
                            else {
                                message = new Alert(Register04Activity.this);
                            }

                            message.setMessage(getResources().getString(R.string.error_save));
                            message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    message.dismiss();
                                }
                            });
                            message.show();

                        }
                    });







                }else
                {


                    ObjectMapper oMapper = new ObjectMapper();

                    Map<String, Object> update = oMapper.convertValue(data, Map.class);

                    Iterator<Map.Entry<String, Object>> entryIt = update.entrySet().iterator();

                    while (entryIt.hasNext()) {
                        Map.Entry<String, Object> entry = entryIt.next();

                        if(entry.getKey().equals("id") || entry.getKey().equals("userid"))
                        {
                            entryIt.remove();
                        }
                    }

                    databaseProfesional.child(idProfesional).setValue(update);

                    databaseProfesional.child(idProfesional).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(!Constants.imagenPerfil.equals(""))
                            {
                                databaseUsers.child(idUser).child("imagen").setValue(Constants.imagenPerfil);
                            }


                            progressDialog.dismiss();

                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                                message = new Alert(Register04Activity.this, R.style.AlertDialog);
                            }
                            else {
                                message = new Alert(Register04Activity.this);
                            }

                            message.setMessage(getResources().getString(R.string.ok_save));
                            message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    message.dismiss();
                                    finish();
                                }
                            });
                            message.show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressDialog.dismiss();
                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                                message = new Alert(Register04Activity.this, R.style.AlertDialog);
                            }
                            else {
                                message = new Alert(Register04Activity.this);
                            }

                            message.setMessage(getResources().getString(R.string.error_save));
                            message.setPositveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    message.dismiss();
                                }
                            });
                            message.show();

                        }
                    });






                    /*for (Map.Entry<String, Object> entry : update.entrySet()) {

                        if(!entry.getKey().equals("id") && !entry.getKey().equals("userid"))
                        {
                            databaseProfesional.child(idProfesional).child(entry.getKey()).setValue(entry.getValue());
                        }

                    }*/





                }











            }
        });







    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_FROM_FILE) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            //bitmap = ProcessImage.compressImage(filePath, getApplicationContext(), null);
            //Getting the Bitmap from Gallery
            performCrop(filePath);

        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {

            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            performCrop(uri);
                        }
                    });


        }

        if(requestCode==CROP_FROM_CAMERA) {
            try {
                if(outPutFile.exists()){
                    //bitmap = decodeFile(outPutFile);

                    InputStream ims = new FileInputStream(outPutFile);
                    bitmap= BitmapFactory.decodeStream(ims);

                    switch (imgselect) {
                        case 1:

                            image1 = Constants.getStringImage(bitmap);
                            img1.setImageBitmap(bitmap);

                            break;
                        case 2:

                            image2 = Constants.getStringImage(bitmap);
                            img2.setImageBitmap(bitmap);

                            break;
                        case 3:

                            image3 = Constants.getStringImage(bitmap);
                            img3.setImageBitmap(bitmap);
                            break;
                        case 4:
                            image4 = Constants.getStringImage(bitmap);
                            img4.setImageBitmap(bitmap);
                            break;
                        case 5:
                            image5 = Constants.getStringImage(bitmap);
                            img5.setImageBitmap(bitmap);
                            break;
                        case 6:
                            image6 = Constants.getStringImage(bitmap);
                            img6.setImageBitmap(bitmap);
                            break;
                        case 7:
                            image7 = Constants.getStringImage(bitmap);
                            img7.setImageBitmap(bitmap);
                            break;
                        case 8:
                            image8 = Constants.getStringImage(bitmap);
                            img8.setImageBitmap(bitmap);
                            break;
                        case 9:
                            image9 = Constants.getStringImage(bitmap);
                            img9.setImageBitmap(bitmap);
                            break;
                        case 10:
                            image10 = Constants.getStringImage(bitmap);
                            img10.setImageBitmap(bitmap);

                            break;


                    }



                    //imagen.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public void showFileChooser() {

        PickImageDialog.build(new PickSetup()
                .setTitle(getResources().getString(R.string.image))
                .setTitleColor(getResources().getColor(R.color.colorDivider))
                .setCameraButtonText(getResources().getString(R.string.camera))
                .setGalleryButtonText(getResources().getString(R.string.sd))
                .setButtonTextColor(getResources().getColor(R.color.colorDivider))
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setCancelText(getResources().getString(R.string.cancelar))
                .setCancelTextColor(getResources().getColor(R.color.colorDivider))
                .setGalleryIcon(R.drawable.ic_perm_media_white_24dp)
                .setCameraIcon(R.drawable.ic_photo_camera_white_24dp)

        ).show(getSupportFragmentManager());


    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            r.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),  r.getBitmap(), "temp", null);
            performCrop(Uri.parse(path));

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void performCrop(Uri uri) {

        int x=dpToPx(280);
        int y=dpToPx(280);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", x);
        intent.putExtra("outputY", y);
        //intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        //intent.putExtra("return-data", true);
        //Create output file here
        try {
            /*mImageCaptureUri = FileProvider.getUriForFile(AddPlatoActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());*/
            outPutFile =createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }



        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onBackPressed() {

        Intent intent= new Intent(this,Register03Activity.class);
        startActivity(intent);
        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent= new Intent(this,Register03Activity.class);
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
