package com.wms.watermonitorsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wms.watermonitorsystem.fragments.monitoring;
import com.wms.watermonitorsystem.fragments.reports;

public class MenuAppActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView textTemperature;
    TextView textHumidity;
    BottomNavigationView bottomNav;
    Fragment monitoring=new monitoring();
    Fragment reports=new reports();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String MAC = extras.getString("key");
        setContentView(R.layout.activity_menu_app);
        bottomNav=findViewById(R.id.bottom_navigation);
        replacefragment(monitoring,MAC);
        bottomNav.setOnItemSelectedListener(item -> {
            int id=item.getItemId();
            switch (id){
                case R.id.page_1:
                    replacefragment(monitoring,MAC);
                    return true;
                case R.id.page_2:
                    replacefragment(reports,MAC);
                    return true;
            }
            return true;
        });
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
                    textTemperature.setText(temperatureStr+" ??C");
                }else{
                    textTemperature.setText("N??o Existe");
                    textHumidity.setText("N??o Existe");
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

    public void replacefragment(Fragment newFragment,String mac){
        Bundle bundle=new Bundle();
        bundle.putString("key", mac);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        // n??o chame o super desse m??todo
    }

}