package com.example.aftomaster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aftomaster.View.JournalFragment
import com.example.aftomaster.View.ProductFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bnv = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btwarehouse -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fram, ProductFragment())
                        .commit()
                    true
                }
                R.id.btjournal -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fram, JournalFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}