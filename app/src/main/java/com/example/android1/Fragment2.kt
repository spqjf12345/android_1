package com.example.android1

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_2.*

class Fragment2 : Fragment() {

    private val pickImage = 100
    private val capturePhoto = 101
    private var imageUri:Uri? = null
    private var imagePath:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_camera.setOnClickListener{
            takePicture()
        }
        //갤러리 버튼 선택시 이미지 로드
        btn_gallery.setOnClickListener{
            loadImage()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_2, container, false)

        return rootView
    }

    private fun loadImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == pickImage){
            imageUri = data?.data
            gallery.setImageURI(imageUri)
        }
        if(resultCode == RESULT_OK && requestCode == capturePhoto){
            var bundle : Bundle? = data?.getExtras()
            var bitmap : Bitmap = bundle?.get("data") as Bitmap
            gallery.setImageBitmap(bitmap)
        }
    }

    private fun takePicture() {
        //카메라 앱 실행
        var capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(capture, capturePhoto)
    }
}