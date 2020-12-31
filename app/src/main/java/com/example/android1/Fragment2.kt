package com.example.android1

import android.app.Activity.RESULT_OK
import android.content.Intent
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
    private var imageUri:Uri? = null

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
    }

    private fun takePicture() {
        //카메라 앱 실행
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }
}