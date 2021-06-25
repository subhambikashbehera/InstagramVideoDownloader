package com.subhambikashsubhamgupta.instagramdownloader

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView


class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    var models= arrayListOf<HistoryModel>()
    var context:FragmentActivity

    constructor(models: ArrayList<HistoryModel>, context: FragmentActivity) : super() {
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

            context.grantUriPermission(
                "com.subhambikashsubhamgupta.instagramdownloader",
                list.url,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val URI: Uri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",list.file

            )
            Log.e("file", list.file.toString());

            val shareIntent = Intent()
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, URI)
            shareIntent.type = "video/*"
            context.startActivity(Intent.createChooser(shareIntent, "Share Video to.."))

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