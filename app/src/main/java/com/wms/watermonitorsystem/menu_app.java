package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class menu_app extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView text_temperature;
    TextView text_humidity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle extras=getIntent().getExtras();
        String MAC=extras.getString("key");
        setContentView(R.layout.activity_menu_app);
        String imei=getImeiPhone();
        text_temperature=findViewById(R.id.temperature);
        text_humidity=findViewById(R.id.humidity);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User2");
        databaseReference.child(MAC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int temperature=snapshot.child("temperature").getValue(int.class);
                    int humidity=snapshot.child("humidity").getValue(int.class);
                    String humidity_str=String.valueOf(humidity);
                    String temperature_str= String.valueOf(temperature);
                    text_humidity.setText("Humidade:  "+humidity_str+" kg/m³");
                    text_temperature.setText("Temperatura:  "+temperature_str+" °C");
                }else{
                    text_temperature.setText("Não Existe");
                    text_humidity.setText("Não Existe");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String MAC= snapshot.child("mac").getValue(String.class);
                firebaseDatabas=FirebaseDatabase.getInstance();
                reference=firebaseDatabas.getReference("User2").child(MAC);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        if(snap.exists()){
                            int temperature=snap.child("temperature").getValue(int.class);
                            String temperature_str= String.valueOf(temperature);
                            text_temperature.setText(temperature_str);
                        }else{
                            text_temperature.setText("Não Existe");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
    public  void resgistrarDispositivo(View view){
        databaseReference=firebaseDatabase.getReference("User");
        String imei =getImeiPhone();
        databaseReference=databaseReference.child(imei);
        databaseReference.removeValue();
        retornar();
    }

    public void retornar(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public String getImeiPhone(){

        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String device_id=tm.getImei();
        return device_id;

    }
}