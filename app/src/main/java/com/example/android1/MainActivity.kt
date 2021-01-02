package com.example.android1


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import kotlinx.android.synthetic.main.activity_main.*


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
        //권한 허용
        getPermission()

    }

    private fun getPermission() {
        var permission = object: PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity,"권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity,"권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 사용을 위해 권한을 허용해주세요")
            .setDeniedMessage("권한을 거부하였습니다.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }







}

