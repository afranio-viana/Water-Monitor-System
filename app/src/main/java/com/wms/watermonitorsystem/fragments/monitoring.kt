package com.wms.watermonitorsystem.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.wms.watermonitorsystem.R

class monitoring : Fragment() {
    private lateinit var database: DatabaseReference
    companion object {
        private const val TAG = "KotlinQueryActivity"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mac= arguments?.getString("key")
        database= FirebaseDatabase.getInstance().getReference("User2")
        database.child(mac.toString()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   val temperature=view?.findViewById<TextView>(R.id.temperature)
                   temperature?.text=snapshot.child("temperature").getValue().toString()+" °C"
                   val humidity=view?.findViewById<TextView>(R.id.humidity)
                   humidity?.text=snapshot.child("humidity").getValue().toString()+" %"
               }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        } )
        //Toast.makeText(this.context, database.toString(), Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }
}