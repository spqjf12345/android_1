package com.example.android1

import android.content.Intent

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_2.*

class Fragment2 : Fragment() {
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

     fun takePicture() {
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }


    val gallery =0

    fun loadImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "load picture"), gallery)
    }



}