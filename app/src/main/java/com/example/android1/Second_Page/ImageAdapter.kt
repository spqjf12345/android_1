package com.example.android1

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(val imageList:ArrayList<image_item>):
    RecyclerView.Adapter<ImageAdapter.viewHolder>(){

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.gallery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)

        return viewHolder(view).apply{
            itemView.setOnClickListener(object:View.OnClickListener{
                override fun onClick(v: View?) {
                    val curPos: Int = adapterPosition
                    var item: image_item = imageList.get(curPos)
                    var intent: Intent = Intent(v?.context, ShowBigImage::class.java)
                    intent.putExtra("photo", item.photo)
                    Log.d("putExtra", intent.putExtra("photo", item.photo).toString())

                    v?.context?.startActivity(intent)
                }
            })
        }


    }




    override fun onBindViewHolder(holder: viewHolder, position: Int) {
            holder.image?.setImageURI(imageList.get(position).photo)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}