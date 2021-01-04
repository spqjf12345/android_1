package com.example.android1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android1.Third_Page.MapActivity
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_3.*


class Fragment3 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_Location.setOnClickListener{
            startActivity(Intent(it.context, MapActivity::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(com.example.android1.R.layout.fragment_3, container, false)
        return rootView
    }

}