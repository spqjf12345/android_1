package com.example.android1

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.big_image.*

class ShowBigImage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.big_image)

        val big_image_intent: Intent = Intent.getIntentOld()
        val item:image_item = big_image_intent.getSerializableExtra("big_image_source")
        if(item.isPhoto){
            big_gallery.setImageBitmap(item.photo)
        }
        else{
            big_gallery.setImageURI(item.path)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
    }
}