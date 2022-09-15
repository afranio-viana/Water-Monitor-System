package com.wms.watermonitorsystem.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import com.wms.watermonitorsystem.R
import com.wms.watermonitorsystem.databinding.ActivityMainBinding
import com.yangp.ypwaveview.YPWaveView
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
        var mac = arguments?.getString("key")
        var reservoir=arguments?.getString("reservoir")
        database = FirebaseDatabase.getInstance().getReference("User2")
        lastQuery = database.child(mac.toString()).orderByKey().limitToLast(1)
        lastQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val temperature = view?.findViewById<TextView>(R.id.temperature)
                    val humidity = view?.findViewById<TextView>(R.id.humidity)
                    for (child: DataSnapshot in snapshot.children) {
                        val temp = (child.child("temperature").getValue().toString()).toFloat().toInt()
                        val humi = (child.child("humidity").getValue().toString()).toFloat().toInt()
                        val distance = (child.child("distance").getValue().toString()).toFloat().toInt()
                        temperature?.text = temp.toString() + " °C"
                        humidity?.text = humi.toString() + " %"

                        var distance_total=0
                        if(reservoir.equals("teste")){
                            distance_total=24
                        }else if(reservoir.equals("1000")){
                            distance_total=80
                        }else if(reservoir.equals("500")){
                            distance_total=70
                        }else{
                            distance_total=50
                        }

                        val nivel = view?.findViewById<YPWaveView>(R.id.YPWaveView)
                        if(distance_total<distance){
                            nivel?.progress = 0
                        }else{
                            val percent = ((100 * (distance_total-distance)) / distance_total)
                            nivel?.progress = percent.toInt()*10
                        }



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