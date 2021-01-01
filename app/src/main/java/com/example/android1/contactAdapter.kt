package com.example.android1

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList


class contactAdapter(private val JsonList:ArrayList<list_item>, private var filterList:ArrayList<list_item>):
        RecyclerView.Adapter<contactAdapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.tv_name)
        var number = itemView.findViewById<TextView>(R.id.tv_number)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactAdapter.ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return contactAdapter.ViewHolder(inflatedView).apply {

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
                itemView.setOnClickListener(object:View.OnClickListener{
                    override fun onClick(v: View?) {
                        val curPos: Int = adapterPosition
                        var item: list_item = filterList.get(curPos)
                        val intent_call = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+item.number))
                        parent.context.startActivity(intent_call)
                    }
                })
            }
        }

    override fun getItemCount(): Int {
       return filterList.size
    }

    override fun onBindViewHolder(holder: contactAdapter.ViewHolder, position: Int) { //class ViewHolder을 연결
        holder.name.setText((filterList.get(position).name))
        holder.number.setText((filterList.get(position).number))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    fun filter(charText:String){
        var charTexts = charText.toLowerCase(Locale.getDefault())
        filterList.clear()
        if(charTexts.isEmpty()){
           filterList.addAll(JsonList)
        }
        else{
            for(item:list_item){
                val name:String =
            }
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        potionList.clear();
        if (charText.length() == 0) {
            potionList.addAll(arrayList);
        } else {
            for (Potion potion : arrayList) {
                String name = context.getResources().getString(potion.name);
                if (name.toLowerCase().contains(charText)) {
                    potionList.add(potion);
                }
            }
        }
        notifyDataSetChanged();
    }

}