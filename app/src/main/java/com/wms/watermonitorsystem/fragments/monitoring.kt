package com.wms.watermonitorsystem.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.wms.watermonitorsystem.R
import kotlin.reflect.typeOf

class monitoring : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var lastQuery: Query
    companion object {
        const val TAG = "KotlinQueryActivity"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mac= arguments?.getString("key")
        database= FirebaseDatabase.getInstance().getReference("User2")
        lastQuery = database.child(mac.toString()).orderByKey().limitToLast(1)
        lastQuery.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val temperature=view?.findViewById<TextView>(R.id.temperature)
                    val humidity=view?.findViewById<TextView>(R.id.humidity)
                    for( child:DataSnapshot in snapshot.children){
                        val temp=(child.child("temperature").getValue().toString()).toFloat().toInt()
                        val humi=(child.child("humidity").getValue().toString()).toFloat().toInt()
                        temperature?.text=temp.toString()+" °C"
                        humidity?.text=humi.toString()+" %"

                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelledd", databaseError.toException())
            }
        })

        //Toast.makeText(this.context, database.toString(), Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }
}