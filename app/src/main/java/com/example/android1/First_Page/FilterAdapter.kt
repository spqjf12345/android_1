package com.example.android1.First_Page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android1.R
import com.example.android1.list_item
import java.util.*

class FilterAdapter (var Filter_List: ArrayList<list_item>): RecyclerView.Adapter<FilterAdapter.ViewHolder>(), Filterable{
    private lateinit var filteredList: ArrayList<list_item>
    private lateinit var unfilterList: ArrayList<list_item>


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.tv_name)
        var number = itemView.findViewById<TextView>(R.id.tv_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return FilterAdapter.ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
       return this.filteredList.size
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        holder.name.text = filteredList[position].name
        holder.number.text = filteredList[position].number
    }


    override fun getFilter(): Filter
    {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                //FilterList.clear()
                //Log.d("Filter", "change the list elements")
                filteredList = if (charString.isEmpty()) {
                     unfilterList
                } else {
                    var FilteringList = ArrayList<list_item>()
                    val filterPattern = charString.toLowerCase(Locale.ROOT)
                    for (item in unfilterList) {
                        if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            FilteringList.add(item)
                        }
                    }
                    FilteringList
                }


                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<list_item>
                notifyDataSetChanged()
            }


        }
    }
}

