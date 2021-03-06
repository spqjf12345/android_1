package com.example.android1


import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.*
import org.json.JSONObject

class Fragment1 : Fragment() {
    val list = ArrayList<list_item>()
    //lateinit var adapter:contactAdapter

    lateinit var recyclerView1: RecyclerView
    var permissions = arrayOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE)

    var searchText = ""
    var sortText = ""
    var serach:CharSequence = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        /* add Button */
       /* addButton.setOnClickListener {
            val id: String = ""
            val name = addName.text.toString()
            val number = addNumber.text.toString()
            if (name.isNotEmpty() && number.isNotEmpty()) {
                list.add(list_item(id, name, number))
            }
            refreshFragment(this, parentFragmentManager)
        }*/

        btn_add.setOnClickListener {

            /* custom_add_dialog */
           var dilaog01 = Dialog(view?.context);
            dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val inflater = LayoutInflater.from(view?.context)
            val dialogView = inflater.inflate(R.layout.cutom_add_dialog, null)
            dilaog01.setContentView(dialogView)
            dilaog01.show()
            val ADD_Button: Button = dialogView.findViewById(R.id.addButton_)
            var CANCEL_Button : Button = dialogView.findViewById(R.id.cancelButton_)

            /*cancel button*/

            CANCEL_Button.setOnClickListener {
                //(object : View.OnClickListener {
                //    override fun onClick(view: View?) {
                        dilaog01.dismiss()
                    //}
               // }
                        //)
            }

            ADD_Button.setOnClickListener {

                //(object : view.OnClickListener {
                    //override fun onClick(view: View?) {
                        Log.d("add_click1", "add_click1")
                        val id: String = ""
                        val dialogName = dialogView?.findViewById<TextView>(R.id.addName_)?.text.toString()
                        val dialogNumber = dialogView?.findViewById<TextView>(R.id.addNumber_)?.text.toString()
                        Log.d("dialogName", dialogName)
                        Log.d("dialogNumber", dialogNumber)
                       // if (dialogName.isNotEmpty() && dialogNumber.isNotEmpty()) {
                            list.add(list_item(id, dialogName, dialogNumber))
                            for(i in list) {
                                Log.d("dialogName_dialogNumber", i.name + i.number)
                            }
                            dilaog01.dismiss()
                            contactAdapter(list).notifyItemInserted(0);
                            //refreshFragment(this, parentFragmentManager)

                            Log.d("get_load_add", "get_load_add_button")
                        //}

                        Log.d("get_load_add", "get_load_add_button")

                        //dilaog01.dismiss()

                    }




                //})


            //}


            /*alterdialog*/
            /*var add_builder = AlertDialog.Builder(view?.context)
            //val inflater = LayoutInflater.from(view?.context)
            //val dialogView = inflater.inflate(R.layout.cutom_add_dialog, null)
            add_builder.setView(dialogView)
                .setPositiveButton("ADD") { dialogInterface, i ->
//                    add_builder.setTitle(dialogText.text.toString())
                    val id: String = ""
                    val dialogName = dialogView?.findViewById<TextView>(R.id.addName_)?.text.toString()
                    val dialogNumber = dialogView?.findViewById<TextView>(R.id.addNumber_)?.text.toString()
                    list.add(list_item(id, dialogName, dialogNumber))
                    //refreshFragment(this, parentFragmentManager)
                    contactAdapter(list).notifyItemInserted(0);

                }
                .setNegativeButton("CANCEL") { dialogInterface, i ->
                }
                .show()

             */



        }




        contact_Filter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //val ctAdapter = contactAdapter(list)
                contactAdapter(list).getFilter().filter(s)
                serach = s
                Log.d("serach", serach.toString())
                searchText = s.toString()
                Log.d("searchText", searchText)
                changeList()

            }

        })

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
        //setSearchListener()
    }

    fun setList() {
        //list.addAll(getPhoneNumbers(sortText, searchText))
        recyclerView1.adapter?.notifyDataSetChanged()
        //val set_inflater = LayoutInflater.from(context)
        //val set_View = set_inflater.inflate(R.layout.fragment_a, null)

        //recyclerView1 = set_View.findViewById(R.id.rv_json!!)as RecyclerView
        //recyclerView1.layoutManager = LinearLayoutManager(this.context)
        //adapter = contactAdapter(list)
        //recyclerView1.adapter = adapter

    }

    fun changeList() {
        val newList = getPhoneNumbers(sortText, searchText)
        for (item in newList){
            Log.d("List", item.name)
            Log.d("List", item.number)
        }

        list.clear()
        for (item in list){
            Log.d("getItem1", item.name)
            Log.d("getItem1", item.number)
        }
        list.addAll(newList)

        //recyclerView1.adapter = adapter
        rv_json.adapter?.notifyDataSetChanged()
        rv_json.setHasFixedSize(true)

        //recyclerView1 = rootView.findViewById(R.id.rv_json!!)as RecyclerView
        //rv_json.layottManager = LinearLayoutManager(this.context)
        //recyclerView1.adapter?.notifyDataSetChanged()

        //adapter 연결
        //recyclerView1.adapter = contactAdapter(list)
        //recyclerView1.adapter = FilterAdapter(list)

        //rv_json.adapter = adapter
        //rv_json.layoutManager = LinearLayoutManager(this.context)

    }

    fun getPhoneNumbers(sort:String, searchName:String): ArrayList<list_item> {
        //json 파일
        val assetManager = resources.assets
        val inputStream = assetManager.open("test_contracts")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jObject = JSONObject(jsonString)

        val list = ArrayList<list_item>()
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        Log.d("phoneUri",phoneUri.toString() )
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER)



        val resolver = activity?.contentResolver
        var wheneClause:String? = null

        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        var whereValues = arrayOf<String>()
        //var whereValues: Array<String>
        Log.d("searchName", searchName)
        if(searchName.isNotEmpty() ?: false) {
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?"
            whereValues = arrayOf("%$searchName%")

            Log.d("searchName", searchName)
            Log.d("wheneClause", wheneClause.toString())


            Log.d("whereValues", whereValues.toString())
        }

        val cursor = resolver?.query(phoneUri, projections, wheneClause, whereValues, null)

       while(cursor?.moveToNext()?:false) {
            val id = cursor?.getString(0).toString()
            val name = cursor?.getString(1).toString()
            var number = cursor?.getString(2).toString()
           Log.d("name", name)
           Log.d("number", number)
            // json 파일에 넣기
            val main = JSONObject(jsonString)
           jObject.put("person",main)
           main.put("id", id)
           main.put("name", name)
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var rootView =  inflater.inflate(R.layout.fragment_a, container, false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || isPermitted()) {
            recyclerView1 = rootView.findViewById(R.id.rv_json!!)as RecyclerView
            recyclerView1.layoutManager = LinearLayoutManager(this.context)
            list.addAll(getPhoneNumbers(sortText, searchText))
            //adapter 연결
            recyclerView1.adapter = contactAdapter(list)
            recyclerView1.setHasFixedSize(true)
            startProcess()
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), permissions, 99)
        }

        //startProcess()
        return rootView
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
        Log.v("dialog", "Do refresh")
    }



}





