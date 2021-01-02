package com.example.android1.Third_Page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android1.ImageAdapter
import com.example.android1.R
import com.example.android1.contactAdapter

class GPSAdapter():RecyclerView.Adapter<GPSAdapter.viewHolder>() {
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testText = itemView.findViewById<TextView>(R.id.test)
        val recyclerView1 = itemView.findViewById<RecyclerView>(R.id.recyclerGPS)
        val btn_GPS = itemView.findViewById<Button>(R.id.btn_Location)
        val mainText = itemView.findViewById<TextView>(R.id.recFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GPSAdapter.viewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.big_image, parent, false)
        return GPSAdapter.viewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return 0//imageList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        //holder.image?.setImageURI(imageList.get(position).photo)
    }

}