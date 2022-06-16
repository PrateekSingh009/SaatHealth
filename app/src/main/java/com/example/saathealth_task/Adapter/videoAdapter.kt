package com.example.saathealth_task.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saathealth_task.R
import com.example.saathealth_task.activity.PlayerActivity
import com.example.saathealth_task.model.VideoData

class videoAdapter(private val activity: AppCompatActivity,val list:ArrayList<VideoData>) : RecyclerView.Adapter<videoAdapter.ViewHolder>() {


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txt : TextView = view.findViewById(R.id.videoName)
        val image : ImageView = view.findViewById(R.id.videoImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): videoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: videoAdapter.ViewHolder, position: Int) {
        holder.txt.text = list[position].title
        Glide.with(activity.applicationContext)
            .asBitmap()
            .load(list[position].thumbnail)
            .apply(RequestOptions().placeholder(R.drawable.play_icon).centerCrop())
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent =  Intent(activity, PlayerActivity::class.java)
            intent.putExtra("Url",list[position].video)
            intent.putExtra("Caption",list[position].title)


            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}