package com.wms.watermonitorsystem.fragments

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.wms.watermonitorsystem.R
import java.security.KeyStore
import java.util.*
import kotlin.collections.ArrayList

class reports : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var lastQuery: Query
    private lateinit var lineGraphView:GraphView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragview:View= inflater.inflate(R.layout.fragment_reports, container, false)
        // Inflate the layout for this fragment
        var mac= arguments?.getString("key")
        database= FirebaseDatabase.getInstance().getReference("User2").child(mac.toString())
        val sdf : SimpleDateFormat= SimpleDateFormat("HH:mm")
        //Toast.makeText(this.context, database.toString(), Toast.LENGTH_SHORT).show()
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataVal= ArrayList<KeyStore.Entry>()
                if(snapshot.exists()){
                    lineGraphView=fragview.findViewById<GraphView>(R.id.idGraphView)
                    var series: LineGraphSeries<DataPoint> = LineGraphSeries(arrayOf())

                    var cont :Int=0
                    var highest_temperature=-1000
                    var time_highest_temperature=""
                    var lowest_temperature=10000000
                    var time_lowest_temperature=""

                    for(myDataSnapshot in snapshot.children){
                        val time=(myDataSnapshot.child("epoch_time").getValue().toString())
                        val temp=(myDataSnapshot.child("temperature").value.toString()).toFloat().toInt()
                        if(highest_temperature<=temp){
                            highest_temperature=temp
                            time_highest_temperature=timestampConvert(time)
                        }

                        if(lowest_temperature>=temp){
                            lowest_temperature=temp
                            time_lowest_temperature=timestampConvert(time)
                        }

                        series.appendData(DataPoint(cont.toDouble(),temp.toDouble()),false,3000000)
                        cont=cont+1
                    }
                    //Toast.makeText(context, num.toString(), Toast.LENGTH_SHORT).show()
                    val highest_temperature_view=view?.findViewById<TextView>(R.id.highest_temperature)
                    highest_temperature_view?.text="Maior Temperatura: "+highest_temperature.toString()+"ºC"+"\n"+time_highest_temperature

                    val lowest_temperature_view=view?.findViewById<TextView>(R.id.lowest_temperature)
                    lowest_temperature_view?.text="Menor Temperatura: "+lowest_temperature.toString()+"ºC"+"\n"+time_lowest_temperature
                    // on below line adding animation
                    lineGraphView?.animate()

                    // on below line we are setting scrollable
                    // for point graph view
                    lineGraphView.viewport.borderColor=Color.parseColor("#3C993B")


                    // on below line we are setting color for series.
                        series.color = Color.parseColor("#3C993B")

                    // on below line we are adding
                    // data series to our graph view.
                    lineGraphView?.addSeries(series)
                    lineGraphView.gridLabelRenderer.verticalAxisTitle="ºC"
                    lineGraphView.gridLabelRenderer.horizontalAxisTitle="Número de Registros"
                    lineGraphView.gridLabelRenderer.numHorizontalLabels=6
                    lineGraphView.gridLabelRenderer.numVerticalLabels=8
                    lineGraphView.gridLabelRenderer.setHumanRounding(true,true)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(monitoring.TAG, "loadPost:onCancelledd", databaseError.toException())
            }
        })
        return fragview
    }

    fun timestampConvert(time:String): String {
        val timeD:Date= Date(time.toLong()*1000)
        val sdf : SimpleDateFormat= SimpleDateFormat("EEEE dd-MM-yyy HH:mm")
        val data_hora :String=sdf.format(timeD)
        return data_hora
    }

}