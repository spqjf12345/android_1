package com.example.android1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //페이지어뎁터를 받아와 프래그멘트 실제 연결
        val adapter = PageAdapter(supportFragmentManager)

        adapter.addFragment(Fragment1(), "연락처")
        adapter.addFragment(Fragment2(), "사진")
        adapter.addFragment(Fragment3(), "자유")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}