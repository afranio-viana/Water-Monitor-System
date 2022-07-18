package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuAppActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView textTemperature;
    TextView textHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String MAC = extras.getString("key");
        //setContentView(R.layout.activity_menu_app);
        //textTemperature = findViewById(R.id.temperature);
        //textHumidity = findViewById(R.id.humidity);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User2");
        /*databaseReference.child(MAC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int temperature = snapshot.child("temperature").getValue(int.class);
                    int humidity = snapshot.child("humidity").getValue(int.class);
                    String humidityStr = String.valueOf(humidity);
                    String temperatureStr = String.valueOf(temperature);
                    textHumidity.setText(humidityStr+" %");
                    textTemperature.setText(temperatureStr+" °C");
                }else{
                    textTemperature.setText("Não Existe");
                    textHumidity.setText("Não Existe");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });*/

    }

    public  void registerDevice(View view) {
        databaseReference = firebaseDatabase.getReference("User");
        String imei = getImeiPhone();
        databaseReference = databaseReference.child(imei);
        databaseReference.setValue(null);
        goBack();
    }

    public void goBack() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String getImeiPhone() {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
}