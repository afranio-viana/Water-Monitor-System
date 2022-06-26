package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button scanner;
    TextView text1,text2;
    User user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase=FirebaseDatabase.getInstance();
        user= new User();
        String imei=getImeiPhone();
        databaseReference=firebaseDatabase.getReference("User");
        databaseReference.child(imei).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String MAC=snapshot.child("mac").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), menu_app.class);
                    intent.putExtra("key",MAC);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    //Toast.makeText(MainActivity.this,"Existe",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addUser(String imeiUser, String mac){
        databaseReference=firebaseDatabase.getReference("User").child(imeiUser);
        user.setUserImei(imeiUser);
        user.setMac(mac);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(user);
                Toast.makeText(MainActivity.this,"Adicionado",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Falha",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String getImeiPhone(){

        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String device_id=tm.getImei();
        return device_id;

    }
    public void scannear(View view){
        scanner=findViewById(R.id.button_scanner);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();

    }
    /*public void read_database(){
        DatabaseReference reference=mDatabase.getReference().child("DHT").child("temperature");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temperatura= snapshot.child("-N5MOBzZgm5l5KiUkayq").getValue(int.class);
                String temp= Integer.toString(temperatura);
                Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
                read_database();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "erro", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String device_id=getImeiPhone();
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String mac=intentResult.getContents();
                //Toast.makeText(MainActivity.this,device_id,Toast.LENGTH_SHORT).show();
                addUser(device_id,mac);

            }
        } else {
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            //Toast.makeText(cad_user.this, "O usuário foi criado com sucesso!", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode=Integer.parseInt(null), resultCode=Integer.parseInt(null), data);
        }
    }
}

