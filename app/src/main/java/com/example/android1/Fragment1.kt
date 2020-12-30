package com.example.android1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class Fragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //여기에 그리기
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView =  inflater.inflate(R.layout.fragment_a, container, false)
        lateinit var recyclerView1 : RecyclerView

        val list = ArrayList<list_item>()
        var friend_DataArray: ArrayList<list_item> = ArrayList()

        friend_DataArray.add(list_item("이름","닉"))
        friend_DataArray.add(list_item("바보","닉"))
        friend_DataArray.add(list_item("이름","닉"))

        list.add(list_item("조소정", "010-3245-2959"))
        list.add(list_item("이준용", "010-3245-2959"))
        list.add(list_item("조소정", "010-3245-2959"))
        list.add(list_item("이준용", "010-3245-2959"))




        recyclerView1 = rootView.findViewById(R.id.rv_json!!)as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(this.context)
        recyclerView1.adapter = contactAdapter(list)

        return rootView
    }


}