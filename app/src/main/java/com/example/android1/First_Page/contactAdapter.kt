package com.example.android1

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class contactAdapter(val JsonList:ArrayList<list_item>):
        RecyclerView.Adapter<contactAdapter.ViewHolder>(), Filterable {

    private var filterList: ArrayList<list_item> = JsonList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.tv_name)
        var number = itemView.findViewById<TextView>(R.id.tv_number)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactAdapter.ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return contactAdapter.ViewHolder(inflatedView).apply {
            /*Toast message*/
            itemView.setOnLongClickListener(object :View.OnLongClickListener{
                    override fun onLongClick(v: View?): Boolean {
                        val curPos: Int = adapterPosition
                        var item: list_item = filterList.get(curPos)

                        //JsonList.removeAt(curPos)
                        Toast.makeText(parent.context,
                            "${curPos}\n ${item.name}\n ${item.number}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return true
                    }

                })

            /* call */
            itemView.iv_call.setOnClickListener {
                val curPos: Int = adapterPosition
                var item: list_item = filterList.get(curPos)
                val intent_call = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.number))
                parent.context.startActivity(intent_call)
            }
            /* message */
            itemView.iv_mms.setOnClickListener {
                val curPos: Int = adapterPosition
                var item: list_item = filterList.get(curPos)
                val intent_mms = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + item.number))
                parent.context.startActivity(intent_mms)

            }


            /*edit*/


        }
    }


    override fun getItemCount(): Int {
        return this.filterList.size
    }

    override fun onBindViewHolder(holder: contactAdapter.ViewHolder, position: Int) { //class ViewHolder을 연결
        holder.name.setText((filterList[position].name))
        holder.number.text = (filterList[position].number)

        /*delete*/
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(view: View?): Boolean {
                //val curPos: Int = adapterPosition
                //var item: list_item = filterList.get(curPos)

                //Log.d("react", "react")
                //다이얼로그 생성
                var builder = AlertDialog.Builder(view?.context)
                Log.d("builder", builder.toString())
                val inflater = LayoutInflater.from(view?.context)
                Log.d("inflater", inflater.toString())
                val dialogView = inflater.inflate(R.layout.custom_dialog, null)
                val dialogText = dialogView.findViewById<TextView>(R.id.dg_content)

                builder.setView(dialogView)
                    .setPositiveButton("확인") { dialogInterface, i ->
                        builder.setTitle(dialogText.text.toString())
                        //json 파일 불러오기
                        //
                        Toast.makeText(view?.context, "파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소") { dialogInterface, i ->
                    }
                    .show()
                return true
            }
        })


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getFilter(): Filter
    {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                filterList.clear()
                Log.d("Filter", "change the list elements")
                if (charString.isEmpty()) {
                    filterList.addAll(JsonList)
                } else {
                    val filterPattern = charString.toLowerCase(Locale.ROOT)
                    for (item in JsonList) {
                        if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filterList.add(item)
                        }
                    }
                }


                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("Filter", "change the list elements?")
                filterList = results?.values as ArrayList<list_item>
                notifyDataSetChanged()
            }

            fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
                var ft: FragmentTransaction = fragmentManager.beginTransaction()
                ft.detach(fragment).attach(fragment).commit()
            }
        }
    }




}


