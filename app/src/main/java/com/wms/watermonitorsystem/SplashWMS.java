package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashWMS extends AppCompatActivity {
    User user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_wms);
        user = new User();
        String imei = getImeiPhone();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        databaseReference.child(imei).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    finish();
                    String MAC = snapshot.child("mac").getValue(String.class);
                    String reservoir=snapshot.child("reservoir").getValue(String.class);
                    //Toast.makeText(SplashWMS.this, reservoir,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MenuAppActivity.class);
                    intent.putExtra("key", MAC);
                    intent.putExtra("reservoir",reservoir);
                    startActivity(intent);
                }else{
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public String getImeiPhone(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
}