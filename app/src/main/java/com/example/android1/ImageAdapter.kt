package com.example.android1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(val context: Context, val imageList:ArrayList<image_item>):
    RecyclerView.Adapter<ImageAdapter.Holder>(){

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val image = itemView?.findViewById<ImageView>(R.id.image_holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(imageList.get(position).isPhoto){
            holder.image.setImageBitmap(imageList.get(position).photo)
        }
        else{
            holder.image.setImageURI(imageList.get(position).path)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}