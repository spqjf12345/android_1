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
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.*
import org.json.JSONObject


class Fragment1 : Fragment() {

    val list = ArrayList<list_item>()
    //val list = ArrayList<String>()
    lateinit var recyclerView1 : RecyclerView
    var permissions = arrayOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE)
    lateinit var adapter:contactAdapter

    var searchText = ""
    var sortText = "asc"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        /* add Button */
        addButton.setOnClickListener{
            val name =addName.text.toString()
            val number = addNumber.text.toString()
            if(name.isNotEmpty() && number.isNotEmpty()){
                list.add(list_item(name, number))
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
            if(check) startProcess()
            else {
                Toast.makeText(this.context, "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun changeList() {
        val newList = getPhoneNumbers(sortText, searchText)
        this.list.clear()
        this.list.addAll(newList)
        this.adapter.notifyDataSetChanged()
    }
    fun getPhoneNumbers(sort:String, searchName:String?): ArrayList<list_item> {
        // 결과목록 미리 정의
        val list = ArrayList<list_item>()
        // 1. 주소록 Uri
        val addressUri = ContactsContract.Contacts.CONTENT_URI
        // 1. 전화번호 Uri
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        // 2.1 전화번호에서 가져올 컬럼 정의
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER)
        // 2.2 조건 정의
        var wheneClause:String? = null
        var whereValues = Array(100,{""})
        // searchName에 값이 있을 때만 검색을 사용한다
        if(searchName?.isNotEmpty() ?: false) {
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "like ?"
            var i: Int = 0
            whereValues.set(i++, searchName!!)

        }
        // 2.3 정렬쿼리 사용
        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " $sort"
        // 3. 테이블에서 주소록 데이터 쿼리
        val resolver = activity?.contentResolver
        val cursor = resolver?.query(phoneUri, projections, wheneClause, whereValues, optionSort)
        // 4. 반복문으로 아이디와 이름을 가져오면서 전화번호 조회 쿼리를 한번 더 돌린다.
        while(cursor?.moveToNext()?:false) {
            //val id = cursor?.getString(0)
            val name = cursor?.getString(1).toString()
            var number = cursor?.getString(2).toString()
            // 개별 전화번호 데이터 생성
            val phone = Phone(name, number)
            // 결과목록에 더하기
            list.add(list_item(name, number))
        }
        // 결과목록 반환
        return list
    }

    fun startProcess() {
        list.addAll(getPhoneNumbers(sortText, searchText))
        adapter = contactAdapter(list)
        recyclerView1.adapter = adapter
        recyclerView1.layoutManager = LinearLayoutManager(this.context)

        /*editSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                changeList()
            }
        })*/
        /*radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioAsc -> sortText = "asc"
                R.id.radioDsc -> sortText = "desc"
            }
            changeList()
        }*/

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
            startProcess()
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



data class Phone(val name:String?, val phone:String?)



