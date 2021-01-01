package com.example.android1

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.*


class Fragment1 : Fragment() {
    val list = ArrayList<list_item>()
    lateinit var recyclerView1 : RecyclerView
    var permissions = arrayOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE)

    var searchText = ""
    var sortText = "asc"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* add Button */
        addButton.setOnClickListener{
            val id: String = ""
            val name =addName.text.toString()
            val number = addNumber.text.toString()
            if(name.isNotEmpty() && number.isNotEmpty()){
                list.add(list_item(id, name, number))
            }
            refreshFragment(this, parentFragmentManager)
        }

        /*delButton.setOnClickListener{//delete button 클릭 시
            val cbx = itemClicked.cb_delete
            CheckBox cbx = (CheckBox)itemClick
            if (cb_delete.isChecked){

            }
            val curPos: Int = adapterPosition
            //체크 버튼 눌림
            if(){
                list.remove()//name과 number
            }else{//안눌림
                if()//체크 버튼 하나도 눌려 있지 않을 때
            }
        }*/
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if(requestCode == 99) {
            var check = true
            for(grant in grantResults) {
                if(grant != PackageManager.PERMISSION_GRANTED) {
                    check = false
                    break
                }
            }
            if(check) {
                list.addAll(getPhoneNumbers(sortText, searchText))
                changeList()
            }
            else {
                Toast.makeText(this.context, "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun changeList() {
        val newList = getPhoneNumbers(sortText, searchText)
        Log.d("newList", newList.toString())
        this.list.clear()
        this.list.addAll(newList)
        //this.adapter.notifyDataSetChanged()
    }

    fun getPhoneNumbers(sort:String, searchName:String?): ArrayList<list_item> {
        val list = ArrayList<list_item>()
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER)

        val resolver = activity?.contentResolver

        val cursor = resolver?.query(phoneUri, projections, null, null, null)
        Log.d("cursor", cursor.toString())

       while(cursor?.moveToNext()?:false) {
            val id = cursor?.getString(0).toString()
            val name = cursor?.getString(1).toString()
            var number = cursor?.getString(2).toString()
            list.add(list_item(id, name, number))
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isPermitted():Boolean {
        for(perm in permissions) {
            if(ActivityCompat.checkSelfPermission(this.requireContext(), perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || isPermitted()) {
            list.addAll(getPhoneNumbers(sortText, searchText))
            changeList()
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), permissions, 99)
        }

            /*val assetManager = resources.assets
            val inputStream = assetManager.open("Contacts.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jObject = JSONObject(jsonString)
            var jArray = jObject.getJSONArray("person")
            for (i in 0 until jArray.length()) {
                val obj = jArray.getJSONObject(i)
                val name = obj.getString("name")
                val number = obj.getString("number")
                Log.d("TAG", "name($i): $name")
                Log.d("TAG", "number($i): $number")
                list.add(list_item(name, number))
            }*/
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

data class Phone(val id: String?, val name:String?, val phone:String?)



