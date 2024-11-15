package com.example.senportable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final int SECOND_CALL_ID =123;
    private static final int REQUEST_LOCATION = 123;

    TextView latView;
    TextView longView;

     Button  btnAjout;

    LocationManager locationManager;
    String latitude,longitude;
    Button btnLoc;
    Button btnInf;
    EditText editText;
    TextView textView;
    TelephonyManager tel;
    private String varIMEI;

    private String deviceId;
    FirebaseFirestore base;


   private  TextView textViewA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInf = (Button) findViewById(R.id.phoneInf);
        btnLoc = (Button) findViewById(R.id.phoneLoc);
        btnAjout= (Button) findViewById(R.id.phoneAjout);
        textViewA = (TextView) findViewById(R.id.viewIda);
        textView = (TextView) findViewById(R.id.viewId);
        latView = (TextView) findViewById(R.id.latId);
        longView = (TextView) findViewById(R.id.longId);

//        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Carte.class);
//                intent.putExtra("message",editText.getText().toString());
//                startActivityForResult(intent, SECOND_CALL_ID);
                startActivity(intent);

            }
        });
        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base =  FirebaseFirestore.getInstance();
                String x = textViewA.getText().toString();
                String y = textView.getText().toString();
                double z = Double.parseDouble(latView.getText().toString());
                double t = Double.parseDouble(longView.getText().toString());
                Coordonnees newCoordonnees = new Coordonnees(x, y, z,t);
                CollectionReference ref = base.collection("Coordonnees");
                ref.document().set(newCoordonnees).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "ajouter succés", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "eurreur ajout", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnInf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int PermissI = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE);

                if (PermissI == PackageManager.PERMISSION_GRANTED) {
                    tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    varIMEI = tel.getDeviceId().toString();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 123);
                }
                textViewA.setText(varIMEI);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                textView.setText(Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                 localisation du gps
//
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // whrite fonction To enable GPS

                }
                else {
                    //GPS is albready on then
                    getLocation();
                }
               }
        });
    }
    private  void  getLocation(){
//         check permission again

        if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        else {

            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetWork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationPassive= locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if(locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double log = locationGPS.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(log);

                latView.setText(latitude);
                longView.setText(longitude);

            }
            else if(locationNetWork != null){
                double lat = locationNetWork.getLatitude();
                double log = locationNetWork.getLongitude();

                 latitude = String.valueOf(lat);
                longitude = String.valueOf(log);

                latView.setText(latitude);
                longView.setText(longitude);
            }
            else if(locationPassive != null){
                double lat = locationPassive.getLatitude();
                double log = locationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(log);

                latView.setText(latitude);
                longView.setText(longitude);

            }
            else {
                Toast.makeText(this,"can't get your location ",Toast.LENGTH_LONG);
            }
            //that finish

        }
    }
        private void onGPS(){
         final AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("Enable GPS ").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

             }
         }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
             }
         });
         final AlertDialog alertDialog = builder.create();
         alertDialog.show();
       }

    }
