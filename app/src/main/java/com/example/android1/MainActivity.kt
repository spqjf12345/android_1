package com.example.android1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = PageAdapter(supportFragmentManager)
        adapter.addFragment(A_Fragment(), "연락처")
        adapter.addFragment(B_Fragment(), "사진")
        adapter.addFragment(C_Fragment(), "자유")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}