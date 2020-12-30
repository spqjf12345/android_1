package com.example.android1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.*

class A_Fragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = ArrayList<list_item>()
        list.add(list_item("조소정", "010-3245-2959"))
        list.add(list_item("이준용", "010-3245-2959"))
        list.add(list_item("조소정", "010-3245-2959"))
        list.add(list_item("이준용", "010-3245-2959"))
        val myadapter = contactAdapter(list)
        A_Fragment.adapter = myadapter

        //val jsonString =
        //tv_json.text =
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }


}