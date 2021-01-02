package com.example.android1


import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android1.First_Page.FilterAdapter
import kotlinx.android.synthetic.main.fragment_a.*
import org.json.JSONObject

class Fragment1 : Fragment() {
    val list = ArrayList<list_item>()
    lateinit var recyclerView1 : RecyclerView
    var permissions = arrayOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE)

    var searchText = ""
    var sortText = ""

    private var filterAdapter: FilterAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* add Button */
        addButton.setOnClickListener {
            val id: String = ""
            val name = addName.text.toString()
            val number = addNumber.text.toString()
            if (name.isNotEmpty() && number.isNotEmpty()) {
                list.add(list_item(id, name, number))
            }
            refreshFragment(this, parentFragmentManager)
        }

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 99) {
            var check = true
            for(grant in grantResults) {
                if(grant != PackageManager.PERMISSION_GRANTED) {
                    check = false
                    break
                }
            }
            if(check) {
                startProcess()
            }
            else {
                Toast.makeText(this.context, "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun startProcess() {
        setList()
        setSearchListener()
    }
    fun setList() {
        list.addAll(getPhoneNumbers(sortText, searchText))
    }
    fun setSearchListener() {
        contact_Filter.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("searchText",searchText)
                searchText = s.toString()
                Log.d("searchText",searchText)
                changeList()
            }
        })
    }


    fun changeList() {
        val newList = getPhoneNumbers(sortText, searchText)
        this.list.clear()
        this.list.addAll(newList)
    }

    fun getPhoneNumbers(sort:String, searchName:String): ArrayList<list_item> {
        //json 파일
        val assetManager = resources.assets
        val inputStream = assetManager.open("test_contracts")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jObject = JSONObject(jsonString)

        val list = ArrayList<list_item>()
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER)
        val resolver = activity?.contentResolver
        var wheneClause:String? = null

        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + sort
        var whereValues = arrayOf<String>()
        //var whereValues: Array<String>
        Log.d("searchName", searchName)
        if(searchName.isNotEmpty() ?: false) {
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "like ?"
            whereValues = arrayOf(searchName)

            Log.d("searchName", searchName)
            Log.d("wheneClause", wheneClause.toString())
            Log.d("whereValues", whereValues.toString())
        }

        val cursor = resolver?.query(phoneUri, projections, null, null, null)

       while(cursor?.moveToNext()?:false) {
            val id = cursor?.getString(0).toString()
            val searchName = cursor?.getString(1).toString()
            var number = cursor?.getString(2).toString()

            // json 파일에 넣기
            val main = JSONObject(jsonString)
           jObject.put("person",main)
           main.put("id", id)
           main.put("name", searchName)
           main.put("number", number)

           //넣은 값을 불러와서 list item 에 부여
            for (i in 0 until jObject.length()) {
                //var jArray = jObject.getJSONArray("person")
                val obj = jObject.getJSONObject("person")
                Log.d("obj", obj.toString())
                val json_id = obj.getString("id")
                val json_name = obj.getString("name")
                val json_number = obj.getString("number")
                list.add(list_item(json_id, json_name, json_number))
            }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView =  inflater.inflate(R.layout.fragment_a, container, false)
        recyclerView1 = rootView.findViewById(R.id.rv_json!!)as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(this.context)
        recyclerView1.adapter?.notifyDataSetChanged()

        filterAdapter = FilterAdapter(list)
        recyclerView1.adapter = contactAdapter(list)
        recyclerView1.adapter = FilterAdapter(list)

        recyclerView1.setHasFixedSize(true)
        return rootView
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }



}





