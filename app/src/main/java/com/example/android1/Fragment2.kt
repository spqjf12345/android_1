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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_2.*

class Fragment2 : Fragment() {
    lateinit var recyclerView2 : RecyclerView
    val image_list = ArrayList<image_item>()
    private val pickImage = 100
    private val capturePhoto = 101
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
        recyclerView2 = rootView.findViewById(R.id.rv_image!!)as RecyclerView
        recyclerView2.layoutManager = GridLayoutManager(this.context,3)
        recyclerView2.adapter = ImageAdapter(image_list)
        recyclerView2.setHasFixedSize(true)

        /*val mAdapter = ImageAdapter(image_list)
        rv_image.adapter = mAdapter
        rv_image.layoutManager = LinearLayoutManager(this.context)*/
        return rootView
    }

    private fun loadImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            if (requestCode == pickImage) {
                imageUri = data?.data
                image_list.add(image_item(false, null, imageUri))
                //gallery.setImageURI(imageUri)
            }
            if (requestCode == capturePhoto) {
                var bundle: Bundle? = data?.getExtras()
                var bitmap: Bitmap = bundle?.get("data") as Bitmap
                image_list.add(image_item(true, bitmap, null))
                //gallery.setImageBitmap(bitmap)
            }
            refreshFragment(this, parentFragmentManager)
        }
    }

    private fun takePicture() {
        //카메라 앱 실행
        var capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(capture, capturePhoto)
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }
}