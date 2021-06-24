package com.subhambikashsubhamgupta.instagramdownloader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView


class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    var models= arrayListOf<HistoryModel>()
    var context:Context

    constructor(models: ArrayList<HistoryModel>, context: Context) : super() {
        this.models = models
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historyitem, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
       val list=models[position]


        Log.d("bitmap", "onBindViewHolder: ${list.urlthum}")
        holder.imageView.setImageBitmap(list.urlthum)

        holder.imageView.setOnClickListener {
            val intent=Intent(context,fullvideo::class.java)
            intent.putExtra("video",list.url.toString())
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = models.size


    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var  imageView = itemView.findViewById<ImageView>(R.id.imageView2)


    }



    fun clear() {
        models.clear()
        notifyDataSetChanged()
    }

    fun addAll(videolist:List<HistoryModel>) {
        models.addAll(videolist)
        notifyDataSetChanged()
    }
}