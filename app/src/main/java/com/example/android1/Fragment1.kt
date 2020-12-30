package com.example.android1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.*
import org.json.JSONObject


class Fragment1 : Fragment() {
    val list = ArrayList<list_item>()
    lateinit var recyclerView1 : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addButton.setOnClickListener{
            val name =addName.text.toString()
            val number = addNumber.text.toString()
            if(name.isNotEmpty() && number.isNotEmpty()){
                list.add(list_item(name, number))
            }
        }
        refreshFragment(this, supportFragmentManager())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val assetManager = resources.assets
        val inputStream = assetManager.open("Contacts.json")
        val jsonString = inputStream.bufferedReader().use{it.readText()}
        val jObject = JSONObject(jsonString)
        var jArray = jObject.getJSONArray("person")

        for (i in 0 until jArray.length()) {
            val obj = jArray.getJSONObject(i)
            val name = obj.getString("name")
            val number = obj.getString("number")
            Log.d("TAG", "name($i): $name")
            Log.d("TAG", "number($i): $number")
            list.add(list_item(name, number))
        }
    }

    //여기에 그리기
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var rootView =  inflater.inflate(R.layout.fragment_a, container, false)

        recyclerView1 = rootView.findViewById(R.id.rv_json!!)as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(this.context)
        recyclerView1.adapter = contactAdapter(list)
        recyclerView1.setHasFixedSize(true)

        return rootView
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

}