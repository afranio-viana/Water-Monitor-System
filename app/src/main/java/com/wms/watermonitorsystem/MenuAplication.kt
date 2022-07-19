package com.wms.watermonitorsystem

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wms.watermonitorsystem.fragments.monitoring
import com.wms.watermonitorsystem.fragments.reports

class MenuAplication : AppCompatActivity() {
    private val monitoring=monitoring()
    private val reports=reports()
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_aplication)
        bottomNav=findViewById(R.id.bottom_navigation) as BottomNavigationView
        replaceFragment(monitoring)

        bottomNav.setOnItemSelectedListener (){
            when(it.itemId){
                R.id.page_1->{

                    replaceFragment(monitoring)
                }
                R.id.page_2->{
                    replaceFragment(reports)
                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun click_here(view: android.view.View){
        Toast.makeText(baseContext, "gdfgs", Toast.LENGTH_SHORT).show()
    }



}