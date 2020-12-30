package com.example.android1

import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = PageAdapter(supportFragmentManager)
        adapter.addFragment(Fragment1(), "연락처")
        adapter.addFragment(Fragment2(), "사진")
        adapter.addFragment(Fragment3(), "자유")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}