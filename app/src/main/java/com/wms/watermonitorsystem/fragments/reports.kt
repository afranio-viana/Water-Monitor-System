package com.wms.watermonitorsystem.fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.wms.watermonitorsystem.R
import java.security.KeyStore
import java.util.*
import kotlin.collections.ArrayList

class reports : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var lastQuery: Query
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mac= arguments?.getString("key")
        database= FirebaseDatabase.getInstance().getReference("User2").child(mac.toString())
        //Toast.makeText(this.context, database.toString(), Toast.LENGTH_SHORT).show()
        /*database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataVal= ArrayList<KeyStore.Entry>()
                if(snapshot.hasChildren()){
                    for(myDataSnapshot :DataSnapshot in snapshot.children){
                        //val dataPoint:DataPoint=myDataSnapshot.getValue(DataPoint:Object)
                        //Toast.makeText(context, myDataSnapshot.toString(), Toast.LENGTH_SHORT).show()
                        val time=myDataSnapshot.child("epoch_time").getValue().toString()
                        val data_hora=timestampConvert(time)
                        val temp=(myDataSnapshot.child("temperature").getValue().toString()).toFloat().toInt()
                        val xvalue=ArrayList<String>()
                        xvalue.add(data_hora)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(monitoring.TAG, "loadPost:onCancelledd", databaseError.toException())
            }
        })*/
        lastQuery = database.orderByKey().limitToLast(1)
        lastQuery.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val data=view?.findViewById<TextView>(R.id.textView6)
                    for( child: DataSnapshot in snapshot.children){
                        val time=child.child("epoch_time").getValue().toString()
                        val data_hora=timestampConvert(time)

                    data?.text=data_hora.toString()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(monitoring.TAG, "loadPost:onCancelledd", databaseError.toException())
            }
        })
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    fun timestampConvert(time:String): String {
        val timeD:Date= Date(time.toLong()*1000)
        val sdf : SimpleDateFormat= SimpleDateFormat("EEEE dd-MM-yyy HH:mm")
        val data_hora :String=sdf.format(timeD)
        return data_hora
    }

}