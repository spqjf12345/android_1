package com.example.android1

import android.media.Image
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter(private val ImageList:ArrayList<Uri>): RecyclerView.Adapter<ImageAdapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var image = itemView.findViewById<ImageView>(R.id.gallery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageAdapter.ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        val size = ImageList.size
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(ImageList[position])


        //list_image에 ADD 시킨 ImageList를 imageresource로 출력
       // holder.image.setImageResource()
    }
}