package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                    //Toast.makeText(MainActivity.this,snapshot.toString(),Toast.LENGTH_SHORT).show();
                    String MAC=snapshot.child("mac").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), menu_app.class);
                    intent.putExtra("key",MAC);
                    startActivity(intent);

                    //Toast.makeText(MainActivity.this,snapshot.toString(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Nao existe",Toast.LENGTH_SHORT).show();

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
        databaseReference.setValue(user);
    }
    public String getImeiPhone(){

        String device_id= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        return device_id;

    }
    public void scannear(View view){
        scanner=findViewById(R.id.button_scanner);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();

    }
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

            //Toast.makeText(cad_user.this, "O usuário foi criado com sucesso!", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode=Integer.parseInt(null), resultCode=Integer.parseInt(null), data);
        }
    }
}

