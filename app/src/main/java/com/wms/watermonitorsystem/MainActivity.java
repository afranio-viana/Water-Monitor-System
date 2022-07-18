package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button scanner;
    User user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = new User();
        String imei = getImeiPhone();

        /*firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        databaseReference.child(imei).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String MAC = snapshot.child("mac").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), MenuAppActivity.class);
                    intent.putExtra("key", MAC);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });*/
    }
    @Override
    public void onBackPressed() {
        // não chame o super desse método
    }

    private void addUser(String imeiUser, String mac){
        databaseReference = firebaseDatabase.getReference("User").child(imeiUser);
        user.setUserImei(imeiUser);
        user.setMac(mac);
        databaseReference.setValue(user);
    }

    public String getImeiPhone(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public void scanner(View view){
        scanner = findViewById(R.id.button_scanner);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Digitalize o QR Code");
        intentIntegrator.setOrientationLocked(false);
        List<String> acceptedTypes = new ArrayList<String>();
        acceptedTypes.add("QR_CODE");
        intentIntegrator.initiateScan(acceptedTypes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String deviceId = getImeiPhone();
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Operação cancelada", Toast.LENGTH_SHORT).show();
            } else {
                String mac = intentResult.getContents();

                String patternMac = "^([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}$";
                boolean isMac = mac.matches(patternMac);

                if (isMac) {
                    //addUser(deviceId, mac);
                    finish();
                    Intent intent = new Intent(getApplicationContext(), ChooseReservoir.class);
                    intent.putExtra("key", mac);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Formato inválido de MAC",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode = Integer.parseInt(null), resultCode = Integer.parseInt(null), data);
        }
    }
}