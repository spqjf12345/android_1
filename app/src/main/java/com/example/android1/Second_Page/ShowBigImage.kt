package com.example.android1

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.big_image.*

class ShowBigImage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.big_image)
        var item: image_item? = null
        var uri:Uri
        uri = intent.getParcelableExtra("photo" )!!
        //item?.photo = Uri.parse(intent.getStringExtra("photo" ))
        iv_detail.setImageURI(uri)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
        return true
    }
}