package com.example.android1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(val imageList:ArrayList<image_item>):
    RecyclerView.Adapter<ImageAdapter.viewHolder>(){

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.image_holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return viewHolder(view).apply{
            itemView.setOnClickListener(object:View.OnClickListener{
                override fun onClick(v: View?) {
                    val curPos: Int = adapterPosition
                    var item: image_item = imageList.get(curPos)
                    var big_image_intent:Intent = Intent()
                    big_image_intent.putExtra("bit_image_source", item)
                    startActivity(big_image_intent)
                }
            })
        }
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if(imageList.get(position).isPhoto){
            holder.image?.setImageBitmap(imageList.get(position).photo)
        }
        else{
            holder.image?.setImageURI(imageList.get(position).path)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}