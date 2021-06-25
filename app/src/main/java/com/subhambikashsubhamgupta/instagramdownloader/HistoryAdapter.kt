package com.subhambikashsubhamgupta.instagramdownloader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.net.toFile
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

        holder.share.setOnClickListener {
            val intent1 = Intent(Intent.ACTION_SEND)
            intent1.type = "image/*"
            intent1.type = "text/plain"
            intent1.putExtra(Intent.EXTRA_SUBJECT, "REEL DOWNLOADER")
            val shareMessage=list.url
            intent1.putExtra(Intent.EXTRA_STREAM, shareMessage)
            val shareMessage2="https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
            intent1.putExtra(Intent.EXTRA_TEXT, shareMessage2)
            context.startActivity(Intent.createChooser(intent1, "share by"))
        }




    }

    override fun getItemCount(): Int = models.size


    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var  imageView = itemView.findViewById<ImageView>(R.id.imageView2)
        var share=itemView.findViewById<ImageView>(R.id.sharevideo)

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