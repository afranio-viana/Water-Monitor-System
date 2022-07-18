package com.wms.watermonitorsystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wms.watermonitorsystem.fragments.monitoring
import com.wms.watermonitorsystem.fragments.reports

class MenuAplication : AppCompatActivity() {
    private val monitoring=monitoring()
    private val reports=reports()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_aplication)
        val bottom_navigation=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        replaceFragment(monitoring)
        bottom_navigation?.setOnItemSelectedListener {item->
            when(item.itemId){
                R.id.page_1-> replaceFragment(monitoring)
                R.id.page_2-> replaceFragment(reports)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    }
}