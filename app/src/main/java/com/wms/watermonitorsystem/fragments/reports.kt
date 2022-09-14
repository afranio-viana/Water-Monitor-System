package com.wms.watermonitorsystem.fragments

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.wms.watermonitorsystem.R
import java.security.KeyStore
import java.time.Duration
import java.util.*

class reports : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var lastQuery: Query
    private lateinit var lineGraphView:GraphView
    private lateinit var lineGraphView_Humidity: GraphView
    private lateinit var lineGraphView_Distance: GraphView

    private lateinit var lowest_distance_difference:Date
    private lateinit var highest_distance_difference:Date
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragview:View= inflater.inflate(R.layout.fragment_reports, container, false)
        // Inflate the layout for this fragment
        var mac= arguments?.getString("key")
        var reservoir=arguments?.getString("reservoir")
        database= FirebaseDatabase.getInstance().getReference("User2").child(mac.toString())
        val sdf : SimpleDateFormat= SimpleDateFormat("EEEE dd-MM-yyy HH:mm")
        //Toast.makeText(this.context, database.toString(), Toast.LENGTH_SHORT).show()
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataVal= ArrayList<KeyStore.Entry>()
                if(snapshot.exists()){
                    lineGraphView=fragview.findViewById<GraphView>(R.id.idGraphView)
                    lineGraphView_Humidity=fragview.findViewById<GraphView>(R.id.idGraphViewHumidity)
                    lineGraphView_Distance=fragview.findViewById<GraphView>(R.id.idGraphViewDistance)
                    var series: LineGraphSeries<DataPoint> = LineGraphSeries(arrayOf())
                    var series_humidity:LineGraphSeries<DataPoint> = LineGraphSeries(arrayOf())
                    var series_distance:LineGraphSeries<DataPoint> = LineGraphSeries(arrayOf())
                    var cont :Int=0
                    var highest_temperature=-1000
                    var time_highest_temperature=""
                    var lowest_temperature=10000000
                    var time_lowest_temperature=""
                    var highest_humidity=-1000
                    var time_highest_humidity=""
                    var lowest_humidity=10000000
                    var time_lowest_humidity=""
                    var highest_distance=-1000
                    var time_highest_distance=""
                    var lowest_distance=10000000
                    var time_lowest_distance=""


                    for(myDataSnapshot in snapshot.children){
                        val time=(myDataSnapshot.child("epoch_time").getValue().toString())
                        val temp=(myDataSnapshot.child("temperature").value.toString()).toFloat().toInt()
                        val humi=(myDataSnapshot.child("humidity").value.toString()).toFloat().toInt()
                        val distance=(myDataSnapshot.child("distance").value.toString()).toFloat().toInt()
                        if(highest_temperature<=temp){
                            highest_temperature=temp
                            time_highest_temperature=timestampConvert(time)
                        }
                        if(lowest_temperature>=temp){
                            lowest_temperature=temp
                            time_lowest_temperature=timestampConvert(time)
                        }
                        if(highest_humidity<=humi){
                            highest_humidity=humi
                            time_highest_humidity=timestampConvert(time)
                        }
                        if(lowest_humidity>=humi){
                            lowest_humidity=humi
                            time_lowest_humidity=timestampConvert(time)
                        }
                        if(highest_distance<=distance){
                            highest_distance=distance
                            time_highest_distance=timestampConvert(time)
                            highest_distance_difference=Date(time.toLong()*1000)
                        }
                        if(lowest_distance>=distance){
                            lowest_distance=distance
                            time_lowest_distance=timestampConvert(time)
                            lowest_distance_difference=Date(time.toLong()*1000)
                        }

                        series.appendData(DataPoint(cont.toDouble(),temp.toDouble()),false,3000000)
                        series_humidity.appendData(DataPoint(cont.toDouble(),humi.toDouble()),false,3000000)
                        series_distance.appendData(DataPoint(cont.toDouble(),distance.toDouble()),false,3000000)
                        cont=cont+1
                    }
                    //Toast.makeText(context, num.toString(), Toast.LENGTH_SHORT).show()

                    //Temperature
                    val highest_temperature_view=view?.findViewById<TextView>(R.id.highest_temperature)
                    highest_temperature_view?.text="Maior Temperatura:\n"+highest_temperature.toString()+"ºC"+"\n"+time_highest_temperature
                    val lowest_temperature_view=view?.findViewById<TextView>(R.id.lowest_temperature)
                    lowest_temperature_view?.text="Menor Temperatura:\n"+lowest_temperature.toString()+"ºC"+"\n"+time_lowest_temperature
                    lineGraphView?.animate()
                    lineGraphView.viewport.borderColor=Color.parseColor("#3C993B")
                    series.color = Color.parseColor("#3C993B")
                    lineGraphView?.addSeries(series)
                    lineGraphView.gridLabelRenderer.verticalAxisTitle="ºC"
                    lineGraphView.gridLabelRenderer.horizontalAxisTitle="Número de Registros"
                    lineGraphView.gridLabelRenderer.numHorizontalLabels=6
                    lineGraphView.gridLabelRenderer.numVerticalLabels=8
                    lineGraphView.gridLabelRenderer.setHumanRounding(true,true)


                    //Humidity
                    val highest_humidity_view=view?.findViewById<TextView>(R.id.highest_humidity)
                    highest_humidity_view?.text="Maior Umidade:\n"+highest_humidity.toString()+"%"+"\n"+time_highest_humidity
                    val lowest_humidity_view=view?.findViewById<TextView>(R.id.lowest_humidity)
                    lowest_humidity_view?.text="Menor Umidade:\n"+lowest_humidity.toString()+"%"+"\n"+time_lowest_humidity
                    lineGraphView_Humidity?.animate()
                    lineGraphView_Humidity.viewport.borderColor=Color.parseColor("#FF0000")
                    series_humidity.color = Color.parseColor("#FF0000")
                    lineGraphView_Humidity?.addSeries(series_humidity)
                    lineGraphView_Humidity.gridLabelRenderer.verticalAxisTitle="%"
                    lineGraphView_Humidity.gridLabelRenderer.horizontalAxisTitle="Número de Registros"
                    lineGraphView_Humidity.gridLabelRenderer.numHorizontalLabels=6
                    lineGraphView_Humidity.gridLabelRenderer.numVerticalLabels=8
                    lineGraphView_Humidity.gridLabelRenderer.setHumanRounding(true,true)

                    //Distance
                    val difference=highest_distance_difference.compareTo(lowest_distance_difference)
                    val difference_distance_view=view?.findViewById<TextView>(R.id.difference_distance)
                    if(difference>0){
                        var diff: Duration = Duration.between(lowest_distance_difference.toInstant(), highest_distance_difference.toInstant())
                        val days = diff.toDays()
                        diff = diff.minusDays(days)
                        val hours = diff.toHours()
                        diff = diff.minusHours(hours)
                        val minutes = diff.toMinutes()
                        diff = diff.minusMinutes(minutes)
                        val seconds = diff.seconds
                        difference_distance_view?.text="Levou aproximandamente "+days.toString()+" dias, "+hours.toString()+"h "+minutes.toString()+"min "+seconds.toString()+"s para esvaziar o reservatório!"
                    }else{
                        var diff: Duration = Duration.between(highest_distance_difference.toInstant(), lowest_distance_difference.toInstant())
                        val days = diff.toDays()
                        diff = diff.minusDays(days)
                        val hours = diff.toHours()
                        diff = diff.minusHours(hours)
                        val minutes = diff.toMinutes()
                        diff = diff.minusMinutes(minutes)
                        val seconds = diff.seconds
                        difference_distance_view?.text="Levou aproximandamente "+days.toString()+" dias, "+hours.toString()+"h "+minutes.toString()+"min "+seconds.toString()+"s para encher o reservatório!"
                    }

                    val highest_distance_view=view?.findViewById<TextView>(R.id.highest_distance)
                    highest_distance_view?.text="Maior distância:\n"+highest_distance.toString()+"mm"+"\n"+time_highest_distance
                    val lowest_distance_view=view?.findViewById<TextView>(R.id.lowest_distance)
                    lowest_distance_view?.text="Menor distância:\n"+lowest_distance.toString()+"mm"+"\n"+time_lowest_distance
                    lineGraphView_Distance?.animate()
                    lineGraphView_Distance.viewport.borderColor=Color.parseColor("#FF0000")
                    series_distance.color = Color.parseColor("#0365A7")
                    lineGraphView_Distance?.addSeries(series_distance)
                    lineGraphView_Distance.gridLabelRenderer.verticalAxisTitle="mm"
                    lineGraphView_Distance.gridLabelRenderer.horizontalAxisTitle="Número de Registros"
                    lineGraphView_Distance.gridLabelRenderer.numHorizontalLabels=6
                    lineGraphView_Distance.gridLabelRenderer.numVerticalLabels=8
                    lineGraphView_Distance.gridLabelRenderer.setHumanRounding(true,true)
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