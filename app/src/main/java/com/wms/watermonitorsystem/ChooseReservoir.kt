package com.wms.watermonitorsystem

import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChooseReservoir : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var radioGroup: RadioGroup? = null
    private val user:User=User()
    lateinit var radioButton: RadioButton
    private lateinit var button: Button
    //val textView=findViewById<TextView>(R.id.apenas_um_teste)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_reservoir)

        var mac=intent.getStringExtra("key")
        var reservoir=intent.getStringExtra("reservoir")
        var user_id_android:String=getImeiPhone()
        database= FirebaseDatabase.getInstance().getReference("User")
        //var textView: TextView =findViewById<TextView>(R.id.apenas_um_teste)
        //textView.text = user_id_android
        radioGroup = findViewById(R.id.radioGroup)
        button = findViewById(R.id.submit_reservoir)
        button.setOnClickListener {
            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
            if(intSelectButton<0){
                radioButton=findViewById(R.id.radioButton1)
            }else{
                radioButton = findViewById(intSelectButton)
            }
            addUser(user_id_android,mac.toString(),radioButton.text.toString())
            //Toast.makeText(baseContext, radioButton.text, Toast.LENGTH_SHORT).show()
        }
    }

    fun addUser(user_id_android:String, mac:String,reservoir:String){
        database=database.child(user_id_android)
        user.reservoir=reservoir
        user.userImei=user_id_android
        user.mac=mac
        database.setValue(user)
        finish()
    }
    fun getImeiPhone(): String {
        val id_android:String=Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        return id_android
    }


}