package com.example.prov_proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImagePagerAdapt(private val images: ArrayList<String>) :
    RecyclerView.Adapter<ImagePagerAdapt.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = images[position]
        Glide.with(holder.imageView)
            .load(imageItem)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}